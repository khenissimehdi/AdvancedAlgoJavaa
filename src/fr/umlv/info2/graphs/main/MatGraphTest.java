package fr.umlv.info2.graphs.main;

import fr.umlv.info2.graphs.classes.AdjGraph;
import fr.umlv.info2.graphs.classes.MatGraph;
import fr.umlv.info2.graphs.utils.Edge;
import fr.umlv.info2.graphs.utils.Graphs;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

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
        var graph = new MatGraph(4);
        graph.addEdge(0,1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(3, 1, 1);
        graph.addEdge(3, 2, 1);

      // System.out.println(graph.toGraphviz());
        System.out.println(Graphs.DFS(graph, 0));
    }

    @Test
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
    }

    @Test
    void timedDFS() {
        var graph = new MatGraph(3);
        graph.addEdge(0,1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 2, 1);

        var c =  Graphs.timedDepthFirstSearch(graph, 0);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < graph.numberOfVertices(); j++) {
                System.out.println(c[0][j]);
            }
        }
    }
    @Test
    void topoSort() {
        var g = new MatGraph(6);
        g.addEdge(0, 1,1);
        g.addEdge(1, 3,1);
        g.addEdge(1,5,1);
        g.addEdge(3,2,1);
        g.addEdge(2,4,1);
        g.addEdge(5, 4, 1);

        System.out.println(Graphs.topologicalSort(g, false));
    }
}