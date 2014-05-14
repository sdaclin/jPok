package io.jpok.analyzer;

import java.util.Arrays;

import fr.jpok.model.HandValue;

import fr.jpok.model.Card;
import fr.jpok.model.Rank;
import fr.jpok.model.Strength;
import fr.jpok.tools.ArrayTools;

/**
 * Analyse une main en fonction des 3 cartes sur table
 * @author Tonio
 *
 */
public class HandAnalyser5 implements HandAnalyser {

	// --- Non re-affectation du tableau
	private final int SIZE=5;
	private Card[] cards;
	// --- Variables de l'automate
	private int kicker;				// kicker
	private int kickerSize;			// nombre de cartes dans le kicker
	private int nb; 				// nombre de cards de meme force
	private int max;				// propriete de plus grande valeur (sur les multiples uniquement)
	private boolean[] props;		// mains possibles
	private Card[] topCards;		// cartes definissant la main
	private Card card;				// carte en cours
	private int couleur;			// couleur en cours
	
	protected HandAnalyser5() {
		this.cards=new Card[SIZE];
		this.props=new boolean[] {false,false,false,false,false,false,false,false};
		this.topCards=new Card[] {null,null};
	}
	
	/**
	 * Initialisation de l'automate
	 */
	private void init() {
		Arrays.fill(props, false);
		props[4]=true;
		Arrays.fill(topCards, null);
		kicker=0;
		kickerSize=5;
		nb=0;
		max=-1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.pokerprobability.helper.service.io.jpok.analyzer.HandAnalyser#getHandStrength(net.pokerprobability.enumeration.Card[], net.pokerprobability.enumeration.Card[])
	 */
	public HandValue getHandStrength(Card[] hand, Card[] table) {
		ArrayTools.fusion(hand, table, cards);
		// --- Initialisation des variables
		init();
		card=cards[0];
		couleur=card.getSuit().ordinal();
		for (int i=1; i<SIZE; i++ ) {
			// --- Initialisation
			card=cards[i];
			if (couleur!=card.getSuit().ordinal()) props[4]=false;
			couleur=card.getSuit().ordinal();
			// --- Cards identiques
			if (cards[i-1].compareRank(card)==0) {
				if (nb==0) nb=1;
				nb++;
				// --- Cards identiques
				if (nb==2 && max<0) {
					// --- Premiere paire
					props[0]=true;
					max=0;
					topCards[0]=cards[i-1];
					kickerSize=3;
				} else if (nb==2 && max==0) {
					// --- Deuxieme paire
					props[1]=true;
					props[0]=false;
					max=1;
					topCards[1]=cards[i-1];
					kickerSize=1;
				} else if (nb==3 && max==0) {
					// --- Brelan sur 1ere paire
					props[2]=true;
					props[0]=false;
					max=2;
					kickerSize=0;
				} else if (nb==2 && max==2) {
					// --- Full sur deuxieme paire
					props[2]=false;
					props[5]=true;
					max=5;
					topCards[1]=cards[i-1];
					kickerSize=0;
				} else if (nb==3 && max==1) {
					// --- Full sur brelan
					props[1]=false;
					props[5]=true;
					max=5;
					topCards[1]=topCards[0];
					topCards[0]=cards[i-1];
					kickerSize=0;
				} else if (nb==4) {
					// --- Carre
					props[6]=true;
					props[max]=false;
					max=6;
					kickerSize=0;
					break;
				}
			} else {
				// --- Rupture de cartes, RAZ
				nb=0;
			}
		}
		if (props[4]) topCards[0]=cards[0];
		// --- Test des quintes
		if (max<0 && (cards[0].compareRank(cards[4])==4 || (cards[0].getRank().equals(Rank.ACE) && cards[4].equals(Rank.TWO)))) {
			props[3]=true;
			topCards[0]=cards[0];
			kickerSize=0;
			props[7]=props[4];
		}
		// --- Kicker
		int count=0;
		int p=28561;
		if (topCards[0]!=null) {
			kicker=(topCards[0].getRank().ordinal())*p;
			if (topCards[1]!=null) {
				p=p/13;
				kicker+=(topCards[1].getRank().ordinal())*p;
			}
		}
		while (kickerSize>0 && count<SIZE) {
			if (cards[count]!=topCards[0] && (topCards[1]==null || cards[count]!=topCards[1])) {
				kickerSize--;
				p=p/13;
				kicker+=(cards[count].getRank().ordinal())*p;
			}
			count++;
		}
		if (props[7]) { // quinte flush
			return new HandValue(Strength.STRAIGHT_FLUSH, kicker, topCards[0]);
		}
		else if (props[6]) { // carre
			return new HandValue(Strength.FOUR_OF_A_KING, kicker, topCards[0]);
		}
		else if (props[5]) { // full
			return new HandValue(Strength.FULL_HOUSE, kicker, topCards[0], topCards[1]);
		}
		else if (props[4]) { // couleur
			return new HandValue(Strength.FLUSH, kicker, topCards[0]);
		}
		else if (props[3]) { // suite
			return new HandValue(Strength.STRAIGHT, kicker, topCards[0]);
		}
		else if (props[2]) { // brelan
			return new HandValue(Strength.THREE_OF_A_KING, kicker, topCards[0]);
		}
		else if (props[1]) { // double paire
			return new HandValue(Strength.TWO_PAIR, kicker, topCards[0], topCards[1]);
		}
		else if (props[0]) { // paire
			return new HandValue(Strength.ONE_PAIR, kicker, topCards[0]);
		}
		else { // rien
			return new HandValue(Strength.HIGHT_CARD, kicker, topCards[0]);
		}
	}

}
