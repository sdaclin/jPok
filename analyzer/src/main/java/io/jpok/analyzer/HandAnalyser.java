package io.jpok.analyzer;

import io.jpok.model.Card;
import io.jpok.model.HandValue;


/**
 * Analyse une main en fonction des cartes sur table
 * @author Tonio
 *
 */
public interface HandAnalyser {

	/**
	 * Calcul la force d'une main
	 * @param hand sorted hand
	 * @param table sorted table
	 * @return
	 */
	public HandValue getHandStrength(Card[] hand, Card[] table);

}
