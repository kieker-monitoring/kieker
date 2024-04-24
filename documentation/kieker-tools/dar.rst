.. _kieker-tools-dar:

Dynamic Architecture Analysis
=============================

Read a Kieker log and generate an architecture model from it.

===== ===================== ======== ======================================================
Short Long                  Required Description
===== ===================== ======== ======================================================
-i    --input               yes      Input Kieker log directory location
-o    --output              yes      Output directory to store the recovered model
-M    --component-maps               Component, file and function map files
-a    --addrline                     Location of the addrline tool (necessary for Kieker4C)
-e    --executable                   Location of the executable
-l    --source-label        yes      Set source label for the read data
-c    --case-insensitive             Handle function names case insensitive
-E    --experiment-name     yes      Name of the experiment
-s    --signature-extractor yes      Type of extractor used for component and operation 
                                     signatures (elf, python, java)
-k    --keep-metadata                Keep the metadata info in memory regardless a trace
                                     is considered complete
-ms   --map-file-separator           Separator character for the mapping file
-m    --module-modes        yes      Module converter strategies: file-mode, map-mode,
                                     module-mode, java-class-mode, java-class-long-mode
===== ===================== ======== ======================================================

Examples
--------

```
dar -i kieker-log -o model-directory -l dynamic -E demo-project -s java -m module-mode
```

Module Modes
------------

Currently **dar** supports five different modes to modularize the architecture.
Each module is seen as a component of the architecture.
- **file-mode** all functions within a file are put in the same component
- **map-mode**  a separate map file sorts functions into a component
- **module-mode** Fortran module definitions are used to place a function into a component
- **java-class-mode** The simple class name is used for component names and to group functions/methods
- **java-class-long-mode** The full qualified name of classes is used for component names and to group functions/methods
  
In principle, it is possible to specify multiple modes. This is helpful when
for example not all parts of a program use, e.g., Fortran modules, then the
functions outside of modules can be grouped based on another strategy.

Component map file
------------------

The tool allows to group operations not only by file, but also by module
or component. As the module is not present with all kind of signatures, e.g.,
in ELF executable, information cannot be derived from the file name, we use a
module map file with the following format:

component name, file path, operation name

 
Using Addrline
--------------
 
Addrline is a command line tool under Linux and other Unices which is able to
exctract ELF information on functions in ELF based executable. That are all
native executable under Linux and similar operating systems. It might work with
other binaries as well.
 
To be able to work the executable must be linked with debugging symbols, i.e.,
with gcc this can be achieved with the option -g.

Executable
----------

When analyzing Kieker logs from Kieker4C the log only contains function
pointer references. This is suboptimal to understand what is going on.
Therefore, you can extract names and other information on function with dar
utilizing addrline automatically.

The executable must be compiled with -g to contain debugging symbols.

Special Trace Cases
-------------------

Usually a trace is a sequenceo of before and after operation events. Each
before operation increases the stack and every after operation decreases the
call stack. Thus, when the call stack is empty again, the **dar** tool removes
the trace metadata from memory, as the trace is considered complete. However,
in some contexts this is not the case and the trace continues afterwards.
Therefore, you can use the option -k. This will keep the trace metadata.
Regardless the trace seems to be complete. In case you have many small traces
this migh lead to a memory leak, as all trace metadata is kept until termination
of the tool.
 
