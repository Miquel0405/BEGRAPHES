package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }


    protected boolean ExistsNotMarked(List<Label> labels){
        boolean res=false;
        for (Label l : labels){
            if (!l.isMarque()){
                res=true;
                break;
            }
        }
        return res;
    }
    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        // TODO:
        
        
        Graph g= data.getGraph();
        Arc arc = null;
        List<Node> ListNodes=g.getNodes();
        List<Label> labels = new ArrayList<>();
        for(Node n : ListNodes){
            Label l = new Label(n,false,Double.POSITIVE_INFINITY,arc);
            labels.add(l);
        }
        
        
        Label s=labels.get(data.getOrigin().getId());
        s.setCout_realise(0);
        
        BinaryHeap<Label> tas = new BinaryHeap<>();
        tas.insert(s);
        while (ExistsNotMarked(labels)){
            Label x = tas.findMin();
            x.setMarque(true);
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
                        //if (tas.array.indexOf(courant.getSommet_courant())
                    }
                }
            }
        }

        
        return solution;
    }

}
