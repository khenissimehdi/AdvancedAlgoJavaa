package fr.umlv.info2.graphs.main;

import fr.umlv.info2.graphs.classes.AdjGraph;
import fr.umlv.info2.graphs.classes.MatGraph;
import fr.umlv.info2.graphs.utils.Edge;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class AdjGraphTest {

    static AdjGraph createNewGraph() {
        var graph = new AdjGraph(3);
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
    void toMat(){
        var g = createNewGraph();
        var rg = g.genRandGraph(4, 8);
        System.out.println(rg.toMatGraph());
    }
}