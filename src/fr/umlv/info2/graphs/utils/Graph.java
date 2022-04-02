package fr.umlv.info2.graphs.utils;

import fr.umlv.info2.graphs.classes.MatGraph;

import java.util.Iterator;
import java.util.function.Consumer;

public interface Graph {
    int numberOfEdges () ;
    int numberOfVertices () ;
    void addEdge (int i, int j, int value ) ;
    boolean isEdge (int i, int j) ;
    int getWeight (int i, int j) ;
    Iterator<Edge > edgeIterator (int i) ;
    void forEachEdge (int i, Consumer<Edge > consumer ) ;
    String toGraphviz () ;

    default public Graph transpose() {
        var g = new MatGraph(numberOfVertices());
        for (int i = 0; i < numberOfVertices(); i++) {
            for (int j = 0; j < numberOfVertices(); j++) {

               if(isEdge(i,j)) {
                   g.addEdge(j, i, 1);
               }
            }
        }
        return g;
    }
}