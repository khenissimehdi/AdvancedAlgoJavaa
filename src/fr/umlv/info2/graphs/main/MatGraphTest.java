package fr.umlv.info2.graphs.main;

import fr.umlv.info2.graphs.classes.AdjGraph;
import fr.umlv.info2.graphs.classes.MatGraph;
import fr.umlv.info2.graphs.utils.Edge;
import fr.umlv.info2.graphs.utils.Graph;
import fr.umlv.info2.graphs.utils.Graphs;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MatGraphTest {

    static MatGraph createNewGraph() {
        var graph = new MatGraph(3);
        graph.addEdge(0,1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(2, 1, 1);
        return graph;
    }

    @Test
    void numberOfVertices() {
        var graph = createNewGraph();
        assertEquals(3, graph.numberOfVertices());
    }

    @Test
    void numberOfEdges() {
        var graph = createNewGraph();
        assertEquals(4, graph.numberOfEdges());
    }

    @Test
    void addEdge() {
        var graph = createNewGraph();
        assertEquals(4, graph.numberOfEdges());
        graph.addEdge(1,0, 1);
        assertEquals(5, graph.numberOfEdges());
    }

    @Test
    void isEdge() {
        var graph = createNewGraph();

        assertFalse(graph.isEdge(0, 0));
        assertTrue(graph.isEdge(0, 1));
    }

    @Test
    void getWeight() {
        var graph = createNewGraph();
        assertEquals(1 ,graph.getWeight(1,2));
    }

    @Test
    void edgeIterator() {
        var graph = createNewGraph();
        graph.addEdge(1, 0, 1);

        int count = 0;
        for (Iterator<Edge> it = graph.edgeIterator(1); it.hasNext(); ) {
            Edge value = it.next();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void forEachEdge() {
        var graph = createNewGraph();
        final int[] count = new int[1];
        graph.forEachEdge(0, edge -> count[0]++);

        assertEquals(1, count[0]);
    }

    @Test
    void toGraphviz() {
        var graph = createNewGraph();
        assertEquals("""
                digraph G {
                0;
                0 -> 1 [ label="1" ] ;
                1;
                1 -> 2 [ label="1" ] ;
                2;
                2 -> 0 [ label="1" ] ;
                2 -> 1 [ label="1" ] ;
                }""", graph.toGraphviz());
    }

    @Test
    void dfs() {
        var graph = new MatGraph(7);
        graph.addEdge(0,1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3,1);
        graph.addEdge(3, 2, 1);
        graph.addEdge(4, 3, 1);
        graph.addEdge(4,  5,1);
        graph.addEdge(6, 5, 1);
        graph.addEdge(6, 4, 1);

      //System.out.println(graph.toGraphviz());
        boolean [] v = new boolean[graph.numberOfVertices()];
        System.out.println(Graphs.DFS(graph, 0,v));
    }

   /* @Test
    void randGraph() {
        var graph = new MatGraph(4);
        var rg = graph.genRandGraph(4,8);
        System.out.println(rg.toGraphviz());
    }

    @Test
    void toMat(){
        var graph = new MatGraph(4);
        var rg = graph.genRandGraph(4, 8);
        System.out.println(rg.toMatGraph());
    }*/

    @Test
    void timedDFS() {
        var graph = new MatGraph(7);
        graph.addEdge(0,1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3,1);
        graph.addEdge(3, 2, 1);
        graph.addEdge(4, 3, 1);
        graph.addEdge(4,  5,1);
        graph.addEdge(6, 5, 1);
        graph.addEdge(6, 4, 1);

        System.out.println(Graphs.topologicalSort(graph,false));


    }
    @Test
    void topoSort() {
        var g = new MatGraph(6);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(1, 4, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 3, 1);
        g.addEdge(4, 5, 1);
        g.addEdge(2, 5, 1);
        g.addEdge(5, 2, 1);
        g.addEdge(3, 1, 1);
        g.addEdge(3, 0, 1);
        var c =  Graphs.timedDepthFirstSearch(g, 0);


        for (int j = 0; j < g.numberOfVertices(); j++) {
                System.out.println(c[1][j]);
        }

        System.out.println(Graphs.scc(g));

    }

    @Test
    void topoSort2() {
        var g = new MatGraph(5);
        g.addEdge(0, 2, 1);
        g.addEdge(0, 3, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(2, 1, 1);
        g.addEdge(3, 4, 1);


        System.out.println(Graphs.scc(g));

    }

    @Test
    void test() {


        var g = new AdjGraph(5);
        g.addEdge(0,1, 2);
        g.addEdge(0,2,-1);
        g.addEdge(2, 4,2);
        g.addEdge(1, 3,3);
        g.addEdge(3, 4, 1);
        g.addEdge(4,1, 0);
        g.addEdge(1,2,1);

        Graphs.bellmanFord(g, 0).printShortestPathTo(4);

    }

    @Test
    void test2() {
        var g = new AdjGraph(5);
        g.addEdge(0,1, 2);
        g.addEdge(0,2,5);
        g.addEdge(2, 4,2);
        g.addEdge(1, 3,1);
        g.addEdge(3, 4, 6);
        g.addEdge(4,1, 1);
        g.addEdge(1,2,2);

        System.out.println(Graphs.dijkstra(g,0));

    }



    @Test
    void test3() {


        var g = new AdjGraph(4);
        g.addEdge(0, 1, 3);
        g.addEdge(0,2, 2);
        g.addEdge(2,1, 1);

        g.addEdge(2,3, 4);
        g.addEdge(1,3, 3);

        g.addEdge(2,1, -1);

        System.out.println(Graphs.floydWarshall(g));

    }
}