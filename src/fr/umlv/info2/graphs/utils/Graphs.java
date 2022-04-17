package fr.umlv.info2.graphs.utils;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Graphs {
// depth - first
public static void recDFS(Graph g, int s, boolean[] v, ArrayList<Integer> list) {
    v[s] = true;
    list.add(s);
    for (Iterator<Edge> it = g.edgeIterator(s); it.hasNext(); ) {
        Edge value = it.next();
        if (!v[value.getEnd()]) {
          recDFS(g,value.getEnd(), v,list);
        }
    }
}
public static List<Integer> DFS(Graph g, int v0, boolean[] v) {
    var list = new ArrayList<Integer>();
    recDFS(g, v0, v, list);
    return list;
}


// breadth - first
public static List < Integer > BFS( Graph g, int v0) {
    var list = new ArrayList<Integer>();
    boolean [] v = new boolean[g.numberOfVertices()];

    for (int i = v0; i < g.numberOfVertices(); i++) {
        g.forEachEdge(i, edge -> {
            if(!v[edge.getEnd()]) {
                v[edge.getStart()] = true;
                list.add(edge.getStart());
                list.add(edge.getEnd());
            }
        });
    }
    return list.stream().distinct().toList();
}

    public static void recDFSTimed(Graph g,
                                                  int s,
                                                  int[] start,
                                                  int[] end,
                                                  LongAdder counter,
                                                  LinkedList<Integer> list,
                                                  boolean[] v) {
        v[s] = true;
        start[s] = counter.intValue();
        counter.increment();
        for (Iterator<Edge> it = g.edgeIterator(s); it.hasNext(); ) {
            Edge value = it.next();
            if (start[value.getEnd()] == 0) {
                recDFSTimed(g,value.getEnd(), start, end, counter, list, v);
            }
        }
        list.add(s);
        end[s] = counter.intValue();
        counter.increment();

    }

    public static LinkedList<Integer> recDFSTimed2(Graph g,
                                                  int s,
                                                  int[] start,
                                                  int[] end,
                                                  LongAdder counter,
                                                  LinkedList<Integer> list,
                                                  boolean[] v) {
        v[s] = true;

        start[s] = counter.intValue();
        counter.increment();
        for (Iterator<Edge> it = g.edgeIterator(s); it.hasNext(); ) {
            Edge value = it.next();
            if (start[value.getEnd()] == 0) {
                recDFSTimed(g,value.getEnd(), start, end, counter, list, v);
            }
        }
        list.add(s);
        end[s] = counter.intValue();
        counter.increment();
        return list;

    }


    public static boolean recIsCyclic(int i, boolean[] visited, boolean[] recStack, Graph g) {
        if(recStack[i]) {
            return true;
        }

        if(visited[i]) {
            return false;
        }

        visited[i] = true;
        recStack[i] = true;

        for (Iterator<Edge> it = g.edgeIterator(i); it.hasNext(); ) {
            Edge value = it.next();
            if (recIsCyclic(value.getEnd(),visited,recStack,g)) {
                return true;
            }
        }

        recStack[i] = false;
        return false;
    }

    public static boolean isCyclic(Graph g) {
        var nbVertices = g.numberOfVertices();
        boolean[] visited = new boolean[nbVertices];
        boolean[] recStack = new boolean[nbVertices];

        for (int i = 0; i < g.numberOfVertices(); i++) {
            if(recIsCyclic(i, visited, recStack, g)) {
                return true;
            }
        }
        return  false;
    }

    public static int [][] timedDepthFirstSearch ( Graph g, int v0) {
        var l = new LinkedList<Integer>();
        var n= g.numberOfVertices();
        var v = new boolean[n];
        var counter = new LongAdder();
        int[][] res = new int[2][n];
        recDFSTimed(g, v0, res[0], res[1], counter,l,v);
        return res;
    }

    public static List <Integer> topologicalSort(Graph g, boolean cycleDetect) {
        if(cycleDetect && isCyclic(g)) {
            throw new IllegalStateException("Cycle detected");
        }

        var l = new LinkedList<Integer>();
        boolean [] v = new boolean[g.numberOfVertices()];

        for (int i = 0; i < g.numberOfVertices(); i++) {
            if(!v[i]) {
                recDFSTimed(g,i,new int[g.numberOfVertices()],new int[g.numberOfVertices()],new LongAdder(),l, v);
            }
        }
        var res = new ArrayList<>(l);
        Collections.reverse(res);
        return res ;
    }

    public static List<List<Integer>> scc(Graph g) {
        var l = new ArrayList<>(Graphs.topologicalSort(g,false)).stream().distinct().toList();
        boolean [] v = new boolean[g.numberOfVertices()];
        var gt = g.transpose();

        var nl = new ArrayList<List<Integer>>();
        for (int i = 0; i < l.size(); i++) {
            if(!v[l.get(i)]) {
                nl.add(Graphs.DFS(gt, l.get(i), v));
            }
        }
        return nl;
    }

    public static ShortestPathFromOneVertex bellmanFord(Graph g, int source) {

        var d =  new int[g.numberOfVertices()];
        var pi =  new int[g.numberOfVertices()];
        Arrays.fill(pi, -1);
        pi[source] = source;
        for (int j = 1; j < g.numberOfVertices()-1; j++) {
            innerLoop(g, d, pi);
        }

        return new ShortestPathFromOneVertex(source, d, pi);
    }

    private static void innerLoop(Graph g, int[] d, int[] pi) {

        for (int i = 0; i < g.numberOfVertices(); i++) {
            for (Iterator<Edge> it = g.edgeIterator(i); it.hasNext(); ) {
                Edge value = it.next();
                var w = value.getValue();
                if(pi[value.getStart()] != -1) {
                    if(pi[value.getEnd()] == -1 || d[value.getStart()] + w < d[value.getEnd()] ) {
                        d[value.getEnd()] = d[value.getStart()] + w;
                        pi[value.getEnd()] = value.getStart();
                    }
                }
            }
        }
    }


    public static ShortestPathFromOneVertex dijkstra(Graph g, int source) {
        var numberOfVertices = g.numberOfVertices();
        var todo = new BitSet(numberOfVertices);
        var d = new int[numberOfVertices];
        var pi = new int[numberOfVertices];
        for (var i = 0; i < numberOfVertices; i++) {
            d[i] = -1;
            pi[i] = -1;
            todo.set(i);
        }
        d[source] = source;

        while (!todo.isEmpty()) {
            var s = todo.nextSetBit(0);
            for (var k = s + 1; k < g.numberOfVertices(); k++) {
                if (!todo.get(k)) {
                    continue;
                }

                if (d[k] != -1 && d[s] != -1) {
                    if ( d[k] < d[s]) {
                        s = k;
                    }
                }
            }
            todo.set(s, false);

            if (d[s] == -1) {
                continue;
            }

            var iterator = g.edgeIterator(s);
            while (iterator.hasNext()) {
                var edge = iterator.next();
                var edgeWeight = edge.getValue();
                var edgeEnd = edge.getEnd();
                if ( d[edgeEnd] == -1 || d[s] + edgeWeight < d[edgeEnd]) {

                    d[edgeEnd] = d[s] + edgeWeight;
                    pi[edgeEnd] = s;
                }
            }
        }
        d = Arrays.stream(d).toArray();
        return new ShortestPathFromOneVertex(source, d, pi);
    }


    public static ShortestPathFromAllVertices floydWarshall(Graph g) {
        Objects.requireNonNull(g);
        int V = g.numberOfVertices();
        int[][] d = new int[V][V];
        int[][] pi = new int[V][V];

        for (int s = 0 ; s < V ; ++s) {
            for (int t = 0 ; t < V ; ++t) {
                if (s == t) {
                    d[s][t] = 0;
                    pi[s][t] = s;
                }
                else {
                    if (g.isEdge(s, t)) {
                        d[s][t] = g.getWeight(s, t);
                        pi[s][t] = s;
                    }
                    else {
                        d[s][t] = -1;
                        pi[s][t] = -1;
                    }
                }
            }
            var iterator = g.edgeIterator(s);
            while (iterator.hasNext()) {
                var edge = iterator.next();
                var value = edge.getValue();
                var start = edge.getStart();
                var end = edge.getEnd();
                if ( value < d[start][end]) {
                    d[start][end] = value;
                    pi[start][end] = start;
                }
            }
        }

        for (int k = 0 ; k < V ; ++k) {
            for (int s = 0 ; s < V ; ++s) {
                for (int t = 0 ; t < V ; ++t) {
                    if ( s != t && pi[s][k] != -1 && pi[k][t] != -1 && (d[s][t] > d[s][k] + d[k][t] || pi[s][t] == -1)) {
                        d[s][t] = d[s][k] + d[k][t];
                        pi[s][t] = pi[k][t];
                    }
                }
            }
        }

        return new ShortestPathFromAllVertices(d, pi);
    }

}