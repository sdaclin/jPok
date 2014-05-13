package io.jpok.model;

import java.io.Serializable;


/**
 * Represente la force d'une main
 * @author Tonio
 *
 */
public class HandValue implements Comparable<HandValue>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static HandValue MIN_HAND_VALUE = new HandValue();
	
	private Strength strength;
	private Card[] topCards;
	private int value;

	/**
	 * Representation de la force d'une main
	 * @param strength
	 * @param kicker
	 * @param topCards
	 */
	public HandValue(Strength strength, int kicker, Card...topCards) {
		this.strength=strength;
		this.topCards=topCards;
		this.value=strength.ordinal()*1000000+kicker;
	}
	
	private HandValue() {
		this.strength = Strength.HIGHT_CARD;
		this.topCards = null;
		this.value = 0;
	}
	
	public Strength getStrength() {
		return strength;
	}
	
	protected int getValue() {
		return value;
	}
	
	/**
	 * Compare la force de deux mains
	 * @param handValue
	 * @return
	 */
	public int compareTo(HandValue handValue) {
		return this.value - handValue.getValue();
	}
	
	/**
	 * Double paire au Roi de coeur et 3 de treffle [2]
	 */
	public String toString() {
		return strength.toString() + (topCards!=null&&topCards.length>=0?" au "+topCards[0]+(topCards!=null&&topCards.length>1?" et "+topCards[1]:""):"")+" [valeur: "+value+"]";
	}
	
}
