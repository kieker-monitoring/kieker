.. _ported-trace-analysis:

Trace Analysis based on the Ported Set of Filters
=================================================

This analysis configuration is derived from the deprecated analysis
configuration implemmented in the old Kieker pipe and filter framework.

**Disclaimer** It might not support all visualization features of the deprecated
analysis tool, but all known test cases produce the desired output.

The image below shows the complete architecture based on the
TeeTime pipe and stage graphical notation. The legend is shown in the
upper right corner.

.. image:: ported-trace-analysis.svg

In detail the figure shows:

- **LogsReaderCompositeStage** is used to read one or more log directories of Kieker logs.
- **ThreadEvent2TraceEventStage** translates ``BeforeThreadBasedEvent``,
  ``AfterThreadBasedEvent`` and ``AfterFailedThreadBasedEvent`` into ``BeforeOperationEvent``,
  ``AfterOperationEvent`` and ``AfterOperationFailedEvent``, respectively. All other events pass
  unchanged.
- **TimestampFilter** allows only to pass events within a specified time range.
- **TraceIdFilter** allows only to pass a certain set of trace-ids. The stage can be set to allow
  all events through. It can work as a filter, removing a specified set of traces from the stream or
  as a selector to choose only a specific set of traces.
- **DynamicEventDispatcher** separates ``OperationExecutionRecord`` events and ``IFlowRecord``
  events so that they can be handled by specific filters. ``IFlowRecord`` events comprise a wide
  set of different events including ``BeforeOperationEvent``, ``AfterOperationEvent`` and
  ``AfterOperationFailedEvent``.
- **ExecutionRecordTransmissionStage** and **TraceReconstructionStage** together generate
  ``ExecutionTrace``, ``InvalidExecutionTrace`` and ``MessageTrace`` events based on collected
  traces. Both stages also help to create a system model of the monitored system.
- **ExecutionRecordTransformationStage** performs an comparable task based on ``IFlowRecord`` events.
- The following three **Merger** stages are used to collect all ``*Trace`` events regardless how they
  are generated. Subsequently, they are distributed to a wide range of output filters and stages.
- All the filters in the gray boxes are connected to the message or execution trace sources, as well as,
  the **System Model**. To avoid a myrad of links, we omitted the single connections. Instead the
  respective box is linked to the pipe arrow. In implementation all free import ports in a box are
  linked to the repsective distirbutor.

