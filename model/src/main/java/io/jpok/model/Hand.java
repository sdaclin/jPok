package io.jpok.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A player(s hand with 2 cards
 *
 * @author Sylvain
 */
public class Hand implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Card> cards;

    public Hand(Card a, Card b) {
        if (a==b){
            throw new RuntimeException("A Hand can't hold the same card");
        }
        cards = new ArrayList<Card>(2);
        cards.add(a);
        cards.add(b);
    }

    @Override
    public String toString() {
        return "[" + cards.get(0) + "/" + cards.get(1) + "]";
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Hand)){
          return false;
        }
        Hand hand = (Hand) o;
        return (((1 << hand.getCards().get(0).ordinal()) | (1 << hand.getCards().get(1).ordinal()))
                ^ ((1 << cards.get(0).ordinal()) | (1 << cards.get(1).ordinal()))) == 0;
    }
    
    @Override
    public int hashCode() {
        return cards.get(0).ordinal()+cards.get(1).ordinal();
    }
}
