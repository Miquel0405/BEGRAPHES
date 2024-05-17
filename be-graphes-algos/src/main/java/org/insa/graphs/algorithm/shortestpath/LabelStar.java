package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{
    
    private double cout_destination;
    public LabelStar(Node sommet_courant,boolean marque,double cout_realise,Arc pere, double cout_destination){
        super(sommet_courant,marque,cout_realise,pere);
        this.cout_destination=cout_destination;
    }



    public double getTotalCost(){
        return getCost()+this.cout_destination;
    }
}
