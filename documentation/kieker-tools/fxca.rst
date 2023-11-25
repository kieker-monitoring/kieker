.. _kieker-tools-fxca:

FXTran Code Analysis
====================

Fxtran is a Fortran parser which outputs an XML based AST.
fxca processes this AST and outputs CSV files for calls, dataflows,
components and data storages.

===== ====================== ======== ======================================================
Short Long                   Required Description
===== ====================== ======== ======================================================
-d    --default-component    no       In case callees are identified that do not have an
                                      implementation in the code, assign the callee to this
                                      operation.
      --eol                  no       End of line symbol, Default is system dependent
-f    --flat                 no       Scan source directories flat, i.e. not in recusrive
                                      mode, default is scan recursively
-i    --input                yes      One or more paths to fxtran-generated XML files.
-l    --library-functions    no       Map files for built-in and other runtime functions.
-o    --output               yes      Path where the output files are placed.
===== ====================== ======== ======================================================

