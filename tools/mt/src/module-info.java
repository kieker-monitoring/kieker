/**
 * This module manipulates tabular datasets (primarily CSV) from
 * Kieker analyses—supporting merges/joins, filtering, sorting,
 * column derivations/renaming, text substitutions, normalization,
 * and exporting the transformed tables for downstream reporting.
 */
module kieker.tools.mt {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.tools;

    //Third-Party Dependencies
    requires com.github.hazendaz.parent;
    requires org.apache.commons.text;
    requires org.slf4j;
}