package kieker.tpmonControlServlet;

import java.io.*;
import java.util.StringTokenizer;
import kieker.tpmon.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * kieker.tpmonControlServlet.ControlServlet.java;
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 *
 * @author Matthias Rohr, Andre van Hoorn
 *
 * History:
 * 2008/05/29: Changed vmid to vmname (defaults to hostname) -- 
 *             the tpmon-control-servlet can change the vmname during runtime
 * 2008/01/14: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007/03/13: Initial Prototype
 */
public class ControlServlet extends HttpServlet {

    protected void dumpError(PrintWriter out, String msg) {
        out.println("<div style=\"color:red\">ERROR: " + msg + "</div>");
    }

    private void printHeader(PrintWriter out) {
        out.println("<table border=\"0\" width=\"100%\"  bgcolor=\"#00478e\" cellspacing=\"3\" cellpadding=\"3\"  frame=\"void\"><tr><td align=\"left\" valign=\"top\"><a href=\"http://kieker.sourceforge.net\"><img src=\"images/kieker-logo-large.png\" alt=\"Kieker\" align=\"left\" border=\"0\"/></a></td></tr></table>");
    }

    private void printFooter(PrintWriter out) {
        out.println("<table border=\"0\" width=\"100%\" cellspacing=\"2\" bgcolor=\"#00478e\" border=\"0\" cellpadding=\"2\"  frame=\"void\"> <tr><td align=\"left\"><td color=\"#ffffff\" style=\"font-size:80%\" valign=\"top\" width=\"70\">	 <br>	Powered by:<br>	<a href=\"http://sourceforge.net\"><img src=\"http://sflogo.sourceforge.net/sflogo.php?group_id=212691&amp;type=4\" width=\"125\" height=\"37\" border=\"0\" alt=\"SourceForge.net Logo\" /></a><br></td><td style=\"font-size:80%\" align=\"right\">	SourceForge, and SourceForge.net are registered trademarks of <br> SourceForge, Inc. in the United States and other countries.</td></tr></table>");
    }
    static String hostname = "unknown";
    private static boolean initialized = false;

    public static void initialize() {
        try {
            hostname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
        }
        System.out.println("TpmonControlServlet initializes.");
        initialized = true;
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!initialized) {
            initialize();
        }

        TpmonController ctrlInst = TpmonController.getInstance();
        
        int experimentID = 0;
        boolean connectorError = false;

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Kieker's tpmonControlServlet</title>");
        out.println("</head>");
        out.println("<body>");
        printHeader(out);
        out.println("<h2>TpmonControlServlet</h2>");
        try {
            long time = ctrlInst.getTime();
            out.println("<br> Nanoseconds since midnight, January 1, 1970 UTC: " + time + "<br>");
            out.println("Host:\"" + hostname + "\"<br>");
            out.println("Vmname:\"" + ctrlInst.getVmname() + "\"<br>");
        } catch (Exception e) {
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = new String("");
        }

        /*
         * action = setDebug
         */
        if (!connectorError) {
            if (action.equals("setDebug")) {
                if (request.getParameter("debug") != null && request.getParameter("debug").equals("on")) {
                    ctrlInst.setDebug(true);
                } else if (request.getParameter("debug") != null && request.getParameter("debug").equals("off")) {
                    ctrlInst.setDebug(false);
                } else {
                    dumpError(out, "Invalid or missing value for parameter 'debug'");
                }
            /*
             * action = setExperimentId
             */
            } else if (action.equals("setExperimentId")) {
                String expimentIdString = request.getParameter("experimentID");
                if (expimentIdString != null && expimentIdString.length() != 0) {
                    try {
                        experimentID = Integer.parseInt(expimentIdString);
                        if (experimentID >= 0) {
                            ctrlInst.setExperimentId(experimentID);
                        }

                    } catch (NumberFormatException ne) {
                        dumpError(out, ne.getMessage());
                    }
                }

            } else if (action.equals("setVmname")) {
                String vmname = request.getParameter("vmname");
                if (vmname != null) {
                    ctrlInst.setVmname(vmname);
                }
            /*
             * action = incExperimentId
             */
            } else if (action.equals("incExperimentId")) {
                experimentID = ctrlInst.incExperimentId();
            /*
             * action = enable
             */
            } else if (action.equals("enable")) {
                ctrlInst.enableMonitoring();
            /*
             * action = disable
             */
            } else if (action.equals("disable")) {
                ctrlInst.disableMonitoring();
            /*
             * invalid action
             */
            } else if (action.equals("insertTestData")) {
                for (int i = 0; i < 12; i++) {
                    ctrlInst.insertMonitoringDataNow("kieker.tpmonControlServlet.TpmonControlServlet","TpmonControlServlet.processRequest(HttpServletRequest,HttpServletResponse)", request.getSession().getId(), ctrlInst.getTraceId(Thread.currentThread().getId()), ctrlInst.getTime(), ctrlInst.getTime(), i, i);
                }
            /*
             * action = disable
             */
            } else if (!(action.length() == 0)) {
                dumpError(out, "Invalid action: '" + action + "'");
            }
        }

        /*
         * Dump Connector Info
         */
        out.println(
                "<h3> Status (" +
                "<a href=\"index.html\"> update </a>" +
                ")  </h3>");
        String dbconnectorInfo = "";
        try {
            dbconnectorInfo = ctrlInst.getConnectorInfo();
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        if (dbconnectorInfo.length() == 0) {
            out.println("<h2> Dbconnector not found ... </h2>");
        } else {
            StringTokenizer infoTokenizer = new StringTokenizer(dbconnectorInfo, ",");
            out.println("<table>");
            while (infoTokenizer.hasMoreTokens()) {
                out.print("<tr>");
                String nameValuePair = infoTokenizer.nextToken();
                StringTokenizer fieldTokenizer = new StringTokenizer(nameValuePair, ":");
                if (fieldTokenizer.countTokens() < 2) {
                    out.print("<td colspan=2>");
                    dumpError(out, "Invalid name-value pair:" + nameValuePair);
                    out.print("</td>");
                } else {
                    out.print("<td>" + fieldTokenizer.nextToken().trim() + ":</td>");
                    out.print("<td>");
                    while (fieldTokenizer.hasMoreTokens()) {
                        out.print(fieldTokenizer.nextToken().trim());
                    }
                    out.print("</td>");
                }
                out.println("</tr>");
            }
            out.println("</table>");
        }
        StringBuffer bu = new StringBuffer();
        bu.append("<br><h3>Options:</h3>");
        bu.append(" enabled: <a href=\"index?action=enable\"> enable </a> / <a href=\"index?action=disable\"> disable </a> <br>");
        bu.append(" debug: <a href=\"index?action=setDebug&debug=on\"> on </a> / <a href=\"index?action=setDebug&debug=off\"> off </a> <br>");
        bu.append(" <FORM ACTION=\"index\" METHOD=\"GET\"> ");
        bu.append(" experimentID: <a href=\"index?action=incExperimentId\"> increment </a> <br>");
        bu.append("<INPUT TYPE=\"HIDDEN\" NAME=\"action\" VALUE=\"setExperimentId\">");
        bu.append(" experimentID (int): <INPUT TYPE=\"TEXT\" SIZE=\"6\" NAME=\"experimentID\" value=\"" + ctrlInst.getExperimentId() + "\"/>");
        bu.append(" <INPUT TYPE=\"SUBMIT\" VALUE=\"change\"> ");
        bu.append("</FORM> <br><br>");
        bu.append(" <FORM ACTION=\"index\" METHOD=\"GET\"> ");
        bu.append(" <INPUT TYPE=\"HIDDEN\" NAME=\"action\" VALUE=\"setVmname\">");
        bu.append(" vmname (max 40 char): <INPUT TYPE=\"TEXT\" SIZE=\"40\" NAME=\"vmname\" value=\"" + ctrlInst.getVmname() + "\"/>");
        bu.append(" <INPUT TYPE=\"SUBMIT\" VALUE=\"change\"> <br> <br>");
        bu.append(" Create 12 fake entries into the log (operation kieker.tpmonControlServlet..): <a href=\"index?action=insertTestData\"> generate </a> <br><br>");
		bu.append(" java.vm.name = "+System.getProperty("java.vm.name")+" <br>");
		try {
		String youngGC  = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans().get(0).getName();
		String tenureGC = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans().get(1).getName();
		bu.append(" Garbage collectors : "+youngGC+" , "+tenureGC+"<br>");
		} catch(Exception e){} // nothing we can do
        out.println(bu.toString());

        printFooter(out);
        out.println("</body>");
        out.println("</html>");

        out.close();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!initialized) {
            initialize();
        }
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!initialized) {
            initialize();
        }
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "TpmonControlServlet";
    }
}
