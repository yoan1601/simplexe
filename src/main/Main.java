package main;
import algo.util.Contrainte;
import algo.util.Simplexe;
import algo.util.SimplexeFraction;
import algo.util.Simplexe2Phases;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.math3.fraction.Fraction;

public class Main {
    public static void main(String[] args) {

        try {
            /*int dimension = 3;
            int nbContraintes = 2;
            String [] comparaisons = { "<=", ">=" };
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
            System.out.println("L'otimum est "+optimum);*/
            
            int dimension = 3;
            int nbContraintes = 2;
            String [] comparaisons = { "<=", ">=" };
            Contrainte contrainte = new Contrainte(nbContraintes, comparaisons);
            
            Fraction[] l2 = { new Fraction(1) ,new Fraction(2) ,new Fraction(2) ,new Fraction(1), new Fraction(0) ,new Fraction(0) ,new Fraction(8,3) };
            Fraction[] l3 = { new Fraction(1) ,new Fraction(2) ,new Fraction(3) ,new Fraction(0),new Fraction(-1), new Fraction(1) ,new Fraction(7,3) };
            
            Fraction[] l1 = { new Fraction(-1) ,new Fraction(-2) ,new Fraction(-3) ,new Fraction(0) ,new Fraction(1), new Fraction(0) ,new Fraction(-7,3)};
            
            Fraction [][] tableau = {l2, l3, l1};
            
            HashMap<String, Fraction> condition = new HashMap<>();
            condition.put("x1", new Fraction(3));condition.put("x2", new Fraction(4));condition.put("x3", new Fraction(1));
            ArrayList<String> hb = new ArrayList<>();
            hb.add("x1"); hb.add("x2"); hb.add("x3");hb.add("S2");
            ArrayList<String> b = new ArrayList<>();
            b.add("S1"); b.add("t1");
            ArrayList<String>var_ecarts = new ArrayList<>();
            var_ecarts.add("S1"); var_ecarts.add("S2");
            ArrayList<String>var_art = new ArrayList<>();
            var_ecarts.add("t1");
            String [] allVar = { "x1", "x2", "x3", "S1", "S2", "t1" };
            Simplexe2Phases s2p = new Simplexe2Phases(contrainte, tableau, condition, true, true, hb, b, var_ecarts, var_art, allVar);
            s2p.getOptimum();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
