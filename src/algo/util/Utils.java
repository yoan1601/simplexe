/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algo.util;

/**
 *
 * @author yoan
 */
public class Utils {
    public static String[] concatenerTableaux(String[]... tableaux) {
        // Calculer la taille totale du tableau résultant
        int tailleTotale = 0;
        for (String[] tableau : tableaux) {
            tailleTotale += tableau.length;
        }

        // Créer un nouveau tableau résultant
        String[] resultat = new String[tailleTotale];

        // Copier les éléments des tableaux individuels dans le tableau résultant
        int destinationIndex = 0;
        for (String[] tableau : tableaux) {
            System.arraycopy(tableau, 0, resultat, destinationIndex, tableau.length);
            destinationIndex += tableau.length;
        }

        return resultat;
    }
}
