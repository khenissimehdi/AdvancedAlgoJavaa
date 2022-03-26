package fr.umlv.info2.graphs.classes;

import fr.umlv.info2.graphs.utils.Edge;
import fr.umlv.info2.graphs.utils.Graph;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class AdjGraph implements Graph {
private int numberOdEdges ;
private final ArrayList<LinkedList<Edge>> adj ;
private final int n; // number of vertices

    public AdjGraph(int n) {
        this.n = n;
        this.adj = new ArrayList<>();
       IntStream.range(0, n).forEach(i -> {
            adj.add(i, new LinkedList<>());
           for (int j = 0; j < n; j++) {
               adj.get(i).add(new Edge(i, j,0));
           }
        });
    }

    @Override
    public int numberOfEdges() {
        return numberOdEdges;
    }

    @Override
    public int numberOfVertices() {
        return n;
    }

    @Override
    public void addEdge(int i, int j, int value) {
        if(checkIndex(i,j))
            throw new IndexOutOfBoundsException();
        adj.get(i).set(j, new Edge(i, j, value));
        numberOdEdges++;
    }

    private boolean checkIndex(int i, int j ) {
        return  i > n || j > n;
    }


    @Override
    public boolean isEdge(int i, int j) {
        for (var e = edgeIterator(i);e.hasNext();) {
            var c = e.next();
            if( c.getEnd() == j) {
                return true;
            }
        }
        return false;

    }

    @Override
    public int getWeight(int i, int j) {
        if(checkIndex(i,j))
            throw new IndexOutOfBoundsException();
        if(!isEdge(i,j))
            throw new NoSuchElementException();

        return adj.get(i).get(j).getValue();
    }

    @Override
    public Iterator<Edge> edgeIterator(int i) {
        return adj.get(i).stream().filter(edge -> edge.getValue() != 0).iterator();
    }

    @Override
    public void forEachEdge(int i, Consumer<Edge> consumer) {
        edgeIterator(i).forEachRemaining(consumer);
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

    public String toMatGraph() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append(n).append("\n");

        IntStream.range(0, n).forEach(i -> {
            adj.get(i).forEach(e -> {
                if(e.getValue() == 0) {
                   stringBuilder.append("0").append(" ");
                } else {
                    stringBuilder.append(e.getEnd()).append(" ");
                }
            });
            stringBuilder.append("\n");
        });

        return stringBuilder.toString();
    }


    public int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static void main(String[] args) {
        var graphAdj = new AdjGraph(3);
        graphAdj.addEdge(0, 1, 4);
        graphAdj.addEdge(1,2,3);
        graphAdj.addEdge(2, 0, 3);
        graphAdj.addEdge(2,1,6);

        System.out.println(graphAdj.toGraphviz());
    }
}