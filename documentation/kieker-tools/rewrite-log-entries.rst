.. _kieker-tools-rewrite-log-entries:

Rewrite Log Entries
===================

This tool has 4 parameters and can be used to replace function pointer
references by function signatures produced by kieker-lang-pack-c
instrumentations.

===== ====================== ======== =============================================================
Short Long                   Required Description
===== ====================== ======== =============================================================
-i    --input                yes      input kieker log directory
-o    --output               yes      path where the output kieker log is placed
-a                           yes      the location of the addr2line executable to resolve the names
-m                           yes      the executable to be analyzed by addr2line
===== ====================== ======== =============================================================

The dar tool can process kieker-lang-pack-c files directly, but the
trace-analysis tools cannot resolve function names. In these cases
rewrite-log-entries can help to add human comprehensible function and
module/class/component descriptions.

