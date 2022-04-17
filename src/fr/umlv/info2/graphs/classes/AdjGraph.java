package fr.umlv.info2.graphs.classes;

import fr.umlv.info2.graphs.utils.Edge;
import fr.umlv.info2.graphs.utils.Graph;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class AdjGraph implements Graph {
    private final ArrayList<LinkedList<Edge>> adj;
    private final int n; // number of vertices
    private int m; // number of edges;

    public AdjGraph(int n) {
        this.n = n;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new LinkedList<Edge>());
        }
    }

    @Override
    public int numberOfEdges() {
        return m;
    }

    @Override
    public int numberOfVertices() {
        return n;
    }

    @Override
    public void addEdge(int i, int j, int value) {
        var linkedList = adj.get(i);
        var newEdge = new Edge(i, j, value);
        linkedList.add(newEdge);
        m++;
    }

    @Override
    public boolean isEdge(int i, int j) {
        return adj.get(i).stream().anyMatch(edge -> edge.getEnd() == j);
    }

    @Override
    public int getWeight(int i, int j) {
        var edge = adj.get(i).stream().filter(e -> e.getEnd() == j).findFirst();
        if (edge.isEmpty()) {
            throw new IllegalArgumentException("Tried to get the weight of a non added edge");
        }
        return edge.get().getValue();
    }

    @Override
    public Iterator<Edge> edgeIterator(int i) {
        return adj.get(i).iterator();
    }

    @Override
    public void forEachEdge(int i, Consumer<Edge> consumer) {
        adj.get(i).forEach(consumer);
    }


    @Override
    public String toGraphviz() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("digraph G {").append("\n");
        IntStream.range(0, adj.size()).forEach(i -> {
            stringBuilder.append(i).append(";").append("\n");
            forEachEdge(i, edge -> stringBuilder.append(i).append(" -> ").
                    append(edge.getEnd()).append(" [ ").append("label=\"").
                    append(edge.getValue()).append("\"").append(" ]").
                    append(" ;").append("\n"));
        });
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public AdjGraph genRandGraph(int v, int e) {
        if(v > (e*e)) {
            throw new AssertionError("nope");
        }
        var g = new AdjGraph(v);
        for (int i = 0; i < e; i++) {
            var start = getRandomNumberUsingNextInt(0, v);
            var end = getRandomNumberUsingNextInt(0, v);
            g.addEdge(start, end, 1);
        }
        return g;
    }

    public int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }


    @Override
    public Graph transpose() {
        var newGraph = new AdjGraph(n);

        for(int i = 0; i < n; i++){
            this.forEachEdge(i, edge -> newGraph.addEdge(edge.getEnd(), edge.getStart(), edge.getValue()));
        }

        return newGraph;
    }

    private boolean adjEquals(AdjGraph graph, AdjGraph other){
        var arrayEquals = graph.adj.equals(other.adj);
        if(!arrayEquals){
            return false;
        }
        for(int i = 0; i < n; i++){
            if(graph.adj.get(i).equals(other.adj.get(i))){
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AdjGraph adjGraph && n == adjGraph.n && m == adjGraph.m && this.adjEquals(this, adjGraph);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adj, n, m);
    }
}