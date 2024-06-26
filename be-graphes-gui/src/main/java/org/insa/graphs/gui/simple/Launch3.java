package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;



public class Launch3 {

    private static List <ArcInspector> listInspector;
    private static ArcInspector arcInspector;

    
    
    
    /* Create a new Drawing inside a JFrame an return it.

    *

    * @return The created drawing.

    *

    * @throws Exception if something wrong happens when creating the graph.

    */
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


    public static Graph init_carte(String mapName) throws FileNotFoundException, IOException{
        Graph g;
        GraphReader reader = new BinaryGraphReader(
        new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        g = reader.read();
        reader.close();
        return g;
    }


    public static void compareDijkstraEtBellman(ShortestPathSolution solutionDijkstra){
        BellmanFordAlgorithm bellmanAlgo;
        ShortestPathSolution solutionBellman;
        bellmanAlgo= new BellmanFordAlgorithm(solutionDijkstra.getInputData());
        solutionBellman = bellmanAlgo.run();
        System.out.println("BELLMAN:  "+solutionBellman.toString());
        System.out.println("DIJKSTRA:  "+solutionDijkstra.toString());
    }



    public static void compareDijkstraEtAstar(ShortestPathSolution solutionDijkstra){
        AStarAlgorithm aStarAlgo;
        ShortestPathSolution solutionAStar;
        aStarAlgo= new AStarAlgorithm(solutionDijkstra.getInputData());
        solutionAStar = aStarAlgo.run();
        System.out.println("ASTAR:  "+solutionAStar.toString());
        System.out.println("DIJKSTRA:  "+solutionDijkstra.toString());
    }


    public static ShortestPathData initialize_data(int Origin_param, int Destination_param, int Mode, Graph graph) {
        Node Origin = graph.get(Origin_param);
        Node Destination = graph.get(Destination_param);
        listInspector = ArcInspectorFactory.getAllFilters(); // Corrigé ici pour assigner à la variable de classe
        arcInspector = listInspector.get(Mode);//Mode 0=allArcsL; Mode 1=forCarsL; Mode 3= forCarsT; Mode 4=forBicyclesT;
        ShortestPathData data = new ShortestPathData(graph, Origin, Destination, arcInspector);
        return data;
    }

    public static void verifierDijkstraAvecPath(ShortestPathSolution solutionDijkstra){
        AbstractInputData.Mode m=solutionDijkstra.getInputData().getMode();
        if (solutionDijkstra.isFeasible()){
            if (m==AbstractInputData.Mode.LENGTH){
                System.out.println("Resultat du Dijkstra trouvé par la classe Path: "+ solutionDijkstra.getPath().getLength()/1000);
            }
            else if (m==AbstractInputData.Mode.TIME){
                System.out.println("Resultat du Dijkstra trouvé par la classe Path: "+ solutionDijkstra.getPath().getMinimumTravelTime()/60);
            }
        }

    }


    



    public static void main(String[] args) throws Exception {
        try{
            DijkstraAlgorithm dijkstraAlgo;
            ShortestPathSolution solutionDijkstra;
            //TEST POUR LE GRAPHE INSA
            Graph g = init_carte("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr");
            System.out.println("-----------------------");
            System.out.println("TEST POUR LE GRAPHE INSA");
            
            //Test chemin plus court pour tous les moyens de transport(chemin possible)
            ShortestPathData d=initialize_data(10, 30, 0, g);
            System.out.println("-----------------------");
            System.out.println(d.toString());
            dijkstraAlgo = new DijkstraAlgorithm(d);
            solutionDijkstra = dijkstraAlgo.run();
            verifierDijkstraAvecPath(solutionDijkstra);
            compareDijkstraEtBellman(solutionDijkstra);
            compareDijkstraEtAstar(solutionDijkstra);

            //Test chemin plus court uniquement pour voitures (chemin non disponible)
            d=initialize_data(20, 40, 1, g);
            System.out.println("-----------------------");
            System.out.println(d.toString());
            dijkstraAlgo = new DijkstraAlgorithm(d);
            solutionDijkstra = dijkstraAlgo.run();
            verifierDijkstraAvecPath(solutionDijkstra);
            compareDijkstraEtBellman(solutionDijkstra);
            compareDijkstraEtAstar(solutionDijkstra);

            //Test chemin plus rapide pour tous les moyens de transport (chemin disponible)
            d=initialize_data(5, 25, 2, g);
            System.out.println("-----------------------");
            System.out.println(d.toString());
            dijkstraAlgo = new DijkstraAlgorithm(d);
            solutionDijkstra = dijkstraAlgo.run();
            verifierDijkstraAvecPath(solutionDijkstra);
            compareDijkstraEtBellman(solutionDijkstra);
            compareDijkstraEtAstar(solutionDijkstra);
            
            //Test chemin plus rapide uniquement a pied (chemin non disponible)
            d=initialize_data(0, 50, 3, g);
            System.out.println("-----------------------");
            System.out.println(d.toString());
            dijkstraAlgo = new DijkstraAlgorithm(d);
            solutionDijkstra = dijkstraAlgo.run();
            verifierDijkstraAvecPath(solutionDijkstra);
            compareDijkstraEtBellman(solutionDijkstra);
            compareDijkstraEtAstar(solutionDijkstra);
            

            //TEST POUR LE GRAPHE CARRE_DENSE
            System.out.println("-----------------------");
            System.out.println("TEST POUR LE GRAPHE CARRE_DENSE");
            g = init_carte("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre-dense.mapgr");
            
            d=initialize_data(0, 500, 0, g);
            System.out.println("-----------------------");
            System.out.println(d.toString());
            dijkstraAlgo = new DijkstraAlgorithm(d);
            solutionDijkstra = dijkstraAlgo.run();
            //compareDijkstraEtBellman(d);
            verifierDijkstraAvecPath(solutionDijkstra);
            compareDijkstraEtAstar(solutionDijkstra);
            

            d=initialize_data(0, 500, 2, g);
            System.out.println("-----------------------");
            System.out.println(d.toString());
            dijkstraAlgo = new DijkstraAlgorithm(d);
            solutionDijkstra = dijkstraAlgo.run();
            //compareDijkstraEtBellman(d);
            verifierDijkstraAvecPath(solutionDijkstra);
            compareDijkstraEtAstar(solutionDijkstra);

            System.out.println("-----------------------");

            //TEST POUR HAUTE-GARONNE EN TEMPS ET DISTANCE
            System.out.println("-----------------------");
            System.out.println("TEST POUR HAUTE-GARONNE EN TEMPS ET DISTANCE");
            g = init_carte("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/haute-garonne.mapgr");
            d=initialize_data(12669, 35646, 2, g);
            System.out.println("-----------------------");
            System.out.println(d.toString());
            dijkstraAlgo = new DijkstraAlgorithm(d);
            solutionDijkstra = dijkstraAlgo.run();
            verifierDijkstraAvecPath(solutionDijkstra);
            compareDijkstraEtAstar(solutionDijkstra);
            d=initialize_data(12669, 35646, 0, g);
            System.out.println("-----------------------");
            System.out.println(d.toString());
            dijkstraAlgo = new DijkstraAlgorithm(d);
            solutionDijkstra = dijkstraAlgo.run();
            verifierDijkstraAvecPath(solutionDijkstra);
            compareDijkstraEtAstar(solutionDijkstra);

            
            //TEST POUR CHILE QUI NE DOIT TROUVER AUCUN CHEMIN
            System.out.println("-----------------------");
            System.out.println("TEST POUR CHILE QUI NE DOIT TROUVER AUCUN CHEMIN");
            g = init_carte("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/chile.mapgr");
            d=initialize_data(122669, 356046, 0, g);
            System.out.println("-----------------------");
            System.out.println(d.toString());
            dijkstraAlgo = new DijkstraAlgorithm(d);
            solutionDijkstra = dijkstraAlgo.run();
            verifierDijkstraAvecPath(solutionDijkstra);
            compareDijkstraEtAstar(solutionDijkstra);
            
            //TEST POUR FRACTAL POUR MONTRER LES DIFFERENCES DE TEMPS ENTRE A-STAR ET DIJKSTRA
            g = init_carte("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/fractal.mapgr");
            d=initialize_data(0, 200, 2, g);
            System.out.println("-----------------------");
            System.out.println("TEST POUR FRACTAL POUR MONTRER LES DIFFERENCES DE TEMPS ENTRE A-STAR ET DIJKSTRA");
            System.out.println(d.toString());
            dijkstraAlgo = new DijkstraAlgorithm(d);
            solutionDijkstra = dijkstraAlgo.run();
            verifierDijkstraAvecPath(solutionDijkstra);
            compareDijkstraEtAstar(solutionDijkstra);
            

        }
        catch(FileNotFoundException e){
            System.out.println("Fichier pas trouve");
        }
        catch(IOException e){
            System.out.println("Probleme init_carte");
        }
    }
}
