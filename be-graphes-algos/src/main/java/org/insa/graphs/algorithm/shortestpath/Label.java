package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{
    private Node sommet_courant;
    private boolean marque;
    private double cout_realise;
    private Arc pere;

    public Label(Node sommet_courant,boolean marque,double cout_realise,Arc pere){
        this.cout_realise=cout_realise;
        this.marque=marque;
        this.sommet_courant=sommet_courant;
        this.pere=pere;
    }

    public Node getSommet_courant() {
        return sommet_courant;
    }

    public boolean isMarque() {
        return marque;
    }

    public double getCout_realise() {
        return cout_realise;
    }

    public Arc getPere() {
        return pere;
    }

    public void setSommet_courant(Node sommet_courant) {
        this.sommet_courant = sommet_courant;
    }

    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    public void setCout_realise(double cout_realise) {
        this.cout_realise = cout_realise;
    }

    public void setPere(Arc pere) {
        this.pere = pere;
    }

    public double getCost(){
        return this.cout_realise;
    }

    public int compareTo(Label l){
        if (this.cout_realise==l.cout_realise){
            return 0;
        }else if(this.cout_realise>l.cout_realise){
            return 1;
        }else{
            return -1;
        }
    }
}
