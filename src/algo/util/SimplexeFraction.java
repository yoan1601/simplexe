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
    int M = 10000;
    int idLigneFarany = 0;
    int limitIdLigneAngalanaMin = 0;

    public SimplexeFraction(int dimension, Contrainte contrainte) throws Exception {
        this.setDimension(dimension);
        this.setContrainte(contrainte);
        variablesHB=new String[dimension];
        this.variablesHB=new String[dimension];
        this.variablesBase=new String[this.getContrainte().getNbContrainte()];
        this.tableau=new Fraction[this.getContrainte().getNbContrainte()+1][this.dimension+this.getContrainte().getNbContrainte()+1];
    }
    
    //rearangement des coeff selon comparaison
    public Fraction [][] generateWithCoeffByComparaison(Fraction [][] lignes) {
        int nbCoeffSup = nbCoeffSupplementaireByComparaison();
        
        int nbLignes = lignes.length;
        //si existe variable artificielle , MISY ! IO AM VOLOANY IO
        if(!variablesArtificielle.isEmpty()) {
            nbLignes++;
        }
        
        Fraction [][] generated = new Fraction[nbLignes][lignes[0].length + nbCoeffSup];
        
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
        
        //completion des coeff variables artificielle
        indiceCol--;
        for (int i = 0; i < lignes.length - 1; i++) { 
            //System.out.println("getVariableArtificielle("+i+") "+getVariableArtificielle(i));
            if(getVariableArtificielle(i) != null) {
                generated[i][indiceCol] = new Fraction(1);
                //System.out.println("generated["+i+"]["+indiceCol+"] "+generated[i][indiceCol]);
            }
            indiceCol++;
        }
        
        //si existe variable artificielle MISY ! IO AM VOLOANY IO
        indiceCol -= lignes.length - 1;
        limitIdLigneAngalanaMin = lignes.length - 2;
        
        if(!variablesArtificielle.isEmpty()) {
            
            limitIdLigneAngalanaMin = lignes.length - 3;
            
            for (int i = 0; i < lignes.length - 1; i++) { 
                //System.out.println("getVariableArtificielle("+i+") "+getVariableArtificielle(i));
                if(getVariableArtificielle(i) != null) {
                    generated[i][indiceCol] = new Fraction(1);
                    //System.out.println("generated["+i+"]["+indiceCol+"] "+generated[i][indiceCol]);
                }
                indiceCol++;
            }
            
            //remplissage de la derniere ligne ajoutee si existe var artificielle
            //jusqu'a avant derniere colonne
            Fraction coeff;
            int idCol = 0;
            for (int i = 0; i < generated[0].length - 1; i++) {
                // coeff = coeff AD ligne + coeff ligne misy A * M
                coeff = constrValM(i, generated, 1);
                //System.out.println("coeff "+coeff);
                generated[nbLignes - 1][i] = coeff;
                idCol++;
            }
            
            //derniere colonne
            // coeff = coeff AD ligne - coeff ligne misy A * M
            coeff = constrValM(idCol, generated, -1);
            generated[nbLignes - 1][idCol] = coeff;
            
        }
        return generated;
    }
    
    public Fraction constrValM(int idCol, Fraction[][] generated, int signe) {
        int idADLigne = generated.length - 2; 
        //coeff AD ligne
        Fraction val = generated[idADLigne][idCol];
        
        Fraction toAdd;
        for (int i = 0; i < idADLigne ; i++) {
            if(getVariableArtificielle(i) != null) {
                toAdd = generated[i][idCol].multiply(M).multiply(signe);
                val = val.add(toAdd);
            }
        }
        return val;
    }
    
    public boolean isThereVarArtificielleInBase() {
        for (String var_base : variablesBase) {
            if(var_base.contains("A") == true) return true;
        }
        return false;
    }

    public void setTableau(Fraction [][] lignes) throws Exception{
        //x ,y, z, .... 
        this.setVariablesHB();

        
        Fraction [][] generated = generateWithCoeffByComparaison(lignes);

        //S1 ,S2, .... ,Sn ou A1, A2, ..., An
        this.setVariablesBase();        
        
        /*for (int i = 0; i < this.tableau.length; i++) {
            tableau[i] = generated[i];
        }*/
        
        tableau = generated;
        
        idLigneFarany = tableau.length - 1;
    }
    
    public int nbCoeffSupplementaireByComparaison() {
        int nb = 0;
        int indice = 1;
        for (String comparaison : contrainte.getComparaisons()) {
            //inferieur ou egal
            if(comparaison.trim().equalsIgnoreCase("<=")) {
                nb = nb + 1; //Sn
                variablesEcart.add("S"+indice);
            }
            else if(comparaison.trim().equalsIgnoreCase("=")) {
                nb = nb + 1; //An
                variablesArtificielle.add("A"+indice);
            }
            else if(comparaison.trim().equalsIgnoreCase(">=")) {
                nb = nb + 1; //Sn
                variablesEcart.add("S"+indice);
                variablesArtificielle.add("A"+indice); //meme indice
                nb = nb + 1; //An
            }
            indice++;
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
        String var_art;
        String var_ecart;
        for (int i = 0; i < this.dimension; i++) {
            var_art = getVariableArtificielle(i);
            if(var_art != null) {
                this.getVariablesBase()[i] = var_art;
            }
            else {
                var_ecart = getVariableEcart(i);
                this.getVariablesBase()[i] = var_ecart;
            }
        }
    }
    
    public String getVariableEcart(int indice) { //0,1,2...
        for (int i = 0; i < variablesEcart.size(); i++) {
            String get = variablesEcart.get(i);
            if(get.equals("S"+(indice+1))) {
                return get;
            }
        }
        return null;
    }
    
    public String getVariableArtificielle(int indice) { //0,1,2...
        for (int i = 0; i < variablesArtificielle.size(); i++) {
            String get = variablesArtificielle.get(i);
            //System.out.println("get "+get);
            //System.out.println("\"A\"+indice+1 "+"A"+indice+1);
            if(get.equalsIgnoreCase("A"+(indice+1))) {
                return get;
            }
        }
        return null;
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
     public void afficheTab(Fraction [][] tab){
        for (Fraction[] tableau1 : tab) {
            for (int j = 0; j < tab[0].length; j++) {
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
        int idColFarany = tableau[0].length - 1;
        Fraction petit=tableau[kely][idColFarany].divide(tableau[kely][max]);
        boolean misyPositif=false;
        for (int i = 0; i <= limitIdLigneAngalanaMin; i++) {
            if (tableau[i][max].floatValue()>0){
                if ((tableau[i][idColFarany].divide(tableau[i][max])).compareTo(petit) < 0){
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
    
    public Fraction[][] delColVarArtificielle() {
        int nbLigne = tableau.length;
        int nbCol = tableau[0].length - variablesArtificielle.size();
        Fraction [][] rep = new Fraction[nbLigne][nbCol];
        
        for (int i = 0; i < nbLigne; i++) {
            for (int j = 0; j < nbCol - 1; j++) { //col farany mbola tsy traitena
                rep[i][j] = tableau[i][j];
            }
        }
        
        //tratement colonne farany
        for (int i = 0; i < nbLigne; i++) {
            rep[i][rep[0].length -1] = tableau[i][tableau[0].length - 1];
        }
        
        /*System.out.println("rep.length * rep[0].length"+rep.length +"*"+ rep[0].length);
        System.out.println("tableau.length * tableau[0].length"+tableau.length +"*"+ tableau[0].length);
        afficheTab(rep);*/
        return rep;
    }

    public Fraction optimum() {
        try {
            Fraction optimum = new Fraction(0);
            //derniere ligne du tableau misy ny coeff fonction cible -> indice : nb contraintes 
            // maka ny INDICE COLONNE coeff max ao @ derniere ligne
            //System.out.println("isThereVarArtificielleInBase() "+isThereVarArtificielleInBase());
            if(isThereVarArtificielleInBase() == false && !variablesArtificielle.isEmpty()) {
                //miakatra eo amle ligne ambony
                idLigneFarany--; 
                //asorina ny col A rehetra
                tableau = delColVarArtificielle();
                limitIdLigneAngalanaMin = tableau.length - 2;
            }
            int max = this.getIndiceMax(tableau[idLigneFarany]);  
            /*System.out.println("max "+max);
            System.out.println("idLigneFarany "+idLigneFarany);*/
            
            // maka ny INDICE LIGNE a partir anle indice colonne coeff max azo teo ambony tq [elt derniere colonne @iny ligne iny / elt @iny colonne iny] est le min apres comparaison ligne par ligne suivant cette colonne  
            int min = this.getMin(max);
            System.out.println("min "+min);

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
            //System.out.println("GAUSS Ok");

            afficheTab();
        } catch (Exception e){
            //rehefa negatif daholo le coeff derniere ligne
            //System.out.println("isThereVarArtificielleInBase() "+isThereVarArtificielleInBase());
            //afficheTab();
            /*System.out.println("tableau.length-1 "+(tableau.length -1 ));
            System.out.println("tableau.length "+tableau.length);*/
            for (int i = 0; i < variablesBase.length ; i++) {
                System.out.println(this.getVariablesBase()[i]+"="+tableau[i][tableau[0].length - 1]);
            }
            //System.out.println("optimum en fraction "+tableau[this.getContrainte().getNbContrainte()][this.getContrainte().getNbContrainte()+this.dimension]);
            return tableau[idLigneFarany][tableau[0].length - 1];
            //return tableau[idLigneFarany][tableau[0].length - 1];
        }
        //boucle jusqu'a meet the condition
        return this.optimum();
    }
}
