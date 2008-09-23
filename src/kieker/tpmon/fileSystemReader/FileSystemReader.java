package kieker.tpmon.fileSystemReader;

import java.io.*;
import java.util.*;
import kieker.tpmon.TpmonController;

/**
 * This reader allows one to read a 
 * folder or an single tpmon file and 
 * transforms it to monitoring events
 * that are stored in the file system 
 * again, written to a database, or 
 * whatever tpmon is configured to do
 * with the monitoring data.
 * 
 *
 * @author Matthias Rohr
 * 
 * History:
 * 2008/09/15: Initial version
 *  
 */
public class FileSystemReader {
    private static FileSystemReader instance;

    // instance variables
    private TpmonController ctrl = null;
    private File inputDir = null;
        
        
    public static void main(String[] args) {
        
//       Properties props = System.getProperties();
//       Iterator it = props.keySet().iterator();
//       while(it.hasNext()) {
//           Object curKey = it.next();
//           System.out.println("Key "+curKey.toString() + " "+props.getProperty(curKey.toString()).toString());
//       }
        
       String inputDir = System.getProperty("inputDir");
       if (inputDir == null || inputDir.length()==0 || inputDir.equals("${inputDir}")){
           System.out.println("FileSystemReader>  No input dir found!");
           System.out.println("FileSystemReader>  Provide an input dir as system property.");
           System.out.println("FileSystemReader>  Example to read all tpmon-* files from /tmp:\n" +
                             "                    ant -DinputDir=/tmp/ run-reader    ");
           System.exit(1);
       } else {
            System.out.println("FileSystemReader>  Reading all tpmon-* files from "+inputDir);            
       }
     
       

        instance = FileSystemReader.instance();  
        
        instance.setInputDir(new File(inputDir));
              
        System.out.println("FileSystemReader>  Activating Tpmon");
        if (!instance.setCtrl(TpmonController.getInstance())) {
            System.out.println("FileSystemReader>  Initialization of tpmon failed");
            System.exit(1);            
        }
        System.out.println("FileSystemReader>  Tpmon initialized");
        
        System.out.println("FileSystemReader>  Staring to read files");
        instance.openAndRegisterData();        
        System.out.println("FileSystemReader>  Finished to read files");
        System.exit(0);
    }
    
    public boolean setCtrl(TpmonController ctrl) {
        this.ctrl = ctrl;
        return (ctrl != null);
    }        
    
    
    
    /**
     * @return the singleton instance of this class
     */
    public static synchronized FileSystemReader instance()
    {
        if (instance == null) instance = new FileSystemReader();
        return instance;
    }
    
    public void openAndRegisterData()
    {
        if (inputDir == null) throw new IllegalStateException("call setInputDir first");
       
        try
        {
            File[] inputFiles = inputDir.listFiles(new FileFilter() {
                public boolean accept(File pathname)
                {
                    return
                        pathname.isFile() &&
                        pathname.getName().startsWith("tpmon") &&
                        pathname.getName().endsWith(".dat");
                } });
            for (int i = 0; i < inputFiles.length; i++)
                processInputFile(inputFiles[i]);
        }
        catch (IOException e)
        {
            System.err.println(
                "An error occurred while parsing files from directory " +
                inputDir.getAbsolutePath() + ":");
            e.printStackTrace();
        }

    }

  

//    
//     long maximumNumberOfTracesToLoad = Long.MAX_VALUE;
//     /**
//     * Limits the number of traces to load from the data source -- this should
//     * allow to speed up exploring experiment data. Not every datasource might
//     * really obey to this limit.
//     * 
//     * default value: Long.MAX_VALUE (basically no limit, memory will be the limit earlier...)
//     * @param numberOfTracesToLoad
//     */
//    public void setMaximumNumberOfTracesToLoad(long numberOfTracesToLoad) {        
//        maximumNumberOfTracesToLoad = numberOfTracesToLoad;
//    }
    
    /**
     * Configures the input directory that will be processed by
     * the next call to {@link #openAndRegisterData()}
     */
    public void setInputDir(File inputDir)
    {
        if (inputDir == null )
            throw new IllegalArgumentException("inputDir null");
        if (!inputDir.isDirectory())
            throw new IllegalArgumentException("inputDir is not a directory: " + inputDir.getAbsolutePath());
        
        this.inputDir = inputDir;
    }
    
   
    // The following data structure will be reused
    //to save the allocation for each execution
    private StringTokenizer st; 
    
    
    int degradableSleepTime = 0;
    private void processInputFile(File input) throws IOException
    {
        System.out.println("< Loading " + input.getAbsolutePath());
        
        BufferedReader in = null;        
        try
        {   in = new BufferedReader(new FileReader(input));            
            String line, name, traceId;            
            long tin, tout;
            int expId, eoi, ess;
            String vmname;
            String sessionid;
            
            while ((line = in.readLine()) != null)
            {
                try
                {               
                    st = new StringTokenizer(line, ";");
                    expId = Integer.parseInt(st.nextToken());
                    name = st.nextToken();
                    sessionid = st.nextToken();
                    traceId = st.nextToken();
                    tin = Long.parseLong(st.nextToken());
                    tout = Long.parseLong(st.nextToken());                    
                    vmname = st.nextToken();                    
                    // for distributed systems, there are two more columns:
                    eoi = st.hasMoreTokens() ? Integer.parseInt( st.nextToken() ) : -1;
                    ess = st.hasMoreTokens() ? Integer.parseInt( st.nextToken() ) : -1;
                    
                    // convert opname
                    int pos = name.lastIndexOf('.');
                    String componentName;
                    String methodName;                    
                    if (pos == -1) {
                        componentName = "";
                        methodName = name;
                    } else {
                        componentName = name.substring(0, pos-1);
                        methodName = name.substring(pos+1);
                    }
                    ctrl.setExperimentId(expId);
                    
                    if (degradableSleepTime > 0) Thread.sleep(degradableSleepTime*5);
                    
                    while (!ctrl.insertMonitoringDataNow(componentName, methodName, sessionid, traceId, tin, tout, eoi, ess)) {
                        Thread.sleep(500);
                        ctrl.enableMonitoring();
                        degradableSleepTime += 50;
                    }
                    if (degradableSleepTime > 0) degradableSleepTime--;
                    
                }
                
                catch (Exception e)
                {
                    System.err.println(
                        "Failed to parse line: {" + line + "} from file " +
                        input.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }
        finally
        {
            if (in != null) try { in.close(); } catch (Exception e) { /* ignore */ }
        }
    }
    
}
