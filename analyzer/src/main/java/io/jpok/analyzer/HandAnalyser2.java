package io.jpok.analyzer;

import fr.jpok.model.HandValue;
import fr.jpok.model.Card;
import fr.jpok.model.Strength;

/**
 * Analyse une main en fonction des 2 cartes qu'elle contient
 *
 * @author Tonio
 *
 */
public class HandAnalyser2 implements HandAnalyser {

    protected HandAnalyser2() {
    }

    /*
     * (non-Javadoc) @see
     * net.pokerprobability.helper.service.io.jpok.analyzer.HandAnalyser#getHandStrength(net.pokerprobability.enumeration.Card[],
     * net.pokerprobability.enumeration.Card[])
     */
    public HandValue getHandStrength(Card[] hand, Card[] table) {
        int p = 28561;
        if (hand[0].compareRank(hand[1]) == 0) {
            return new HandValue(Strength.ONE_PAIR, hand[0].getRank().ordinal() * p, hand[0]);
        } else {
            return new HandValue(Strength.HIGHT_CARD, hand[0].getRank().ordinal() * p + hand[1].getRank().ordinal() * p / 13, hand[0]);
        }
    }
}