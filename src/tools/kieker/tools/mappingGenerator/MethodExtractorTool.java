package kieker.tools.mappingGenerator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import kieker.tools.mappingGenerator.filters.composite.NoInterfaceNoSuperclassFilter;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This Program extracts all method signatures from a given input path in
 * classpath style. 
 * 
 * @author Robert von Massow
 * 
 */
public class MethodExtractorTool {

    private static final Log log = LogFactory.getLog(MethodExtractorTool.class);
    private static CommandLine cmdl = null;
    private static final CommandLineParser cmdlParser = new BasicParser();
    private static final HelpFormatter cmdHelpFormatter = new HelpFormatter();
    private static final Options cmdlOpts = new Options();
    private static String filter = null;
    private static String cp = null;
    private static String mappingFile = null;


    static {
        cmdlOpts.addOption(OptionBuilder.withArgName("classpath").hasArg().withLongOpt("searchpath").isRequired(true).withDescription("Classpath to analyze. Multiple classpath elements can be separated by '" + File.pathSeparator + "'.").withValueSeparator('=').create("c"));
        cmdlOpts.addOption(OptionBuilder.withArgName("classname").hasArg().withLongOpt("filter-classname").isRequired(false).withDescription("Classname of the filter to use.\n Defaults to " + NoInterfaceNoSuperclassFilter.class.getName()).withValueSeparator('=').create("f"));
        cmdlOpts.addOption(OptionBuilder.withArgName("filename").hasArg().withLongOpt("mapping-file").isRequired(true).withDescription("Name of mapping file to be written").withValueSeparator('=').create("m"));
    }

    private static boolean parseArgs(String[] args) {
        try {
            cmdl = cmdlParser.parse(cmdlOpts, args);
        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            printUsage();
            return false;
        }
        return true;
    }

    private static void printUsage() {
        cmdHelpFormatter.printHelp(MethodExtractorTool.class.getName(), cmdlOpts);
    }

    private static boolean initFromArgs() {
        cp = cmdl.getOptionValue("searchpath");
        filter = cmdl.getOptionValue("filter-classname", NoInterfaceNoSuperclassFilter.class.getName());
        mappingFile = cmdl.getOptionValue("mapping-file");
        return true;
    }

    public static void main(final String[] args) throws ClassNotFoundException {
        if (!parseArgs(args) || !initFromArgs()) {
            System.exit(1);
        }

        MethodExtractor extrInstance = new MethodExtractor(cp, filter, mappingFile);
        if (!extrInstance.execute()) {
            System.err.println("An error occured. See 'kieker.log' for details.");
            System.exit(1);
        }
    }
}

class MethodExtractor extends ClassLoader {

    private static final Log log = LogFactory.getLog(MethodExtractor.class);
    private String filtername = null;
    private String cp = null;
    private String mappingFile = null;
    private final NameIdMap map = new NameIdMap();

    public MethodExtractor(String cp, String filtername, String mappingFile) {
        this.cp = cp;
        this.filtername = filtername;
        this.mappingFile = mappingFile;
    }

    @SuppressWarnings("unchecked")
    public boolean execute() {
        boolean retval = false;
        try {
            String[] elem = cp.split(File.pathSeparator);
            Class<MethodFilter> filter = null;
            try {
                filter = (Class<MethodFilter>) loadClass(this.filtername);
            } catch (Exception e) {
                log.error("Unable to load filter or no filter provided (" + this.filtername + "), using default filter...");
                e.printStackTrace();
            }
            Vector<File> directories = new Vector<File>();
            Vector<File> jars = new Vector<File>();
            for (String string : elem) {
                File f = new File(string);
                if (f.isDirectory()) {
                    directories.add(f);
                } else if (f.getName().endsWith(".jar")) {
                    jars.add(f);
                }
            }
            analyzeDirectory(directories, filter);
            analyzeJars(jars, filter);
            this.map.writeMapToFile(mappingFile);
            retval = true;
        } catch (IOException ex) {
            log.error("IOException: ", ex);
            retval = false;
        }
        return retval;
    }

    private void analyzeJars(final Vector<File> jars,
            final Class<MethodFilter> filter) {
        MethodFilter f = null;
        if (filter != null) {
            try {
                f = filter.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (f == null) {
            f = new NullFilter();
        }
        for (File file : jars) {
            try {
                JarFile jar = new JarFile(file.getAbsoluteFile().getName());
                analyseJarFile(jar, f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void analyseJarFile(final JarFile jar, final MethodFilter filter) {
        Enumeration<JarEntry> es = jar.entries();
        while (es.hasMoreElements()) {
            JarEntry e = es.nextElement();
            log.error(e.getName());
            if (e.getName().endsWith(".class")) {
                analyzeJarClassEntry(jar, e, "", filter);
            }
        }
    }

    private void analyzeJarClassEntry(final JarFile jar, final JarEntry e,
            final String prefix, final MethodFilter filter) {
        byte[] data = null;
        long size = e.getSize();
        if (size == -1 || size > Integer.MAX_VALUE) {
            log.error("Size of file \"" + jar.getName() + "/" + e.getName() + " out of range: size");
            return;
        }
        data = new byte[(int) size];
        try {
            jar.getInputStream(e).read(data);
            String name = e.getName().substring(0, e.getName().length() - 6).replaceAll("/", ".");
            Class<?> clazz = null;
            try {
                clazz = defineClass(name, data, 0, data.length);
                analyzeClass(clazz, filter);
            } catch (LinkageError err) {
                log.error("Linkage error", err);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void analyzeDirectory(final Vector<File> dirs,
            final Class<MethodFilter> filter) {
        MethodFilter f = null;
        if (filter != null) {
            try {
                f = filter.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (f == null) {
                f = new NullFilter();
            }
        }
        for (File dir : dirs) {
            descendAndAnalyseDir(dir, "", f);
        }
    }

    /**
     * recursively descent the "classpath" and analyze the classes that a found
     * in it.
     *
     * @param dir
     *            current system directory
     * @param packagePrefix
     *            the packageprefix
     */
    private void descendAndAnalyseDir(final File dir,
            final String packagePrefix, final MethodFilter filter) {
        File[] classes = dir.listFiles(new FilenameFilter() {

            public boolean accept(final File dir, final String name) {
                return name.endsWith(".class");
            }
        });
        File[] descArray = dir.listFiles(new FilenameFilter() {

            public boolean accept(final File f, final String name) {
                return new File(f, name).isDirectory();
            }
        });
        analyseClasses(classes, packagePrefix, filter);
        classes = null;
        for (File file : descArray) {
            descendAndAnalyseDir(file, (packagePrefix + file.getName().replaceFirst(dir.getName(), "")).replaceAll(
                    File.separator, ".") + ".", filter);
        }
    }

    /**
     * extract all methods from the given classes
     *
     * @param classes
     *            the classes in the current package
     * @param packagePrefix
     *            the current package
     */
    private void analyseClasses(final File[] classes,
            final String packagePrefix, final MethodFilter filter) {
        for (File file : classes) {
            int begIndex = file.getName().lastIndexOf(File.separatorChar);
            begIndex = begIndex == -1 ? 0 : begIndex;
            int endIndex = file.getName().lastIndexOf('.');
//            log.info("analyzing " + packagePrefix + file.getName().substring(begIndex, endIndex));
            try {
                Class<?> c = super.loadClass(packagePrefix + file.getName().substring(begIndex, endIndex));
                analyzeClass(c, filter);
            } catch (ClassNotFoundException e) {
                log.error(packagePrefix + file.getName().substring(begIndex, endIndex), e);
                log.error("packagePrefix: " + packagePrefix);
                log.error("file.getName().substring(begIndex, endIndex): " + file.getName().substring(begIndex, endIndex));
            }
        }
    }

    private void analyzeClass(final Class<?> c, final MethodFilter filter) {
        Method[] m = c.getDeclaredMethods();
        for (Method method : m) {
            if (!filter.accept(method, c)) {
                continue;
            }
            int mod = method.getModifiers();
            String prefix = "";
            if (Modifier.isPublic(mod)) {
                prefix += "public ";
            }
            if (Modifier.isPrivate(mod)) {
                prefix += "private ";
            }
            if (Modifier.isProtected(mod)) {
                prefix += "protected ";
            }
            if (Modifier.isNative(mod)) {
                prefix += "native ";
            }
            if (Modifier.isAbstract(mod)) {
                prefix += "abstract ";
            }
            if (Modifier.isStatic(mod)) {
                prefix += "static ";
            }
            if (Modifier.isFinal(mod)) {
                prefix += "final ";
            }
            map.registerName(/*+ prefix*/method.getDeclaringClass().getName() + "." + method.getName() + "(" + concat(method.getParameterTypes()) + ")");
            method.getAnnotation(Override.class);
        }
    }

    /**
     * format a parameter list
     *
     * @param parameterTypes
     *            the method parameters
     * @return formated parameter list
     */
    private String concat(final Class<?>[] parameterTypes) {
        StringBuilder ret = new StringBuilder();
        for (Class<?> class1 : parameterTypes) {
            ret.append(class1.getCanonicalName() + ",");
        }
        if (ret.length() == 0) {
            return "";
        }
        return ret.delete(ret.length() - 1, ret.length()).toString();
    }
}

class NullFilter implements MethodFilter {

    public boolean accept(final Method m, final Class<?> c) {
        return true;
    }
}
