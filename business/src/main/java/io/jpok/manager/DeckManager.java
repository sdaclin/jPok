package io.jpok.manager;

import io.jpok.model.Card;
import io.jpok.model.Deck;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 06/05/14
 * Time: 13:01
 */
public class DeckManager {
  private Deck deck;

  public DeckManager() {
    this.init();
  }


  /**
   * Shuffle the deck
   */
  public void shuffle() {
    Collections.shuffle(deck.getCards());
  }

  /**
   * Pick a card from the deck
   * @return the first card in top of the deck
   */
  public Card pick() {
    return deck.getCards().remove(0);
  }

  /**
   * Remove the card from the deck
   */
  public void remove(Card card) {
    deck.getCards().remove(card);
  }

  /**
   * init the deck
   */
  public void init() {
    this.deck = new Deck();
    shuffle();
  }
}
