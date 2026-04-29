/**
 * This module generates formatted tables from Kieker analysis
 * or experiment outputs — aggregating, filtering, and formatting
 * metrics into report - or publication-ready CSV/Markdown/LaTeX tables.
 */
module kieker.tools.mktable {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.tools;

    //Third-Party Dependencies
    requires org.slf4j;
}