package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    public Label CreateLabel(Node n, ShortestPathData data){
        Label l = new Label(n,false,Double.POSITIVE_INFINITY,null);
        return l;
    }




    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        // TODO:
        
        
        Graph g= data.getGraph();
        final int nbNodes = g.size();
        List<Node> ListNodes=g.getNodes();
        List<Label> labels = new ArrayList<>();
        for(Node n : ListNodes){
            Label l = CreateLabel(n, data);
            labels.add(l);
        }
        
        
        Label s=labels.get(data.getOrigin().getId());
        s.setCout_realise(0);
        Label p=labels.get(data.getDestination().getId());

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];
        
        BinaryHeap<Label> tas = new BinaryHeap<>();
        tas.insert(s);
        while (!tas.isEmpty() && !p.isMarque()){
            Label x = tas.deleteMin();
            x.setMarque(true);
            notifyNodeMarked(x.getSommet_courant());
            Node nodex =x.getSommet_courant();
            List<Arc> successors = nodex.getSuccessors();
            for (Arc a : successors){
                if (!data.isAllowed(a)) {
                    continue;
                }
                Label courant=labels.get(a.getDestination().getId());
                if (!courant.isMarque()){
                    if (courant.getCost()> x.getCost()+a.getLength()){
                        courant.setCout_realise(x.getCost()+a.getLength());
                        if (tas.Exist(courant)){
                            tas.remove(courant);
                            tas.insert(courant);
                        }
                        else{
                            tas.insert(courant);
                            notifyNodeReached(a.getDestination());
                        }
                        courant.setPere(a);
                        predecessorArcs[a.getDestination().getId()] = a;
                        
                    }
                }
            }
        }


        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arco = predecessorArcs[data.getDestination().getId()];
            while (arco != null) {
                arcs.add(arco);
                arco = predecessorArcs[arco.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(g, arcs));
        }

        
        return solution;
    }

}
