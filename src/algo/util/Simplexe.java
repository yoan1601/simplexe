package algo.util;
import java.io.File;
import java.util.Scanner;

public class Simplexe {
    int dimension;
    String[] variables;
    float[] coeffExpression;
    int contraintes;
    String[] variableEcart;
    float[][] coeffContraintes;
    float[][] tableau;
    public Simplexe(int dimension,int contraintes){
        this.setDimension(dimension);
        this.setContraintes(contraintes);
        variables=new String[dimension];
        coeffExpression=new float[dimension];
        variableEcart=new String[this.contraintes];
        coeffContraintes=new float[this.contraintes][this.contraintes+this.dimension+1];
    }

    public Simplexe(String file,String separator) throws  Exception{
        this.setTableau(file,separator);
    }

    public void Initialise(){
        this.setVariables();
        this.setCoeffExpression();
        this.setVariableEcart();
        this.setCoeffContraintes();
        this.Print();
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
        System.out.println("Entrer le nom de vos variables:");
        Scanner scan=new Scanner(System.in);
        for (int i = 0; i < this.dimension; i++) {
            variables[i]=scan.nextLine();
        }
        this.variables = variables;
    }

    public float[] getCoeffExpression() {
        return coeffExpression;
    }

    public void setCoeffExpression() {
        System.out.println("Les coefficients:");
        Scanner scan=new Scanner(System.in);
        for (int i = 0; i < this.dimension; i++) {
            System.out.printf("a"+i);
            this.getCoeffExpression()[i]=Float.parseFloat(scan.nextLine());
        }
    }

    public String[] getVariableEcart() {
        return variableEcart;
    }

    public void setVariableEcart() {
        System.out.println("Entrer les variables d'ecarts:");
        Scanner scan=new Scanner(System.in);
        for (int i = 0; i < this.dimension; i++) {
            this.getVariableEcart()[i]=scan.nextLine();
        }

    }

    public int getContraintes() {
        return contraintes;
    }

    public void setContraintes(int contraintes) {
        this.contraintes = contraintes;
    }

    public float[][] getCoeffContraintes() {
        return coeffContraintes;
    }

    public void setCoeffContraintes() {
        System.out.println("Entrer les coefficients des contraintes:");
        Scanner scan=new Scanner(System.in);
        for (int i = 0; i < this.getCoeffExpression().length; i++) {
            float[] cont=new float[contraintes];
            for (int j = 0; j < this.contraintes; j++) {
                if (j==i){
                    cont[j]=1;
                }else {
                    cont[j]=0;
                }
            }
            System.out.println("Contrainte "+i);
            int o=0;
            for (o = 0; o < this.dimension; o++) {
                this.getCoeffContraintes()[i][o]=Float.parseFloat(scan.nextLine());
            }
            for (int j = o+1; j < cont.length; j++) {
                this.getCoeffContraintes()[i][j]=cont[j];
            }
            System.out.print("second membre:");
            this.getCoeffContraintes()[i][this.dimension+this.contraintes]=Float.parseFloat(scan.nextLine());
        }
    }
    public void setTableau(String file,String separator) throws Exception{
        File data=new File("dataFile.txt");
        Scanner scan=new Scanner(data);
        if (data.exists()){
            this.dimension=Integer.parseInt(scan.nextLine());
            this.contraintes=Integer.parseInt(scan.nextLine());
            this.variables=new String[dimension];
            this.variableEcart=new String[contraintes];
            this.setVariables();
            this.setVariableEcart();
            String expression= scan.nextLine();
            this.tableau=new float[this.contraintes+1][this.dimension+this.contraintes+1];
            int i=0;
            while (scan.hasNextLine()){
                String[] coeff=scan.nextLine().split(separator);
                for (int j = 0; j < coeff.length; j++) {
                    this.tableau[i][j]=Float.parseFloat(coeff[j]);
                }
                i+=1;
            }
            String[] exp=expression.split(separator);
            for (int j = 0; j < exp.length; j++) {
                this.tableau[i][j]=Float.parseFloat(exp[j]);
            }
            this.afficheTab();
        } else {
            throw new Exception("Fichier de données non trouvé");
        }
    }
    public void afficheTab(){
        for (int i = 0; i < this.tableau.length; i++) {
            for (int j = 0; j < this.tableau[0].length; j++) {
                System.out.print(this.tableau[i][j]+" ");
            }
            System.out.println();
        }
    }

    public int getIndiceMax(float[] ambany) throws Exception{
        int valiny=0;
        float ngeza=ambany[valiny];
        boolean misyPositif=false;
        for (int i = 0; i < ambany.length; i++) {
            if (ambany[i]>0){
                if (ambany[i]>ngeza){
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
        float petit=tableau[kely][this.dimension+this.contraintes]/tableau[kely][max];
        boolean misyPositif=false;
        for (int i = 0; i < tableau.length-1; i++) {
            if (tableau[i][max]>0){
                if (tableau[i][this.dimension+this.contraintes]/tableau[i][max]<petit){
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
        float intersection=tableau[min][max];
        for (int i = 0; i < tableau[min].length; i++) {
            tableau[min][i]=tableau[min][i]/intersection;
        }
    }
    public void GAUSS(int min,int max){
        for (int i = 0; i < tableau.length; i++) {
            float coeff=tableau[i][max];
            if (i!=min){
                for (int j = 0; j < tableau[0].length; j++) {
                    tableau[i][j]=tableau[i][j]-(coeff*tableau[min][j]);
                }
            }
        }
    }

    public float optimum() {
        try {
            float optimum = 0;
            int max = this.getIndiceMax(tableau[this.contraintes]);
            int min = this.getMin(max);
            String temp=this.getVariables()[max];
            this.getVariables()[max]=this.getVariableEcart()[min];
            this.getVariableEcart()[min]=temp;
            System.out.println("Max=" + max + " Min=" + min);
            transformPivot(min, max);
            GAUSS(min, max);
            afficheTab();
        } catch (Exception e){
            for (int i = 0; i <tableau.length-1 ; i++) {
                System.out.println(this.getVariableEcart()[i]+"="+tableau[i][this.dimension+this.contraintes]);
            }
            return tableau[this.contraintes][this.contraintes+this.dimension];
        }
        return this.optimum();
    }
}
