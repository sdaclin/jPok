package io.jpok.model;


/**
 * Enumeration des differentes forces possibles d'un main
 * @author Tonio
 *
 */
public enum Strength {

	HIGHT_CARD,
	ONE_PAIR,
	TWO_PAIR,
	THREE_OF_A_KING,
	STRAIGHT,
	FLUSH,
	FULL_HOUSE,
	FOUR_OF_A_KING,
	STRAIGHT_FLUSH;
	
	public String toString() {
		switch (this) {
			case HIGHT_CARD : return "Meilleur carte";
			case ONE_PAIR : return "Paire";
			case TWO_PAIR : return "Double paire";
			case THREE_OF_A_KING : return "Brelan";
			case STRAIGHT : return "Quinte";
			case FLUSH : return "Couleur";
			case FULL_HOUSE : return "Full";
			case FOUR_OF_A_KING : return "Carre";
			default : return "Quinte flush";
		}
	}
	
}
