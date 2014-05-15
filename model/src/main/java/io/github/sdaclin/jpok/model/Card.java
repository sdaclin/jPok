package io.github.sdaclin.jpok.model;

import java.util.Collection;
import java.util.EnumSet;

public enum Card {

  CLUB_TWO(Suit.CLUB, Rank.TWO),
  DIAMOND_TWO(Suit.DIAMOND, Rank.TWO),
  HEART_TWO(Suit.HEART, Rank.TWO),
  SPADE_TWO(Suit.SPADE, Rank.TWO),
  CLUB_THREE(Suit.CLUB, Rank.THREE),
  DIAMOND_THREE(Suit.DIAMOND, Rank.THREE),
  HEART_THREE(Suit.HEART, Rank.THREE),
  SPADE_THREE(Suit.SPADE, Rank.THREE),
  CLUB_FOUR(Suit.CLUB, Rank.FOUR),
  DIAMOND_FOUR(Suit.DIAMOND, Rank.FOUR),
  HEART_FOUR(Suit.HEART, Rank.FOUR),
  SPADE_FOUR(Suit.SPADE, Rank.FOUR),
  CLUB_FIVE(Suit.CLUB, Rank.FIVE),
  DIAMOND_FIVE(Suit.DIAMOND, Rank.FIVE),
  HEART_FIVE(Suit.HEART, Rank.FIVE),
  SPADE_FIVE(Suit.SPADE, Rank.FIVE),
  CLUB_SIX(Suit.CLUB, Rank.SIX),
  DIAMOND_SIX(Suit.DIAMOND, Rank.SIX),
  HEART_SIX(Suit.HEART, Rank.SIX),
  SPADE_SIX(Suit.SPADE, Rank.SIX),
  CLUB_SEVEN(Suit.CLUB, Rank.SEVEN),
  DIAMOND_SEVEN(Suit.DIAMOND, Rank.SEVEN),
  HEART_SEVEN(Suit.HEART, Rank.SEVEN),
  SPADE_SEVEN(Suit.SPADE, Rank.SEVEN),
  CLUB_EIGHT(Suit.CLUB, Rank.EIGHT),
  DIAMOND_EIGHT(Suit.DIAMOND, Rank.EIGHT),
  HEART_EIGHT(Suit.HEART, Rank.EIGHT),
  SPADE_EIGHT(Suit.SPADE, Rank.EIGHT),
  CLUB_NINE(Suit.CLUB, Rank.NINE),
  DIAMOND_NINE(Suit.DIAMOND, Rank.NINE),
  HEART_NINE(Suit.HEART, Rank.NINE),
  SPADE_NINE(Suit.SPADE, Rank.NINE),
  CLUB_TEN(Suit.CLUB, Rank.TEN),
  DIAMOND_TEN(Suit.DIAMOND, Rank.TEN),
  HEART_TEN(Suit.HEART, Rank.TEN),
  SPADE_TEN(Suit.SPADE, Rank.TEN),
  CLUB_JACK(Suit.CLUB, Rank.JACK),
  DIAMOND_JACK(Suit.DIAMOND, Rank.JACK),
  HEART_JACK(Suit.HEART, Rank.JACK),
  SPADE_JACK(Suit.SPADE, Rank.JACK),
  CLUB_QUEEN(Suit.CLUB, Rank.QUEEN),
  DIAMOND_QUEEN(Suit.DIAMOND, Rank.QUEEN),
  HEART_QUEEN(Suit.HEART, Rank.QUEEN),
  SPADE_QUEEN(Suit.SPADE, Rank.QUEEN),
  CLUB_KING(Suit.CLUB, Rank.KING),
  DIAMOND_KING(Suit.DIAMOND, Rank.KING),
  HEART_KING(Suit.HEART, Rank.KING),
  SPADE_KING(Suit.SPADE, Rank.KING),
  CLUB_ACE(Suit.CLUB, Rank.ACE),
  DIAMOND_ACE(Suit.DIAMOND, Rank.ACE),
  HEART_ACE(Suit.HEART, Rank.ACE),
  SPADE_ACE(Suit.SPADE, Rank.ACE);
  private Suit suit;
  private Rank rank;

  public static enum Rank {

    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE;

    public String toString() {
      switch (this) {
        case ACE:
          return "A";
        case KING:
          return "K";
        case QUEEN:
          return "Q";
        case JACK:
          return "J";
        default:
          return String.valueOf(this.ordinal() + 2);
      }
    }

  }

  public static enum Suit {

    CLUB,
    DIAMOND,
    HEART,
    SPADE;

    public String toString() {
      switch (this) {
        case CLUB : return "C";
        case DIAMOND : return "D";
        case HEART : return "H";
        default : return "S";
      }
    }

  }

  private Card(Suit suit, Rank rank) {
    this.suit = suit;
    this.rank = rank;
  }

  /**
   * Creation d'un jeu de cartes ordonne
   *
   * @return
   */
  public static Collection<Card> toDeck() {
    return EnumSet.allOf(Card.class);
  }

  /**
   * Comparaison de la force de deux cartes
   *
   * @param card
   * @return
   */
  public int compareRank(Card card) {
    return this.rank.compareTo(card.getRank());
  }

  public Suit getSuit() {
    return suit;
  }

  public Rank getRank() {
    return rank;
  }

  public static Card of(Rank r, Suit s) {
    return Card.values()[r.ordinal() * 4 + s.ordinal()];
  }

  public String toString() {
    return this.rank + "" + this.suit;
  }
}
