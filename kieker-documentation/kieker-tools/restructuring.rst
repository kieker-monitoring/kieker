.. _kieker-tools-restructuring:

Restructuring
=============

Identify a number of operations to transform an architecture model
into another one. Best strategy to use is kuhn

===== ====================== ======== ======================================================
Short Long                   Required Description
===== ====================== ======== ======================================================
-i    --input                yes      Input architecture model directories
-o    --output               yes      Output architecture model directory
      --eol                  no       End of line symbol
-e    --experiment           no       Experiment name
-s    --strategy             yes      Strategy identifier
                                      Possible Values: [NORMAL, EMPTY, RANDOM, KUHN]
===== ====================== ======== ======================================================
