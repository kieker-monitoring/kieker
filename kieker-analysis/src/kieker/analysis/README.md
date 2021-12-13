# Package Overview

Current layout
- configuration (deprecated)
- trace         Trace analysis and output
- display       graph output visualization
- util          various utilities
- model         handling models (temporary)
- graph         graph handling and output
- sink          generic sinks
- common        shared functionality
- debug         debug support
- exception     exceptions
- signature     handling of signatures (move)
- stage         stages/filter
- stage/select  selection filter
- stage/general generic stages
- stage/flow    stages for flow based analysis
- stage/events  processing events
- stage/forward 
- statistics    statistics

- plugin        Old style Kieker filters (non teetime)

- annotation
- source        Record/event source stage (producers)
- repository    Repository handling
- analysisComponent
- tt            Deprecated packages
- time          Time related filters

This layout has issues and should better be modularized into:
a) generic stuff (IO, selection, time, filters, etc)
b) trace related
c) deployment and instantiation
d) behavior
e) graph
f) display -> graph/display
h) model
i) repository
