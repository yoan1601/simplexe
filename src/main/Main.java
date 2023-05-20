package main;
import algo.util.Contrainte;
import algo.util.Simplexe;
import algo.util.SimplexeFraction;
import org.apache.commons.math3.fraction.Fraction;

public class Main {
    public static void main(String[] args) {

        try {
            int dimension = 3;
            int nbContraintes = 3;
            String [] comparaisons = { "<=", "<=", "<=" };
            Contrainte contrainte = new Contrainte(nbContraintes, comparaisons);
            
            SimplexeFraction simplexe=new SimplexeFraction(dimension, contrainte);
            //contraintes
            // 3 2 1 1 0 0 10
            // 1 5 -2 0 1 0 8
            // 2 3 3 0 0 1 20
            //Fraction[] l2 = { new Fraction(3) ,new Fraction(2) ,new Fraction(1) ,new Fraction(1) ,new Fraction(0) ,new Fraction(0) ,new Fraction(10) };
            //Fraction[] l3 = { new Fraction(1) ,new Fraction(5) ,new Fraction(-2) ,new Fraction(0) ,new Fraction(1) ,new Fraction(0) ,new Fraction(8) };
            //Fraction[] l4 = { new Fraction(2) ,new Fraction(3) ,new Fraction(3) ,new Fraction(0) ,new Fraction(0) ,new Fraction(1) ,new Fraction(20) };
            Fraction[] l2 = { new Fraction(3) ,new Fraction(2) ,new Fraction(1) ,new Fraction(10) };
            Fraction[] l3 = { new Fraction(1) ,new Fraction(5) ,new Fraction(-2) ,new Fraction(8) };
            Fraction[] l4 = { new Fraction(2) ,new Fraction(3) ,new Fraction(3) ,new Fraction(20) };
            //fonction objectif
            // 2 3 7 0 0 0 0
            //Fraction[] l1 = { new Fraction(2) ,new Fraction(3) ,new Fraction(7) ,new Fraction(0) ,new Fraction(0) ,new Fraction(0) ,new Fraction(0) };
            Fraction[] l1 = { new Fraction(2) ,new Fraction(3) ,new Fraction(7) ,new Fraction(0) };
            Fraction[][] lignes = {l2, l3, l4, l1};
            
            simplexe.setTableau(lignes);
            
            simplexe.showVariablesEcart();
            simplexe.showVariablesArtificielle();
            
            simplexe.afficheTab();
            
            Fraction optimum = simplexe.optimum();
            System.out.println("L'otimum est "+optimum);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
