.. _kieker-tools-mt:

Manipulate Table
================

This tools can be used to process MoveOperationEntry based tables which
have a source-component-name, target-component-name, and operation-name
column. It can sort and cluster table entries. It is possible to use
both together. However, the sorter does not honor the changes by the 
clusterer. Thus, applying both may lead to unwanted results. The tool
first clusteres entries by their operation name and then sorts them.


===== ===================== ======== ======================================================
Short Long                  Required Description
===== ===================== ======== ======================================================
-i    --input               yes      Input table path
-o    --output              yes      Output table path
-s    --sort                         Sort column by the specified criteria. The argument
                                     accepts a list of column names separated by commas.
                                     Each column name must be suffixed by a : and the sort
                                     direction, .i.e, ascending or descending
-p    --min-ptr                      Clustering min ptr value for the optics clusterer (default 1)
-d    --clustering-distance          Minimum clustering distance for name clustering
===== ===================== ======== ======================================================

Example sort
```
mt -i input.csv -o output.csv -s operation-name:ascending,target-component-name:descending
```

Example cluster
```
mt -i input.csv -o output.csv -d 0.11
```



