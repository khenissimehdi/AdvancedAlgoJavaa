package fr.umlv.info2.graphs.utils;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

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

    public static List<Integer> recDFSTimed(Graph g, int s, int[] start, int[] end, LongAdder counter ) {
        start[s] = counter.intValue();
        counter.increment();

        var list = new ArrayList<Integer>();
        list.add(s);

        for (Iterator<Edge> it = g.edgeIterator(s); it.hasNext(); ) {
            Edge value = it.next();
            if (start[value.getEnd()] == 0) {
                list.add(value.getEnd());
                list.addAll(recDFSTimed(g,value.getEnd(), start, end, counter));
            }
        }
        end[s] = counter.intValue();
        counter.increment();
        return list;
    }


    public static int [][] timedDepthFirstSearch ( Graph g, int v0) {
        var n= g.numberOfVertices();
        var counter = new LongAdder();
        int[][] res = new int[2][n];

        for (int i = 0; i < g.numberOfVertices(); i++) {
            if(res[0][i] == 0) {
                recDFSTimed(g, i, res[0], res[1], counter);
            }
        }

        return res;
    }


    public static List<Integer> topoRecDFS(Graph g, int s, boolean[] v) {
        var list = new ArrayList<Integer>();
        Iterator<Edge> it = g.edgeIterator(s);
        while (it.hasNext()) {
            Edge value = it.next();
            if (!v[value.getEnd()]) {
                v[s] = true;
                System.out.println(value.getEnd());

                list.addAll(topoRecDFS(g,value.getEnd(), v));
                list.add(value.getEnd());
            }
        }



        return list;
    }

    public static List < Integer > topologicalSort ( Graph g, boolean cycleDetect ) {

        boolean [] v = new boolean[g.numberOfVertices()];
        var list = new ArrayList<Integer>();

        for (int i = 0; i < g.numberOfVertices(); i++) {

            if(!v[i]) {
                list.addAll(topoRecDFS(g,i,v));
            }
        }
        list.add(0);
        var res = list.stream().distinct().collect(Collectors.toList());
        Collections.reverse(res);
        return res;
    }


}