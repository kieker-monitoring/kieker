# Package Overview

New layout:
- architecture architecture related classes
  - trace architecture trace analysis
  - recovery architecture recovery stages
  - metrics architecture evaluation stages
  - repository architecture model repository stages
  - adaptation adaptation of an architecture
- behavior user behavior stages
- generic contains generically usable stages
- util utility classes/stages
  - time time utilities

The general idea is that stages and other code are grouped by
domain, e.g., architecture, performance and behavior.

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
