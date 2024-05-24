package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Node;
public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    public LabelStar CreateLabel(Node n, ShortestPathData data){
        AbstractInputData.Mode m=data.getMode();
        double cout_estime=0;
        if (m==AbstractInputData.Mode.LENGTH){
            cout_estime=n.getPoint().distanceTo(data.getDestination().getPoint());
        }else if (m==AbstractInputData.Mode.TIME){
            if(data.getGraph().getGraphInformation().hasMaximumSpeed()){
                cout_estime=(n.getPoint().distanceTo(data.getDestination().getPoint()))/(data.getGraph().getGraphInformation().getMaximumSpeed());
            }else{
                cout_estime=(n.getPoint().distanceTo(data.getDestination().getPoint()))/130;
            }
        }
        LabelStar l = new LabelStar(n,false,Double.POSITIVE_INFINITY,null, cout_estime);
        return l;
    }

}