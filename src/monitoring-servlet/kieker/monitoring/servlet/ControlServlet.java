/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 ***************************************************************************/

package kieker.monitoring.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Matthias Rohr, Andre van Hoorn
 * 
 *         History: 2008/09/04: Displays the global insert count 2008/09/02: Now
 *         it shows java.vm.name. Is Client JVM used on your server ? :)
 *         Additionally, it shows the garbage collectors in use. 2008/09/01:
 *         Added some features to create dummy monitoring events 2008/05/29:
 *         Changed vmid to vmname (defaults to hostname) -- the control-servlet
 *         can change the vmname during runtime 2008/01/14: Refactoring for the
 *         first release of Kieker and publication under an open source licence
 *         2007/03/13: Initial Prototype
 */
public class ControlServlet extends HttpServlet {
	private static final long serialVersionUID = 689701318L;

	private final static IMonitoringController CTRL_INST = MonitoringController.getInstance();
	private final static ITimeSource TIMESOURCE = ControlServlet.CTRL_INST.getTimeSource();

	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.getInstance();
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.getInstance();

	protected void dumpError(final PrintWriter out, final String msg) {
		out.println("<div style=\"color:red\">ERROR: " + msg + "</div>");
	}

	private void printHeader(final PrintWriter out) {
		out.println("<table border=\"0\" width=\"100%\"  bgcolor=\"#00478e\" cellspacing=\"3\" cellpadding=\"3\"  frame=\"void\"><tr><td align=\"left\" valign=\"top\"><a href=\"http://kieker.sourceforge.net\"><img src=\"images/kieker-logo-large.png\" alt=\"Kieker\" align=\"left\" border=\"0\"/></a></td></tr></table>");
	}

	private void printFooter(final PrintWriter out) {
		out.println("<table border=\"0\" width=\"100%\" cellspacing=\"2\" bgcolor=\"#00478e\" border=\"0\" cellpadding=\"2\"  frame=\"void\"> <tr><td align=\"left\"><td color=\"#ffffff\" style=\"font-size:80%\" valign=\"top\" width=\"70\">	 <br>	Powered by:<br>	<a href=\"http://sourceforge.net\"><img src=\"http://sflogo.sourceforge.net/sflogo.php?group_id=212691&amp;type=4\" width=\"125\" height=\"37\" border=\"0\" alt=\"SourceForge.net Logo\" /></a><br></td><td style=\"font-size:80%\" align=\"right\">	SourceForge, and SourceForge.net are registered trademarks of <br> SourceForge, Inc. in the United States and other countries.</td></tr></table>");
	}

	static String hostname = "unknown";
	private static boolean initialized = false;

	public static void initialize() {
		try {
			ControlServlet.hostname = java.net.InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
			ControlServlet.hostname = "unknown";
		}
		System.out.println("ControlServlet initializes.");
		ControlServlet.initialized = true;
	}

	/**
	 * Constructs a {@link ControlServlet}.
	 */
	public ControlServlet() {}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void processRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		if (!ControlServlet.initialized) {
			ControlServlet.initialize();
		}

		int experimentID = 0;
		final boolean connectorError = false;

		response.setContentType("text/html;charset=UTF-8");
		final PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Kieker's ControlServlet</title>");
		out.println("</head>");
		out.println("<body>");
		this.printHeader(out);
		out.println("<h2>ControlServlet</h2>");
		out.println("<br> Nanoseconds since midnight, January 1, 1970 UTC: " + ControlServlet.TIMESOURCE.getTime() + "<br>");
		out.println("Host:\"" + ControlServlet.hostname + "\"<br>");
		out.println("Vmname:\"" + ControlServlet.CTRL_INST.getHostName() + "\"<br>");

		String action = request.getParameter("action");
		if (action == null) {
			action = "";
		}

		if (!connectorError) {
			if (action.equals("setExperimentId")) { // NOCS (EqualsAvoidNullCheck)
				final String expimentIdString = request.getParameter("experimentID");
				if ((expimentIdString != null) && (expimentIdString.length() != 0)) {
					try {
						experimentID = Integer.parseInt(expimentIdString);
						if (experimentID >= 0) {
							ControlServlet.CTRL_INST.setExperimentId(experimentID);
						}

					} catch (final NumberFormatException ne) {
						this.dumpError(out, ne.getMessage());
					}
				}
				/*
				 * action = incExperimentId
				 */
			} else if (action.equals("incExperimentId")) { // NOCS (EqualsAvoidNullCheck)
				ControlServlet.CTRL_INST.incExperimentId();
				/*
				 * action = enable
				 */
			} else if (action.equals("enable")) { // NOCS (EqualsAvoidNullCheck)
				ControlServlet.CTRL_INST.enableMonitoring();
				/*
				 * action = disable
				 */
			} else if (action.equals("disable")) { // NOCS (EqualsAvoidNullCheck)
				ControlServlet.CTRL_INST.disableMonitoring();
				/*
				 * action = terminate
				 */
			} else if (action.equals("terminate")) { // NOCS (EqualsAvoidNullCheck)
				ControlServlet.CTRL_INST.terminateMonitoring();
				/*
				 * action = ...
				 */
			} else if (action.equals("insertTestData")) { // NOCS (EqualsAvoidNullCheck)
				ControlServlet.SESSION_REGISTRY.storeThreadLocalSessionId(request.getSession(true).getId());
				ControlServlet.CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
				for (int i = 0; i < 12; i++) { // NOCS
					ControlServlet.CTRL_INST.newMonitoringRecord(new OperationExecutionRecord("kieker.monitoring.controlServlet.ControlServlet",
							"processRequest(HttpServletRequest,HttpServletResponse)", ControlServlet.SESSION_REGISTRY.recallThreadLocalSessionId(),
							ControlServlet.CF_REGISTRY.recallThreadLocalTraceId(), ControlServlet.TIMESOURCE.getTime(), ControlServlet.TIMESOURCE.getTime(),
							ControlServlet.CTRL_INST.getHostName(), i, i));
				}
				ControlServlet.CF_REGISTRY.unsetThreadLocalTraceId();
				ControlServlet.SESSION_REGISTRY.unsetThreadLocalSessionId();
				/*
				 * invalid action
				 */
			} else if (!(action.length() == 0)) {
				this.dumpError(out, "Invalid action: '" + action + "'");
			}
		}

		/*
		 * Dump Connector Info
		 */
		out.println("<h3> Status (" + "<a href=\"index.html\"> update </a>" + ")  </h3>");
		String monitoringControllerStatus = "";
		try {
			monitoringControllerStatus = ControlServlet.CTRL_INST.toString();
		} catch (final Exception e) { // NOCS (IllegalCatchCheck)
			out.println(e.getMessage());
		}

		if (monitoringControllerStatus.length() == 0) {
			out.println("<h2> Dbconnector not found ... </h2>");
		} else {
			out.println("<table>");
			out.print("<tr><td><pre>");
			out.print(monitoringControllerStatus);
			out.println("</pre></td></tr>");
			out.println("</table>");
		}
		final StringBuffer bu = new StringBuffer();
		bu.append("<br><h3>Options:</h3>");
		bu.append(" enabled: <a href=\"index?action=enable\"> enable </a> / <a href=\"index?action=disable\"> disable </a> / <a href=\"index?action=terminate\"> terminate</a><br>");
		bu.append(" <FORM ACTION=\"index\" METHOD=\"GET\"> ");
		bu.append(" experimentID: <a href=\"index?action=incExperimentId\"> increment </a> <br>");
		bu.append("<INPUT TYPE=\"HIDDEN\" NAME=\"action\" VALUE=\"setExperimentId\">");
		bu.append(" experimentID (int): <INPUT TYPE=\"TEXT\" SIZE=\"6\" NAME=\"experimentID\" value=\"" + ControlServlet.CTRL_INST.getExperimentId() + "\"/>");
		bu.append(" <INPUT TYPE=\"SUBMIT\" VALUE=\"change\"> ");
		bu.append("</FORM> <br><br>");
		bu.append(" <FORM ACTION=\"index\" METHOD=\"GET\"> ");
		bu.append(" <INPUT TYPE=\"HIDDEN\" NAME=\"action\" VALUE=\"setVmname\">");
		bu.append(" vmname (max 40 char): <INPUT TYPE=\"TEXT\" SIZE=\"40\" NAME=\"vmname\" value=\"" + ControlServlet.CTRL_INST.getHostName() + "\"/>");
		bu.append(" <INPUT TYPE=\"SUBMIT\" VALUE=\"change\"> <br> <br>");
		bu.append(" Create 12 fake entries into the log (operation kieker.monitoring.controlServlet..): <a href=\"index?action=insertTestData\"> generate </a> <br><br>");
		bu.append(" Kieker monitoring events since last execution environment restart = " + ControlServlet.CTRL_INST.getNumberOfInserts() + " <br>");
		bu.append(" java.vm.name = " + System.getProperty("java.vm.name") + " <br>");
		try {
			final String youngGC = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans().get(0).getName();
			final String tenureGC = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans().get(1).getName();
			bu.append(" Garbage collectors : " + youngGC + " , " + tenureGC + "<br>");
		} catch (final RuntimeException e) { // ignore // NOCS (IllegalCatchCheck)
		}
		out.println(bu.toString());
		this.printFooter(out);
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		if (!ControlServlet.initialized) {
			ControlServlet.initialize();
		}
		this.processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		if (!ControlServlet.initialized) {
			ControlServlet.initialize();
		}
		this.processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	@Override
	public String getServletInfo() {
		return "Kieker ControlServlet";
	}
}
