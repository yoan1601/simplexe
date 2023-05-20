package algo.util;
import java.io.File;
import java.util.Scanner;
import org.apache.commons.math3.fraction.Fraction;

public class SimplexeFraction {
    int dimension;
    String[] variables;
    Fraction[] coeffExpression;
    int contraintes;
    String[] variableEcart;
    Fraction[][] coeffContraintes;
    Fraction[][] tableau;
    String[] varDispo = {"x", "y", "z", "t", "u", "v"} ;

    public SimplexeFraction(int dimension, int contraintes) throws Exception {
        this.setDimension(dimension);
        this.setContraintes(contraintes);
        variables=new String[dimension];
        coeffExpression=new Fraction[dimension];
        variableEcart=new String[this.contraintes];
        coeffContraintes=new Fraction[this.contraintes][this.contraintes+this.dimension+1];
        this.variables=new String[dimension];
        this.variableEcart=new String[contraintes];
        //x ,y, z, .... 
        this.setVariables();
        //S1 ,S2, .... ,Sn 
        this.setVariableEcart();
        this.tableau=new Fraction[this.contraintes+1][this.dimension+this.contraintes+1];
    }
    
    


    public void setTableau(Fraction [][] lignes) throws Exception{
        for (int i = 0; i < this.tableau.length; i++) {
            tableau[i] = lignes[i];
        }
    }


    public void Print(){
        String expression="f=";
        for (int i = 0; i <this.dimension ; i++) {
            if (i<this.dimension-1){
                expression+="("+this.getCoeffExpression()[i]+")"+this.getVariables()[i]+"+";
            }else {
                expression+="("+this.getCoeffExpression()[i]+")"+this.getVariables()[i];
            }
        }
        System.out.println(expression);
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        if (dimension>=1)
        this.dimension = dimension;
    }

    public String[] getVariables() {
        return variables;
    }

    public void setVariables() {
        String[] variables=new String[this.dimension];
        //System.out.println("Entrer le nom de vos variables:");
        //Scanner scan=new Scanner(System.in);
        // for (int i = 0; i < this.dimension; i++) {
        //     variables[i]=scan.nextLine();
        // }
        
        for (int i = 0; i < this.dimension; i++) {
            variables[i]=varDispo[i];
        }
        this.variables = variables;
    }

    public Fraction[] getCoeffExpression() {
        return coeffExpression;
    }


    public String[] getVariableEcart() {
        return variableEcart;
    }

    public void setVariableEcart() {
        //System.out.println("Entrer les variables d'ecarts:");
        // Scanner scan=new Scanner(System.in);
        // for (int i = 0; i < this.dimension; i++) {
        //     this.getVariableEcart()[i]=scan.nextLine();
        // }
        for (int i = 0; i < this.dimension; i++) {
            this.getVariableEcart()[i]="S"+(i+1);
        }
    }

    public int getContraintes() {
        return contraintes;
    }

    public void setContraintes(int contraintes) {
        this.contraintes = contraintes;
    }

    public Fraction[][] getCoeffContraintes() {
        return coeffContraintes;
    }
    
    public void afficheTab(){
        for (int i = 0; i < this.tableau.length; i++) {
            for (int j = 0; j < this.tableau[0].length; j++) {
                System.out.print(this.tableau[i][j]+" ");
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
            throw new Exception("L'optimum est "+tableau[this.contraintes][this.contraintes+this.dimension]);
        return valiny;
    }
    public int getMin(int max) throws Exception{
        int kely=0;
        Fraction petit=tableau[kely][this.dimension+this.contraintes].divide(tableau[kely][max]);
        boolean misyPositif=false;
        for (int i = 0; i < tableau.length-1; i++) {
            if (tableau[i][max].floatValue()>0){
                if ((tableau[i][this.dimension+this.contraintes].divide(tableau[i][max])).compareTo(petit) < 0){
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
            int max = this.getIndiceMax(tableau[this.contraintes]);
            
            // maka ny INDICE LIGNE a partir anle indice colonne coeff max azo teo ambony tq [elt derniere colonne @iny ligne iny / elt @iny colonne iny] est le min apres comparaison ligne par ligne suivant cette colonne  
            int min = this.getMin(max);

            //maka anle variable vaovao hiditra ao am base
            String temp=this.getVariables()[max];

            // echange entre variable miditra ao am base sy le miala
            this.getVariables()[max]=this.getVariableEcart()[min];
            this.getVariableEcart()[min]=temp;

            System.out.println("Max=" + max + " Min=" + min);
            
            //transforme le pivot en 1 et transforme toute la ligne mifanandrify @iny par consequent
            transformPivot(min, max);

            //rendre 0 tous les valeurs amle colonne anle pivot (a part anle pivot) et transforme toute la ligne mifanandrify @iny par consequent
            GAUSS(min, max);

            afficheTab();
        } catch (Exception e){
            //rehefa negatif daholo le coeff derniere ligne
            for (int i = 0; i <tableau.length-1 ; i++) {
                System.out.println(this.getVariableEcart()[i]+"="+tableau[i][this.dimension+this.contraintes]);
            }
            //System.out.println("optimum en fraction "+tableau[this.contraintes][this.contraintes+this.dimension]);
            return tableau[this.contraintes][this.contraintes+this.dimension];
        }
        //boucle jusqu'a meet the condition
        return this.optimum();
    }
}
