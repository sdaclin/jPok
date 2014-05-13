package io.jpok.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sdaclin
 */
public class Deck implements Cloneable {

  private List<Card> cards;

  /**
   * Construit un nouveau paquet
   */
  public Deck() {
    cards = new ArrayList<>(Card.toDeck());
  }

  /**
   * Make a new cards with a few cards
   */
  public Deck(List<Card> cards) {
    this.cards = cards;
  }

  public List<Card> getCards() {
    return cards;
  }

  @Override
  public String toString() {
    return cards.toString();
  }

  @Override
  public Deck clone() {
    return new Deck(new ArrayList<Card>(cards));
  }
}
