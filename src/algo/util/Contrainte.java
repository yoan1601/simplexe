/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algo.util;

/**
 *
 * @author yoan
 */
public class Contrainte {
    int nbContrainte;
    String [] comparaisons;

    public Contrainte(int nbContrainte, String[] comparaisons) {
        this.nbContrainte = nbContrainte;
        this.comparaisons = comparaisons;
    }
    

    public int getNbContrainte() {
        return nbContrainte;
    }

    public void setNbContrainte(int nbContrainte) {
        this.nbContrainte = nbContrainte;
    }

    public String[] getComparaisons() {
        return comparaisons;
    }

    public void setComparaisons(String[] comparaisons) {
        this.comparaisons = comparaisons;
    }
    
    
}
