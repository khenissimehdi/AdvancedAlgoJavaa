package fr.umlv.info2.graphs.classes;
import fr.umlv.info2.graphs.utils.Edge;
import fr.umlv.info2.graphs.utils.Graph;

import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class MatGraph implements Graph {
    private final int [][] mat ;
    private final int n; // number of vertices
    private int numberOfEdges = 0;

    public MatGraph(int n) {
        mat = new int[n][n];
        this.n = n;
    }

    @Override
    public int numberOfEdges() {
        return numberOfEdges;
    }

    @Override
    public int numberOfVertices() {
        return n;
    }

    @Override
    public void addEdge(int i, int j, int value) {
        if(checkIndex(i, j))
            throw new IndexOutOfBoundsException();
        numberOfEdges++;
        mat[i][j] = value;
    }

    @Override
    public boolean isEdge(int i, int j) {
        if(checkIndex(i, j))
            throw new IndexOutOfBoundsException();
        return mat[i][j] > 0;
    }

    @Override
    public int getWeight(int i, int j) {
        if (checkIndex(i, j) )
            throw new IndexOutOfBoundsException();
        return mat[i][j] ;
    }

    @Override
    public Iterator<Edge> edgeIterator(int i) {
        return new Iterator<>() {
            private int index = computeNext(0);
            @Override
            public boolean hasNext() {
                return index != -1;
            }

            private int computeNext(int src) {
                for (int j = src; j < mat[i].length; j++) {
                    if(mat[i][j] > 0) {
                        return j;
                    }
                }
                return -1;
            }

            @Override
            public Edge next() {
                if(!hasNext())
                    throw new NoSuchElementException();
                var temp = index;
                index = computeNext(temp + 1 );
                return new Edge(i, temp, mat[i][temp]);
            }
        };
    }

    @Override
    public void forEachEdge(int i, Consumer<Edge> consumer) {
        edgeIterator(i).forEachRemaining(consumer);
    }


    @Override
    public String toGraphviz() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("digraph G {").append("\n");
        IntStream.range(0, mat.length).forEach(i -> {
            stringBuilder.append(i).append(";").append("\n");
            forEachEdge(i, edge -> stringBuilder.append(i).append(" -> ").
                    append(edge.getEnd()).append(" [ ").append("label=\"").
                    append(edge.getValue()).append("\"").append(" ]").
                    append(" ;").append("\n"));
        });
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String toMatGraph() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append(mat[0].length).append("\n");
        for (int i = 0; i < mat.length; i++) {
            stringBuilder.append(i).append(" ");
            for (int j = 0; j < mat[0].length; j++) {
                if(!isEdge(i, j)) {
                    stringBuilder.append("0").append(" ");
                } else {
                    stringBuilder.append(j).append(" ");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


    private boolean checkIndex(int i, int j ) {
        int numberOfColumns = mat.length;
        int numberOfRows = mat[0].length;
        return   i > numberOfRows || j > numberOfColumns;
    }

    public MatGraph genRandGraph(int v, int e) {
        if(v > (e*e)) {
            throw new AssertionError("nope");
        }
        var g = new MatGraph(v);
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

    public static void main(String[] args) {

        var graph = new MatGraph(3);
        graph.addEdge(0,1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(2, 1, 1);

        System.out.println(graph.toGraphviz());
    }
}