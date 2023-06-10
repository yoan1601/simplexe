/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algo.util;
import java.util.ArrayList;
import org.apache.commons.math3.fraction.Fraction;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author yoan
 */
public class Simplexe2Phases {
    Fraction [][] tableau;
    Contrainte contrainte;
    HashMap<String, Fraction> varDecision = new HashMap<>();
    ArrayList<String> varEcart = new ArrayList<>();
    ArrayList<String> varArtificielle = new ArrayList<>();
    ArrayList<String> base = new ArrayList<>();
    ArrayList<String> horsBase = new ArrayList<>();
    Fraction optimum; 
    boolean isMax = true;
    LinkedHashMap<String, Fraction> baseFinale = new LinkedHashMap<>();
    boolean use2phases = false;
    String [] allVariables;
    
    void setTableauAfterPhase1(Fraction [][] tab) {
        tableau = new Fraction[tab.length][varDecision.size() + varEcart.size() + 1]; //+1 amle col farany fa tsisy artificielle
        int idDebutArtificielle = varDecision.size() + varEcart.size();
        int idFinArtificielle = varDecision.size() + varEcart.size() + varArtificielle.size() - 1;
        //System.out.println("idDebutArtificielle "+idDebutArtificielle);
        //System.out.println("idFinArtificielle "+idFinArtificielle);
        int idCol = 0;
        for (int i = 0; i < tab.length; i++) {
            Fraction[] ligne = tab[i];
            idCol = 0;
            for (int j = 0; j < ligne.length; j++) {
                if(j < idDebutArtificielle || j > idFinArtificielle) {
                    Fraction elt = ligne[j];
                    tableau[i][idCol] = elt;
                    idCol++;
                }
            }
        }
    }
    
    void transfert(SimplexePhase1 sp1) {
        //le var artificielle tsy ampidirina tsony
        setTableauAfterPhase1(sp1.getTableau());
        setBase(sp1.getBase());
        setHorsBase(sp1.getHorsBase());
        popArtificielleInHB();
        setBaseFinale(sp1.getBaseFinale());
    }
    
    void popArtificielleInHB() {
        ArrayList<Integer> allIdArtificielle = getAllIdArtificielle(horsBase);
        ArrayList<String> newHorsBase = new ArrayList<>();
        newHorsBase = horsBase;
        for (int i = 0; i < horsBase.size(); i++) {
            String get = horsBase.get(i);
            for (int j = 0; j < allIdArtificielle.size(); j++) {
                Integer get1 = allIdArtificielle.get(j);
                if(get1 == i) newHorsBase.remove(i);
            }
        }
        setHorsBase(newHorsBase);
    }
    
    ArrayList<Integer> getAllIdArtificielle(ArrayList<String> l) {
        ArrayList<Integer> rep = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            String get = l.get(i);
            if(get.contains("t") || get.contains("T") || get.contains("A") || get.contains("a")) {
                rep.add(i);
            }
        }
        return rep;
    }
    
    public Simplexe2Phases(Contrainte c, Fraction[][] t, HashMap<String, Fraction> condition,boolean ismax, boolean use2phases,ArrayList<String> hb, ArrayList<String> b, ArrayList<String>var_ecarts,ArrayList<String>var_art, String[] allVar ) {
        setContrainte(c);
        setTableau(t);
        isMax = ismax;
        if(ismax) System.out.println("maximisation");
        else System.out.println("minimisation");
        setBase(b);
        setHorsBase(hb);
        this.use2phases = use2phases;
        setVarDecision(condition);
        setVarEcart(var_ecarts);
        setVarArtificielle(var_art);
        setAllVariables(allVar);
    }
    
    public Simplexe2Phases(Contrainte c, Fraction[][] t,HashMap<String, Fraction> condition, boolean ismax, ArrayList<String> hb, ArrayList<String> b) {
        setContrainte(c);
        setTableau(t);
        isMax = ismax;
        if(ismax) System.out.println("maximisation");
        else System.out.println("minimisation");
        setBase(b);
        setHorsBase(hb);
        setVarDecision(condition);
    }
    
    void gaussJordan(int idLigneMin,int idColMax){
        for (int i = 0; i < tableau.length; i++) {
            Fraction coeff=tableau[i][idColMax];
            if (i!=idLigneMin){
                for (int j = 0; j < tableau[0].length; j++) {
                    //tableau[i][j]=tableau[i][j]-(coeff*tableau[min][j]);
                    tableau[i][j]=tableau[i][j].add((tableau[idLigneMin][j].multiply(coeff)).multiply(-1));
                }
            }
        }
    }
    
    void transformeLignePivot(int idLigneMin, int idColMax) {
        Fraction pivot=tableau[idLigneMin][idColMax];
        for (int i = 0; i < tableau[idLigneMin].length; i++) {
            tableau[idLigneMin][i]=tableau[idLigneMin][i].divide(pivot);
        }
    }
    
    void updateBase(int idLigneMin, int idColMax) {
        String temp = base.get(idLigneMin);
        int idInHB = getIdInHorsBase(idColMax);
        base.set(idLigneMin, horsBase.get(idInHB));
        horsBase.set(idInHB, temp);
    }
    
    int getIdInHorsBase(int idCol) {
        String label = allVariables[idCol];
        for (int i = 0; i < horsBase.size(); i++) {
            String get = horsBase.get(i);
            if(get.equalsIgnoreCase(label)) return i;
        }
        return -1;
    }
    
    public int getIdMinCol(int max) throws Exception{
        int kely = getCooFirstPositifInColonne(max);
        int idColFarany = tableau[0].length - 1;
        Fraction petit=tableau[kely][idColFarany].divide(tableau[kely][max]);
        boolean misyPositif=false;
        for (int i = 0; i < tableau.length - 1; i++) {
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
    
    public int getCooFirstPositifInColonne(int idColMax) throws Exception {
        Fraction elt;
        for (int i = 0; i < tableau.length - 1; i++) {
//             String string = variablesHB[i];
            elt = tableau[i][idColMax];
            if(elt.floatValue() > 0) return i;
        }
        throw  new Exception("tsy misy CooFirstPositifInColonne");
    }
    
    public int getIdFirstPositif(Fraction [] lf) throws Exception {
        for (int i = 0; i < lf.length; i++) {
            Fraction fraction = lf[i];
            //System.out.println("lf[i] "+lf[i]);
            if(fraction.floatValue() > 0) return i;
        }
        throw new Exception("tsy misy positif intsony le elt");
    }
    
    public int getIdMax_simplexeMax(Fraction[] derniereLigne) throws Exception {
        int idFirstPositif = getIdFirstPositif(derniereLigne);
        Fraction max = derniereLigne[idFirstPositif];
        int idMax= idFirstPositif;
        
        boolean misyPositif=false;
        for (int i = 0; i < derniereLigne.length - 1; i++) {
            if (derniereLigne[i].floatValue()>0){
                if (derniereLigne[i].compareTo(max) > 0){
                    idMax=i;
                    max=derniereLigne[i];
                }
                misyPositif=true;
            }
        }
        if (misyPositif==false)
            throw new Exception("L'optimum est reperé");
        return idMax;
    }
    
    public int getIdFirstNegatif(Fraction [] lf) throws Exception {
        for (int i = 0; i < lf.length; i++) {
            Fraction fraction = lf[i];
            if(fraction.floatValue() < 0) return i;
        }
        throw new Exception("tsy misy negatif intsony le elt");
    }
    
    public int getIdMax_simplexeMin(Fraction[] ambany) throws Exception{
        int valiny=getIdFirstNegatif(ambany);
//        System.out.println("getIndiceMax_Simplexe_min "+valiny);
        float ngeza_abs=fabs(ambany[valiny].floatValue());
        boolean misyNegatif=false;
        for (int i = 0; i < ambany.length - 1; i++) { //ny col farany tsy pris en compte
            if (ambany[i].floatValue()<0){
                float ambany_abs = fabs(ambany[i].floatValue());
                if (ambany_abs > ngeza_abs){
                    valiny=i;
                    ngeza_abs=ambany_abs;
                }
                misyNegatif=true;
            }
        }
        if (misyNegatif==false)
            throw new Exception("L'optimum est reperé");
        return valiny;
    }
    
    public float fabs(float f) {
        if(f < 0) return -f;
        return f;
    }
    
    void traiterVarBaseInFonctionObj() {
        if(thereIsVarBaseInFonctionObj()) {
            //alaina tsirairay le var base ao am fonction
            //pour tout elt dans base , verifier si ao am fonction obj initiale
            int idVarBase = 0;
            for (Map.Entry<String, Fraction> entry : baseFinale.entrySet()) {
                Object key = entry.getKey();
                Object val = entry.getValue();
                if(isInFonctionInitale(key.toString())) {
                    Fraction [] ligneVarBase = tableau[idVarBase];
                    //System.out.println("idVarBase "+idVarBase+" key "+key);
                    //boucler la derniere ligne de ovaina tsirairay alohan derniere colonne
                    for (int i = 0; i < tableau[tableau.length - 1].length - 1; i++) {
                        //verifier si elt derniere ligne ao am fonction intiale sady tsy ao am base -> coefficient  = 0
                        Fraction coeff = new Fraction(0);
                        if(isInFonctionInitale(allVariables[i]) == true) {
                            coeff = varDecision.get(allVariables[i]);
                            //System.out.println("allVariables in fonction "+allVariables[i]+" coeff "+coeff);
                        }
                        tableau[tableau.length - 1][i] = coeff.subtract(ligneVarBase[i]);
                    }
                    //traitement derniere colonne
                    tableau[tableau.length - 1][tableau[0].length - 1] = tableau[tableau.length - 1][tableau[0].length - 1].subtract((Fraction) val);
                }
                idVarBase++;
            }
        }
        else { //coeff tsotra apetraka eo am colonne tsirairay
            
        }
    }
    
    boolean isInFonctionInitale(String var) {
        for (Map.Entry<String, Fraction> entry : varDecision.entrySet()) {
            Object key = entry.getKey();
            Object val = entry.getValue();
            if(var.equalsIgnoreCase(key.toString())) return true;
        }
        return false;
    }
    
    boolean thereIsVarBaseInFonctionObj() {
        for (Map.Entry<String, Fraction> entry : varDecision.entrySet()) {
            Object cle = entry.getKey();
            Object valeur = entry.getValue();
            for (Map.Entry<String, Fraction> entry2 : baseFinale.entrySet()) {
                Object key = entry2.getKey();
                Object val = entry2.getValue();
                if(cle.toString().equalsIgnoreCase(key.toString())) return true;
            }
        }
        return false;
    }
    
    public Fraction getOptimum() {
        afficheTab();
        if(use2phases) {
            SimplexePhase1 sp1 = new SimplexePhase1(contrainte, tableau, varDecision, isMax, use2phases, horsBase, base, varEcart, varArtificielle, allVariables);
            sp1.getOptimum();
            transfert(sp1);
            traiterVarBaseInFonctionObj();
            //afficheTab();
        }
        
        return getOptimumOrigine();
        //return new Fraction(0);
    }
    
    public Fraction getOptimumOrigine() {
        try {
            int idColMax = 0;
            Fraction [] derniereLigne = tableau[tableau.length - 1];
            if(isMax) {
                //mijery ny max @elt positif @derniere ligne , ra tsisy > 0 de exception
                idColMax = getIdMax_simplexeMax(derniereLigne);
            }
            else {
                //mijery ny max @elt negatif @derniere ligne , ra tsisy < 0 de exception
                idColMax = getIdMax_simplexeMin(derniereLigne);
            }
            
            //maka anle min (elt derniere col / elt col max)
            int idLigneMin = getIdMinCol(idColMax);
            
            updateBase(idLigneMin, idColMax);
            
            transformeLignePivot(idLigneMin, idColMax); //mamadika pivot 1 de manova ny elt @iny ligne iny rehetra

            gaussJordan(idLigneMin, idColMax); //annuler tous les elts col avec le pivot
            
            afficheTab();
            System.out.println("=========================================");
            return this.getOptimumOrigine();
            
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            constructBaseFinale();
            System.out.println("optimum : "+tableau[tableau.length - 1][tableau[0].length - 1].multiply(-1));
            return tableau[tableau.length - 1][tableau[0].length - 1].multiply(-1);
        }
    }
   
    
    public void afficheTab(){
        for (Fraction[] tableau1 : this.tableau) {
            for (int j = 0; j < this.tableau[0].length; j++) {
                System.out.print(tableau1[j] + " || ");
            }
            System.out.println();
        }
    }
    
    void constructBaseFinale() {
        for (int i = 0; i < base.size(); i++) {
            String get = base.get(i);
            System.out.println(get + " = "+tableau[i][tableau[0].length - 1]);
            baseFinale.put(get, tableau[i][tableau[0].length - 1]);
        }
    }
    
    

    public String[] getAllVariables() {
        return allVariables;
    }

    public void setAllVariables(String[] allVariables) {
        this.allVariables = allVariables;
    }

    public Fraction[][] getTableau() {
        return tableau;
    }

    public void setTableau(Fraction[][] tableau) {
        this.tableau = tableau;
    }

    public Contrainte getContrainte() {
        return contrainte;
    }

    public void setContrainte(Contrainte contrainte) {
        this.contrainte = contrainte;
    }

    public ArrayList<String> getBase() {
        return base;
    }

    public void setBase(ArrayList<String> base) {
        this.base = base;
    }

    public ArrayList<String> getHorsBase() {
        return horsBase;
    }

    public void setHorsBase(ArrayList<String> horsBase) {
        this.horsBase = horsBase;
    } 

    public HashMap<String, Fraction> getVarDecision() {
        return varDecision;
    }

    public void setVarDecision(HashMap<String, Fraction> VarDecision) {
        this.varDecision = VarDecision;
    }

    public ArrayList<String> getVarEcart() {
        return varEcart;
    }

    public void setVarEcart(ArrayList<String> varEcart) {
        this.varEcart = varEcart;
    }

    public ArrayList<String> getVarArtificielle() {
        return varArtificielle;
    }

    public void setVarArtificielle(ArrayList<String> varArtificielle) {
        this.varArtificielle = varArtificielle;
    }

    public LinkedHashMap<String, Fraction> getBaseFinale() {
        return baseFinale;
    }

    public void setBaseFinale(LinkedHashMap<String, Fraction> baseFinale) {
        this.baseFinale = baseFinale;
    }
    
    
}
