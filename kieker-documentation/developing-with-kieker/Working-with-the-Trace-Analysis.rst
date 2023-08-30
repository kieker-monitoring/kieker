.. _working-with-the-trace-analysis:

Working with the Trace Analysis
===============================

In the current version of Kieker there exists three implementation
of a trace analysis in the code. One based on the old pipe and
filter framework, one ported version utilizing the new pipe and
filter framework, and a new implementation with a clearer architecture.

As we do not want to encourage using the old pipe and filter framework --
it is ugly and complicated to use anyway -- we only describe the
ported version and the new implementation.

In this documentation, we will provide an architectural overview with
all stages and their output mentioned. This allows you to select
those parts you are interest in and skip the rest or use a provided
predefined assembly.

- :ref:`new-trace-analysis`
- :ref:`ported-trace-analysis`


