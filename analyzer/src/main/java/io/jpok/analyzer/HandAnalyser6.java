package io.jpok.analyzer;

import java.util.Arrays;

import io.jpok.model.HandValue;

import io.jpok.model.Card;
import io.jpok.model.Strength;
import io.jpok.tools.ArrayTools;

/**
 * Analyse une main en fonction des 4 cartes sur table
 * @author Tonio
 *
 */
public class HandAnalyser6 implements HandAnalyser {

	// --- Non re-affectation du tableau
	private final int SIZE=6;
	private Card[] cards;
	// --- Variables de l'automate
	private int kicker;				// kicker
	private int kickerSize;			// nombre de cartes dans le kicker
	private int nb; 				// nombre de cards de meme force
	private int max;				// propriete de plus grande valeur (sur les multiples uniquement)
	private boolean[] props;		// mains possibles
	private Card[] topCards;		// cartes definissant la main
	private Card[] colForces;		// forces par quinte de couleur
	private Card[] colorsCard;		// plus forte carte dans chaque couleur
	private int[] col;				// force de la carte precedente jouee dans la quinte flush
	private int[] colors; 			// nombre de cartes tombees par couleur
	private Card colForce=null;		// force de la quinte finale, null sauf si quinte
	private Card card;				// carte en cours
	private int couleur;			// couleur en cours
	private int force;				// force en cours
	
	protected HandAnalyser6() {
		this.cards=new Card[SIZE];
		this.props=new boolean[] {false,false,false,false,false,false,false,false};
		this.topCards=new Card[] {null,null};
		this.colForces=new Card[] {null,null,null,null,null};
		this.colorsCard=new Card[] {null,null,null,null};
		this.col=new int[] {-1,-1,-1,-1,-1};
		this.colors=new int[] {0,0,0,0};
	}
	
	/**
	 * Initialisation de l'automate
	 */
	private void init() {
		Arrays.fill(props, false);
		Arrays.fill(topCards, null);
		Arrays.fill(colorsCard, null);
		Arrays.fill(col, -1);
		Arrays.fill(colors, 0);
		kicker=0;
		kickerSize=5;
		nb=0;
		max=-1;
		colForce=null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see poker.common.analyser.io.jpok.analyzer.HandAnalyser#getHandStrength(poker.common.bean.Hand, poker.common.bean.Table)
	 */
	public HandValue getHandStrength(Card[] hand, Card[] table) {
		ArrayTools.fusion(hand, table, cards);
		// --- Initialisation des variables
		init();
		card=cards[0];
		couleur=card.getSuit().ordinal();
		force=card.getRank().ordinal();
		// --- Initialisation de la couleur
		colors[couleur]++;
		colorsCard[couleur]=card;
		// --- Initialisation de la quinte
		colForces[couleur]=card;
		colForces[4]=card;
		col[couleur]=force;
		col[4]=force;
		for (int i=1; i<SIZE; i++ ) {
			// --- Initialisation
			card=cards[i];
			couleur=card.getSuit().ordinal();
			force=card.getRank().ordinal();
			// --- Couleurs
			colors[couleur]++;
			if (colorsCard[couleur]==null) colorsCard[couleur]=card;
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
			// --- Test des quintes flush
			if (col[couleur]<0) {
				// --- Premiere carte de la suite
				col[couleur]=force;
				colForces[couleur]=card;
			} else if (col[couleur]==force+1) {
				// --- carte suivante
				col[couleur]=force;
				if (colForces[couleur].getRank().ordinal()-force==4) {
					// --- Quinte Flush
					props[3]=true;
					props[7]=true;
					colForce=colForces[couleur];
					break;
				}
			} else if (col[couleur]-force>1) {
				// --- Rupture de suite
				col[couleur]=force;
				colForces[couleur]=card;
			}
			// --- Test de la quinte
			if (!props[3] && col[4]<0) {
				// --- Premiere carte de la suite
				col[4]=force;
				colForces[4]=card;
			} else if (col[4]==force+1) {
				// --- Carte suivante
				col[4]=force;
				if (colForces[4].getRank().ordinal()-force==4) {
					// --- Quinte
					props[3]=true;
					colForce=colForces[4];
				}
			} else if (col[4]-force>1) {
				// --- Rupture de suite
				col[4]=force;
				colForces[4]=card;
			}
		}
		// --- Tester les suites au 5 (5, 4, 3, 2 AS)
		int k=0;
		if (!props[7]) {
			while (cards[k].getRank()==Card.Rank.ACE) {
				couleur=cards[k].getSuit().ordinal();
				force=cards[k].getRank().ordinal();
				// --- Tester la quinte flush
				if (col[couleur]==Card.Rank.TWO.ordinal() && colForces[couleur].getRank()==Card.Rank.FIVE) {
					// --- Quinte Flush
					props[3]=true;
					props[7]=true;
					colForce=colForces[couleur];
					break;
				}
				// --- Tester la quinte
				if (!props[3] && col[4]==Card.Rank.TWO.ordinal() && colForces[4].getRank()==Card.Rank.FIVE) {
					// --- Quinte
					props[3]=true;
					colForce=colForces[4];
				}
				k++;
			}
		}
		// --- Cas d'une suite
		if (props[3] || props[7]) {
			topCards[0]=colForce;
			kickerSize=0;
		}
		// --- Tester la couleur
		if (!props[5] && !props[7]) {
			for (int i=0; i<4; i++) {
				if (colors[i]>4) {
					topCards[0]=colorsCard[i];
					props[4]=true;
					kickerSize=5;
					break;
				}
			}
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
