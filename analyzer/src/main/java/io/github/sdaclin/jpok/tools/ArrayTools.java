package io.github.sdaclin.jpok.tools;

import java.lang.reflect.Array;


/**
 * Facilites sur les tableaux
 * @author avianey
 */
@SuppressWarnings("unchecked")
public class ArrayTools {

    /**
     * Fusion de deux tableaux tries d'elements qui implementent java.lang.Comparable
     * @param <T>
     * @param c1 un tableau trie
     * @param c2 un tableau trie
     * @return le tableau trie des elements fusionnes
     */
    public static <T extends Comparable>T[] fusion(T[] c1, T[] c2) {
        if (c1 == null) return c2;
        T[] result = (T[])Array.newInstance(c1.getClass().getComponentType(), c1.length + c2.length);
        return fusion(c1, c2, result);
    }

    /**
     * Fusion de deux tableaux tries d'elements qui implementent java.lang.Comparable
     * @param <T>
     * @param c1 un tableau trie
     * @param c2 un tableau trie
     * @param result un tableau dans lequel sera stocke le tableau fusionne de taille c1.length+c2.length
     * @return le tableau trie des elements fusionnes modifie par effet de bord
     */
    public static <T extends Comparable>T[] fusion(T[] c1, T[] c2, T[] result) {
        if (result == null || c1 == null || c2 == null) throw new IllegalArgumentException();
        if (result.length != c1.length + c2.length) throw new IllegalArgumentException();
        int i1 = 0, i2 = 0;
        // --- Tri des elements
        for (int i = 0; i < result.length; i++) {
            if (i1 < c1.length && i2 < c2.length) {
                if (c1[i1].compareTo(c2[i2])>0) {
                    result[i] = c1[i1++];
                } else {
                    result[i] = c2[i2++];
                }
            } else if (i1 == c1.length) {
                result[i] = c2[i2++];
            } else {
                result[i] = c1[i1++];
            }
        }
        return result;
    }

    /**
     * Melange aleatoire d'un tableau par l'algorithme de Fisher-Yates
     * @param <T>
     * @param result
     * @return
     */
    public static <T>T[] shuffle(T[] result) {
        for (int i = result.length - 1; i >= 0; i--) {
            int j = (int) Math.floor((Math.random() * result.length));
            if (i != j) {
                T temp = result[i];
                result[i] = result[j];
                result[j] = temp;
            }
        }
        return result;
    }

}