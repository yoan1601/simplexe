package main;
import algo.util.Contrainte;
import algo.util.Simplexe;
import algo.util.SimplexeFraction;
import algo.util.Simplexe2Phases;
import algo.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.math3.fraction.Fraction;

public class Main {
    public static void main(String[] args) {

        try {
            // =================================== SIMPLE ============================================
            
            /*int nbContraintes = 3;
            String [] comparaisons = { "<=", "<=", "<=" };
            Contrainte contrainte = new Contrainte(nbContraintes, comparaisons);
            
            Fraction[] l2 = { new Fraction(3) ,new Fraction(2) ,new Fraction(1) ,new Fraction(1), new Fraction(0) ,new Fraction(0) ,new Fraction(10) };
            Fraction[] l3 = { new Fraction(1) ,new Fraction(5) ,new Fraction(-2) ,new Fraction(0),new Fraction(1), new Fraction(0) ,new Fraction(8) };
            Fraction[] l4 = { new Fraction(2) ,new Fraction(3) ,new Fraction(3) ,new Fraction(0),new Fraction(0), new Fraction(1) ,new Fraction(20) };
            // ===============================================================================
            Fraction[] l1 = { new Fraction(2) ,new Fraction(3) ,new Fraction(7) ,new Fraction(0) ,new Fraction(0), new Fraction(0) ,new Fraction(0)};
            
            Fraction [][] tableau = {l2, l3, l4, l1};
            
            // ============================ DECISIONS =================================
            //RA MISY CONSTANTE DE ATAOVY "c" le cle
            String [] var_condition = { "x" , "y" , "z" };
            Fraction [] cond_values = { new Fraction(3), new Fraction(4), new Fraction(1) };
            HashMap<String, Fraction> condition = new HashMap<>();
            for (int i = 0; i < cond_values.length; i++) {
                Fraction cond_value = cond_values[i];
                condition.put(var_condition[i], cond_value);
            }
            // ============================ ECARTS ===================================
            String [] ecart = { "S1", "S2", "S3" };
            ArrayList<String>var_ecarts = new ArrayList<>();
            var_ecarts.addAll(Arrays.asList(ecart));
            // ============================ ARTIFICIELLE ===================================
            String [] art = {};
            ArrayList<String>var_art = new ArrayList<>();
            var_art.addAll(Arrays.asList(art));
            //================== HORS- BASE =================================
            String [] horsBase = { var_condition[0], var_condition[1], var_condition[2]};
            ArrayList<String> hb = new ArrayList<>();
            hb.addAll(Arrays.asList(horsBase));
            //================= BASE ====================================
            String [] base = { ecart[0], ecart[1], ecart[2] };
            ArrayList<String> b = new ArrayList<>();
            b.addAll(Arrays.asList(base));

            //================= ALL VARIABLES ====================================
            String [] allVar = Utils.concatenerTableaux(var_condition, ecart, art);
            Simplexe2Phases s2p = new Simplexe2Phases(contrainte, tableau, condition, true, false, hb, b, var_ecarts, var_art, allVar);
            s2p.getOptimum();*/
            
            //================================ 2 PHASES ===========================================
            
            int nbContraintes = 2;
            String [] comparaisons = { "<=", ">=" };
            Contrainte contrainte = new Contrainte(nbContraintes, comparaisons);
            
            Fraction[] l2 = { new Fraction(1) ,new Fraction(2) ,new Fraction(2) ,new Fraction(1), new Fraction(0) ,new Fraction(0) ,new Fraction(8,3) };
            Fraction[] l3 = { new Fraction(1) ,new Fraction(2) ,new Fraction(3) ,new Fraction(0),new Fraction(-1), new Fraction(1) ,new Fraction(7,3) };
            // ===============================================================================
            Fraction[] l1 = { new Fraction(-1) ,new Fraction(-2) ,new Fraction(-3) ,new Fraction(0) ,new Fraction(1), new Fraction(0) ,new Fraction(-7,3)};
            
            Fraction [][] tableau = {l2, l3, l1};
            
            // ============================ DECISIONS =================================
            //RA MISY CONSTANTE DE ATAOVY "c" le cle
            String [] var_condition = { "x1" , "x2" , "x3" };
            Fraction [] cond_values = { new Fraction(3), new Fraction(4), new Fraction(1) };
            HashMap<String, Fraction> condition = new HashMap<>();
            for (int i = 0; i < cond_values.length; i++) {
                Fraction cond_value = cond_values[i];
                condition.put(var_condition[i], cond_value);
            }
            // ============================ ECARTS ===================================
            String [] ecart = { "S1", "S2" };
            ArrayList<String>var_ecarts = new ArrayList<>();
            var_ecarts.addAll(Arrays.asList(ecart));
            // ============================ ARTIFICIELLE ===================================
            String [] art = {"t1"};
            ArrayList<String>var_art = new ArrayList<>();
            var_art.addAll(Arrays.asList(art));
            //================== HORS- BASE =================================
            String [] horsBase = { var_condition[0], var_condition[1], var_condition[2], ecart[1] };
            ArrayList<String> hb = new ArrayList<>();
            hb.addAll(Arrays.asList(horsBase));
            //================= BASE ====================================
            String [] base = { ecart[0], art[0] };
            ArrayList<String> b = new ArrayList<>();
            b.addAll(Arrays.asList(base));

            //================= ALL VARIABLES ====================================
            String [] allVar = Utils.concatenerTableaux(var_condition, ecart, art);
            Simplexe2Phases s2p = new Simplexe2Phases(contrainte, tableau, condition, true, true, hb, b, var_ecarts, var_art, allVar);
            s2p.getOptimum();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
