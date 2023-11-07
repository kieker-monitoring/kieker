# Overview

This package is a java side implementation of architecture discovery of dataflow analysis of Earth System Models (ESM).
In the global package settings as well as creation of a TeeTime-Config is added and called by arg:

``-bsc-j`` or ``--bsc-dataflow-input``

To successfully read and convert test data please use CSV files with line inputs like:

``filename;block;read/write;target``

e.g.:
``componentX;xsub1;READ;ysub1``

and

``filename;blockname;type``

e.g.:
``componentX;xcom1;COMMON;``
``componentX;xsub2;SUBROUTINE;``
``componentX;ximportComp;IMPORT;``
``componentY;ysub1;SUBROUTINE;``
``componentY;ysub2;"SUBROUTINE;``

If you want to start a full dataflow analysis of an ESM please start an analysis run of the tool
[ESM Dataflow Analysis] (https://cau-git.rz.uni-kiel.de/ifi-ag-se/oceandsl/esm-dataflow-analysis)
Afterwards use the oceandsl-sar tool to handle the provided data.

Command:
```-bsc-j ".\\dataflow.csv" -bsc-c ".\\contents.csv" -o "./output/" -E "testrun A" -l "ich bin ein Label"```

# Visualization:
For Visualization please use the Plugin Extension of Kieker for Eclipse. With: Window -> Show View -> Other -> Kieker -> Diagram
you are able to open imported XMI files.
Please make sure to have the latest Release of the kieker model, containing Model: OperationAccess and the referenced Mapping Model.

# Design

In order to fit in the given structure of oceandsl-sar, the bsc.dataflow uses the TeeTimeFramework to fulfill
data transformation. The entry point is a separate configuration class, connected to the main class of oceandsl-sar.
With entry flags defined in Settings.java it can be accessed with two flags for a successful reconstruction.

1)  -bsc-j "dataflow.csv"   -- generated file containing all dataflow found in an ESM
2)  -bsc-c "component.csv"  -- generated file containing all subroutines and common blocks

(...)

## Stages

Further Docu is written in JavaDoc comments in the class implementation.

### TeeTimeBscDataflowConfiguration.java

This class starts the data extraction by calling multiple defined Stages and connecting them via TeeTime-Framework ports.
In the beginning of the constructor a lookupTable for subroutines,functions and common blocks is created, which will be used later on
to configure target components. Secondly the first stage is created: "CSVBscDataflowReaderStage". This stage is used to
retrieve dataflow results provided in the runtime arguments. It is stored in a "DataTransferObject"(DTO) to pass upcoming Stages.
In the following stage named "PreConfigurationStage" a passed DTO is added by
the target component of the dataflow stored.
The main part of the dataflow configuration are four stages initialized to fill a type-, assembly-, deployment- and execution-model with data.
All four come from the Kieker Architecture Model, extended with a new Class 'OperationAccess' together with a needed mapping class in the execution-model.
After a successful run sar creates six XMI models in the provided output folder.

### CSVBscDataflowReaderStage

This stage retrieves data from provided csv files and sends the read in data in a DTO Object to its connected stage.

### PreConfigurationStage

This stage searches for every provided traget identifier the component it is contained in. For common blocks it sets one global component called 'common component'.
In the event of an unsuccessful lookup an unknown flag is set to avoid storing errors in the configured models.

### TypeModelStage

This stage stores new source and target ComponentTypes in the type-model. Additionally it seperates between storage access and operation access. For every storage access
a new storage is created and stored in a global 'common-component'. However every operation call is stored in its referenced target component.

### AssemblyModelStage

This stage stores new source and target AssemblyComponents in the assembly-model. Additionally it seperates between storage access and operation access. For every storage access
a new storage is created and stored in a global 'common-component'. However every operation call is stored in its referenced target component.

### DeploymentModelStage

This stage stores new source and target DeploymentComponents in the assembly-model. Additionally it seperates between storage access and operation access. For every storage access
a new storage is created and stored in a global 'common-component'. However every operation call is stored in its referenced target component.

### ExecutionModelStage

This stage configures the passed dataflow to store it in the execution-model. Therefore it creates StorageAccess objects referencing dataflow to/from a common block and 
OperationAccess object referencing dataflow to/from subroutines/functions. Here the last unused attribute of the DTO is stored: RWAccess. All together represents the main results
of this Dataflow Architecture Recovery.

## Model

Further information: (...)

### ComponentLookup

A class to store or retrieve component content referenced by a string identifier.

### ComponentStoreObject

A class to store component information.

### DataTransferObject

A class to store dataflow data of on step. It is used to retrieve and store information for model creation.
