package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;

public class Launch2 {


    /**
     * Create a new Drawing inside a JFrame an return it.
     * 
     * @return The created drawing.
     * 
     * @throws Exception if something wrong happens when creating the graph.
     */
    
    private static int Origin_int;
    private static int Destination_int;
    private static int numInspector;
    
    private static Node Origin;
    private static Node Destination;
    
    private static List <ArcInspector> listInspector;
    private static ArcInspector arcInspector;
    
    private static ShortestPathData data;
    
    private static DijkstraAlgorithm dijkstraAlgo;
    private static AStarAlgorithm aStarAlgo;
    
    private static ShortestPathSolution solutionDijkstra;
    private static ShortestPathSolution solutionAStar;
    private static Graph graphINSA = null;
    private static Graph graphWashington = null;
    private static Graph graphBelgium = null;
    
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }
    
    public static void initAll() throws Exception {


        // Visit these directory to see the list of available files on Commetud.
        String mapINSA = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        String mapBelgium = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/belgium.mapgr";
        String mapWashington = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/washington.mapgr";



        final GraphReader readerINSA = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(mapINSA))));
    final GraphReader readerBelgium = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(mapBelgium))));
    final GraphReader readerWashington = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(mapWashington))));

    graphINSA = readerINSA.read();
    graphBelgium = readerBelgium.read();
    graphWashington = readerWashington.read();
        
    
        
    }
    public static void initialize(int Origin_param, int Destination_param, int Road, Graph graph) {
        Origin = graph.get(Origin_param);
        Destination = graph.get(Destination_param);
        listInspector = ArcInspectorFactory.getAllFilters(); // Corrigé ici pour assigner à la variable de classe
        arcInspector = listInspector.get(Road);
        data = new ShortestPathData(graph, Origin, Destination, arcInspector);
        dijkstraAlgo = new DijkstraAlgorithm(data);
        aStarAlgo= new AStarAlgorithm(data);
        solutionDijkstra = dijkstraAlgo.run();
        solutionAStar = aStarAlgo.run();
    }
    

    public static void main(String[] args) throws Exception {
        initAll();
        // Tests bon pour INSA

        initialize(10, 30, 0, graphINSA);
        System.out.println("Shortest path length from node 0 to node 100 with Dijkstra for INSA: " + solutionDijkstra.getPath().getLength());
        initialize(10, 30, 0, graphINSA);
        System.out.println("Shortest path length from node 0 to node 100 with Bellman-Ford for INSA: " + solutionAStar.getPath().getLength());

        testShortestAllRoads("INSA", graphINSA, 0, 1200, 0);
    
        testFastestAllRoads("INSA", graphINSA, 0, 500, 2);
    
        testShortestLongDistance("INSA", graphINSA, 143, 600, 0);
        testShortestShortDistance("INSA", graphINSA, 95, 200, 0);

        initialize(0, 100, 0, graphBelgium);
        System.out.println("Shortest path length from node 0 to node 100 with Dijkstra for Belguim : " + solutionDijkstra.getPath().getLength());
        initialize(0, 100, 0, graphBelgium);
        System.out.println("Shortest path length from node 0 to node 100 with A* for Belguim : " + solutionAStar.getPath().getLength());

        testShortestAllRoads("Belgium", graphBelgium, 0, 150, 0);
        testShortestLongDistance("Belgium", graphBelgium, 358866, 273663, 0);
        testShortestShortDistance("Belgium", graphBelgium, 157080, 157078, 0);

    }


    public static void testShortestAllRoads(String graphName, Graph graph, int origin, int destination, int road) throws IOException {
        System.out.println("---- testShortestAllRoads -----------");

        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with A* " +  solutionAStar.getPath().getLength());
    }

    public static void testFastestAllRoads(String graphName, Graph graph, int origin, int destination, int road) throws IOException {
        System.out.println("---- testFastestAllRoads-----------");

        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
        initialize(origin, destination, road, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with A* : " +  solutionAStar.getPath().getLength());
    }

    public static void testShortestLongDistance(String graphName, Graph graph, int origin, int destination, int road) throws IOException {
        System.out.println("---- testShortestLongDistance ----------");
        initialize(origin, destination, road, graph);

        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with A* : " +  solutionAStar.getPath().getLength());
    }

    public static void testShortestShortDistance(String graphName, Graph graph, int origin, int destination, int road) throws IOException {

        System.out.println("---- testShortestLongDistance -----------");
        initialize(origin, destination, road, graph);

        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with Dijkstra: " + solutionDijkstra.getPath().getLength());
        System.out.println("Shortest path length from node " + origin + " to node " + destination + " in " + graphName + " with A* : " +  solutionAStar.getPath().getLength());
    }
}
