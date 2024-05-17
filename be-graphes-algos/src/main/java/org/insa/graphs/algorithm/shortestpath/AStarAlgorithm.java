package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    public LabelStar CreateLabel(Node n, ShortestPathData data){
        double dist_puits=n.getPoint().distanceTo(data.getDestination().getPoint());
        LabelStar l = new LabelStar(n,false,Double.POSITIVE_INFINITY,null, dist_puits);
        return l;
    }

}