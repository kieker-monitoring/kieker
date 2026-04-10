/**
 * This module provides a Swing-based, wizard-style GUI for
 * configuring and launching Kieker trace analyses—letting
 * users select input logs, set filters and output options
 * (plots/reports), run the analysis, and manage/export
 * results as a front end to the command-line
 * trace-analysis tool.
 */
module kieker.tools.trace.analysis.gui {

    //Kieker Dependencies
    requires kieker.tools;
    requires kieker.tools.trace.analysis;

    //Java Dependencies
    requires java.desktop;

    //Third Party Dependencies
    requires org.slf4j;

}