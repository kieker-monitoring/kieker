.. _kieker-tools-relabel:

Relabel Model Elements
======================

This tool reads in a model and replaces labels assigned to model elements.
It is possible to specify different operations. They are applied in sequence
from left to right. The tool searches for label sets specified as source
and replaces them by the set of labels specified in target.

===== ===================== ======== ======================================================
Short Long                  Required Description
===== ===================== ======== ======================================================
-i    --input               yes      Directory for the input model
-o    --output              yes      Directory for the modified model
-r    --replacements        yes      Replacement for labels source:target
-e    --experiment          no       Set experiment name of the model repository
===== ===================== ======== ======================================================

Examples
--------

Replace the two labels static and dynamic by veriant-5
```
relabel -i input-model -o modified-model -r static,dynamic:variant-5
```

Replace static by two labels and in a second step merge static-a with dynamic
into variant-4
```
relabel -i input-model -o modified-model -r static:static-a,static-b static-a,dynamic:variant-4
```

