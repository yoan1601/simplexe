package algo.util;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.math3.fraction.Fraction;

public class SimplexeFraction {
    int dimension;
    String[] variablesHB;
    Contrainte contrainte;
    String[] variablesBase;
    Fraction[][] tableau;
    String[] varDispo = {"x", "y", "z", "t", "u", "v"} ;
    ArrayList<String> variablesEcart = new ArrayList<>();
    ArrayList<String> variablesArtificielle = new ArrayList<>();

    public SimplexeFraction(int dimension, Contrainte contrainte) throws Exception {
        this.setDimension(dimension);
        this.setContrainte(contrainte);
        variablesHB=new String[dimension];
        this.variablesHB=new String[dimension];
        this.variablesBase=new String[this.getContrainte().getNbContrainte()];
        this.tableau=new Fraction[this.getContrainte().getNbContrainte()+1][this.dimension+this.getContrainte().getNbContrainte()+1];
    }

    public void setTableau(Fraction [][] lignes) throws Exception{
        //x ,y, z, .... 
        this.setVariablesHB();
        //S1 ,S2, .... ,Sn 
        this.setVariablesBase();
        
        Fraction [][] generated = generateWithCoeffByComparaison(lignes);
        
        for (int i = 0; i < this.tableau.length; i++) {
            tableau[i] = generated[i];
        }
    }
    
    //rearangement des coeff selon comparaison
    public Fraction [][] generateWithCoeffByComparaison(Fraction [][] lignes) {
        int nbCoeffSup = nbCoeffSupplementaireByComparaison();
        Fraction [][] generated = new Fraction[lignes.length][lignes[0].length + nbCoeffSup];
        
        //completion tableau initial fa tsy ajoutena le col farany
        for (int i = 0; i < lignes.length; i++) {
            for (int j = 0; j < lignes[0].length - 1; j++) {
                generated[i][j] = lignes[i][j];
            }
        }
        
        //completion derniere colonne -> 2nd membre
        int derniereCol = lignes[0].length - 1 + nbCoeffSup;
        for (int i = 0; i < lignes.length; i++) {
            generated[i][derniereCol] = lignes[i][lignes[0].length - 1];
        }
        
        int idColDepart = lignes[0].length - 1;
        
        //completion en 0 des restes
        for (int i = 0; i < lignes.length; i++) {
            for (int j = idColDepart; j < idColDepart + nbCoeffSup; j++) {
                generated[i][j] = new Fraction(0);   
            }
        }
        
        //completion des coeff sup
        int indiceCol = idColDepart;
        for (int i = 0; i < lignes.length - 1; i++) { 
            if(contrainte.getComparaisons()[i].trim().equalsIgnoreCase("<=")) {
                generated[i][indiceCol] = new Fraction(1);
            }
            else if(contrainte.getComparaisons()[i].trim().equalsIgnoreCase(">=")) {
                generated[i][indiceCol] = new Fraction(-1);
            }
            indiceCol++;
        }
        
        return generated;
    }
    
    public int nbCoeffSupplementaireByComparaison() {
        int nb = 0;
        for (String comparaison : contrainte.getComparaisons()) {
            //inferieur ou egal
            if(comparaison.trim().equalsIgnoreCase("<=")) {
                nb = nb + 1; //Sn
                variablesEcart.add("S"+nb);
            }
            else if(comparaison.trim().equalsIgnoreCase("=")) {
                nb = nb + 1; //An
                variablesArtificielle.add("A"+nb);
            }
            else if(comparaison.trim().equalsIgnoreCase(">=")) {
                nb = nb + 1; //Sn
                variablesEcart.add("S"+nb);
                variablesArtificielle.add("A"+nb); //meme indice
                nb = nb + 1; //An
            }
        }
        return nb;
    }
    
    public void showVariablesEcart() {
        System.out.println("showVariablesEcart");
        for (int i = 0; i < variablesEcart.size(); i++) {
            String get = variablesEcart.get(i);
            System.out.println(get);
        }
    }
    
    public void showVariablesArtificielle() {
        System.out.println("showVariablesArtificielle");
        for (int i = 0; i < variablesArtificielle.size(); i++) {
            String get = variablesArtificielle.get(i);
            System.out.println(get);
        }
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        if (dimension>=1)
        this.dimension = dimension;
    }

    public String[] getVariablesHB() {
        return variablesHB;
    }

    public void setVariablesHB() {
        String[] variablesHB=new String[this.dimension];
        
        for (int i = 0; i < this.dimension; i++) {
            variablesHB[i]=varDispo[i];
        }
        this.variablesHB = variablesHB;
    }


    public String[] getVariablesBase() {
        return variablesBase;
    }

    public void setVariablesBase() {
        for (int i = 0; i < this.dimension; i++) {
            this.getVariablesBase()[i]="S"+(i+1);
        }
    }

    public Contrainte getContrainte() {
        return contrainte;
    }

    public void setContrainte(Contrainte contrainte) {
        this.contrainte = contrainte;
    }

    public String[] getVarDispo() {
        return varDispo;
    }

    public void setVarDispo(String[] varDispo) {
        this.varDispo = varDispo;
    }
    
    public void afficheTab(){
        for (Fraction[] tableau1 : this.tableau) {
            for (int j = 0; j < this.tableau[0].length; j++) {
                System.out.print(tableau1[j] + " ");
            }
            System.out.println();
        }
    }

    public int getIndiceMax(Fraction[] ambany) throws Exception{
        int valiny=0;
        Fraction ngeza=ambany[valiny];
        boolean misyPositif=false;
        for (int i = 0; i < ambany.length; i++) {
            if (ambany[i].floatValue()>0){
                if (ambany[i].compareTo(ngeza) > 0){
                    valiny=i;
                    ngeza=ambany[i];
                }
                misyPositif=true;
            }
        }
        if (misyPositif==false)
            throw new Exception("L'optimum est "+tableau[this.getContrainte().getNbContrainte()][this.getContrainte().getNbContrainte()+this.dimension]);
        return valiny;
    }
    public int getMin(int max) throws Exception{
        int kely=0;
        Fraction petit=tableau[kely][this.dimension+this.getContrainte().getNbContrainte()].divide(tableau[kely][max]);
        boolean misyPositif=false;
        for (int i = 0; i < tableau.length-1; i++) {
            if (tableau[i][max].floatValue()>0){
                if ((tableau[i][this.dimension+this.getContrainte().getNbContrainte()].divide(tableau[i][max])).compareTo(petit) < 0){
                    kely=i;
                    petit=tableau[i][max];
                }
                misyPositif=true;
            }
        }
        if (misyPositif==false)
            throw new Exception("Pas de solution");
        return kely;
    }

    public void transformPivot(int min,int max){
        Fraction intersection=tableau[min][max];
        for (int i = 0; i < tableau[min].length; i++) {
            tableau[min][i]=tableau[min][i].divide(intersection);
        }
    }
    public void GAUSS(int min,int max){
        for (int i = 0; i < tableau.length; i++) {
            Fraction coeff=tableau[i][max];
            if (i!=min){
                for (int j = 0; j < tableau[0].length; j++) {
                    //tableau[i][j]=tableau[i][j]-(coeff*tableau[min][j]);
                    tableau[i][j]=tableau[i][j].add((tableau[min][j].multiply(coeff)).multiply(-1));
                }
            }
        }
    }

    public Fraction optimum() {
        try {
            Fraction optimum = new Fraction(0);
            //derniere ligne du tableau misy ny coeff fonction cible -> indice : nb contraintes 
            // maka ny INDICE COLONNE coeff max ao @ derniere ligne
            int max = this.getIndiceMax(tableau[this.getContrainte().getNbContrainte()]);
            
            // maka ny INDICE LIGNE a partir anle indice colonne coeff max azo teo ambony tq [elt derniere colonne @iny ligne iny / elt @iny colonne iny] est le min apres comparaison ligne par ligne suivant cette colonne  
            int min = this.getMin(max);

            //maka anle variable vaovao hiditra ao am base
            String temp=this.getVariablesHB()[max];

            // echange entre variable miditra ao am base sy le miala
            this.getVariablesHB()[max]=this.getVariablesBase()[min];
            this.getVariablesBase()[min]=temp;

            System.out.println("Max=" + max + " Min=" + min);
            
            //transforme le pivot en 1 et transforme toute la ligne mifanandrify @iny par consequent
            transformPivot(min, max);

            //rendre 0 tous les valeurs amle colonne anle pivot (a part anle pivot) et transforme toute la ligne mifanandrify @iny par consequent
            GAUSS(min, max);

            afficheTab();
        } catch (Exception e){
            //rehefa negatif daholo le coeff derniere ligne
            for (int i = 0; i <tableau.length-1 ; i++) {
                System.out.println(this.getVariablesBase()[i]+"="+tableau[i][this.dimension+this.getContrainte().getNbContrainte()]);
            }
            //System.out.println("optimum en fraction "+tableau[this.getContrainte().getNbContrainte()][this.getContrainte().getNbContrainte()+this.dimension]);
            return tableau[this.getContrainte().getNbContrainte()][this.getContrainte().getNbContrainte()+this.dimension];
        }
        //boucle jusqu'a meet the condition
        return this.optimum();
    }
}
