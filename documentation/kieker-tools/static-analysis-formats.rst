.. _kieker-tools-static-analysis-formats:

File Formats
============

The static analysis uses a set of input files to read call graph and data
flow traces. All CSV files use commas for value separation. In detail they
are:


**Note** These information might be partially outdated

Function call file
------------------

Containing functions calls.

**Note** old format specification
There are two supported formats with 3 and 4 column CSV files.
 - 3 columns: file, caller, callee: In this format the callee lacks the
   information where the operation is located. Thus, it must be inferred
   at a later point.

 - 4 columns: caller-file, caller, callee-file, callee

**Note** new format specification
 - 6 columns: path, module, caller, path, module, callee

Function call file hs comments
------------------------------

**Note** fparser is obsolete, we use now fxtran
The fparser-based tools already perform the lookup for the callee in the later
versions (this information is added in a post-processing step by a bash
script). The result is stored in a file typically called coupling-joined.csv, with
the four columns you describe above.

(The files produced by versions up until now also have a number as the fifth component, 
hs planned to implement counting here, but we do not look at this in the current
analysis, so hs just removed this from the scripts.)


Function lookup file
--------------------

The lookup file has 2 columns containing the operation name followed by
the filename, i.e., operation, file

Component map file
------------------

The tool allows to group operations not only by file, but also by module
or component. As the module information cannot be derived from the file
name, we use a module map file with the following format:
component name, file path, operation name

Dataflow file
-------------

Dataflow is stored in a file with 4 columns:
caller file, caller, direction, common block

Dataflow file hs comments
-------------------------

There is an additional column that specifies the index of the variable 
in the common block. We probably do not need this for the current paper, though.
The column names from the CSV are a bit different:

filename, function, readWrite, blockname, index

(I did not use "caller" since that looks too much like method call coupling, but
names of course don't really matter ;-))

The fparser-based tool stores this in a file called dataflow.cvs




