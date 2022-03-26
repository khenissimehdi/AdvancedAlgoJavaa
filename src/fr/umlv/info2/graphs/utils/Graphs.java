package fr.umlv.info2.graphs.utils;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;

public class Graphs {
// depth - first
public static List<Integer> recDFS(Graph g, int s, boolean[] v) {
    v[s] = true;
    var list = new ArrayList<Integer>();
    list.add(s);
    for (Iterator<Edge> it = g.edgeIterator(s); it.hasNext(); ) {
        Edge value = it.next();

        if (!v[value.getEnd()]) {
          list.add(value.getEnd());
          list.addAll(recDFS(g,value.getEnd(), v));
        }
    }
    return list;
}
public static List<Integer> DFS(Graph g, int v0) {
    boolean [] v = new boolean[g.numberOfVertices()];
    var list = new ArrayList<Integer>();

    for (int i = 0; i < g.numberOfVertices(); i++) {
        if(!v[i]) {
           list.addAll(recDFS(g,i,v));
        }
    }


    return list.stream().distinct().toList();
}

/*
public static List<Integer> recBFS(Graph g, int s, boolean[] v) {

}*/

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



    public static int [][] timedDepthFirstSearch ( Graph g, int v0) {
        var n= g.numberOfVertices();
        int[][] res = new int[n][n];
        LongAdder v = new LongAdder();


    }


}