/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algo.util;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.math3.fraction.Fraction;

/**
 *
 * @author yoan
 */
public class SimplexePhase1 extends Simplexe2Phases {
    
    public SimplexePhase1(Contrainte c, Fraction[][] t,HashMap<String, Fraction> condition, boolean ismax, boolean use2phases, ArrayList<String> hb, ArrayList<String> b, ArrayList<String>var_ecarts,ArrayList<String>var_art, String[] allVar) {
        super(c, t, condition,ismax, use2phases, hb, b,var_ecarts, var_art, allVar);
    }
    
    @Override
    public Fraction getOptimum() { //tonga de probleme de min fona
        try {
            
            if(tableau[tableau.length - 1][tableau[0].length - 1] == new Fraction(0)) throw new Exception("optimum pahse 1 ok");
            
            int idColMax = 0;
            Fraction [] derniereLigne = tableau[tableau.length - 1];

            //mijery ny max @elt negatif @derniere ligne , ra tsisy < 0 de exception
            idColMax = getIdMax_simplexeMin(derniereLigne);

            //maka anle min (elt derniere col / elt col max)
            int idLigneMin = getIdMinCol(idColMax);
            
            updateBase(idLigneMin, idColMax);
            
            transformeLignePivot(idLigneMin, idColMax); //mamadika pivot 1 de manova ny elt @iny ligne iny rehetra
            
            gaussJordan(idLigneMin, idColMax); //annuler tous les elts col avec le pivot
            
            afficheTab();
            
            return this.getOptimum();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            constructBaseFinale();
            System.out.println("optimum simplexe phase 1 : "+tableau[tableau.length - 1][tableau[0].length - 1].multiply(-1));
            return tableau[tableau.length - 1][tableau[0].length - 1].multiply(-1);
        }
    }
    
}
