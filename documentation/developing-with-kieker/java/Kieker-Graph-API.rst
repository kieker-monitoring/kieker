.. _developing-with-kieker-java-kieker-graph-api:

Kieker Graph API
================

Many algorithms work on graphs in computer science. This includes user behavior observation, code analysis, architectue analysis and many more. There are different graph frameworks available, one is the Google graph framework which is part of `Guava <https://guava.dev/>`_.

Kieker relies on the Google Graph Library (GGL) which is also used by other developers making the integration easier.

Graph Classes and Interfaces
----------------------------

Kieker uses MutableNetwork and ImmutableNetwork for its graphs. This is necessary, as we use annotations to nodes and edges. GGL allows to use any class as node and edge. However, to be able to implement algorithms, we require to implement the ``INode`` and ``IEdge`` interfaces for nodes and edges, respectively.

As GGL does not support annotations to the graph itself, we created the ``IGraph`` interface to allow to set properties and a label to the graph. This also allows to nest graphs.

For these three interfaces, Kieker also provides a generic implementation in ``kieker.analysis.generic.graph.impl``. The corresponding factory is implemented in ``kieker.analysis.generic.graph.GraphFactory``.

Graph Flattening
----------------

Allows to flatten nested graphs.


Graph Clustering
----------------

The graph clustering is based on the graph edit distance, implemented in ``GraphEditDistance``, which can compute the edit distance between flat graphs using a cost function. The ``BasicCostFunction`` only assigns certain values for node and edge insertion. However, for the behavior graph clustering, the cost function can be replaced by a specific implementation that also honors parameters.

We borrowed an MTree implementation by **Eduardo R. D'Avila**.

The main stage for clustering is the ``ClusteringCompositeStage`` in ``kieker.analysis.generic.graph.clustering``. The input port requires an ``OpticsData`` containing the ``MutableNetwork`` of a graph and the ``OPTICSDataGED`` instance which provides the ``GraphEditDistance`` calculator.


Graph Traverser
---------------

Allow to travers the graph including flat graphs and nested graphs.