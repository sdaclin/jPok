package io.github.sdaclin.jpok.model;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 03/05/14
 * Time: 13:39
 */
public class Player {
  private final short id;
  private final String name;
  private int stack;
  private int bet = 0;
  private Hand hand;
  private HandValue handValue;
  private State state;

  public enum State {IN, ALLIN, FOLD, LOOSE}

  public Player(short id, String name, int stack) {
    this.id = id;
    this.name = name;
    this.stack = stack;
    this.state = State.IN;
  }

  public String getName() {
    return name;
  }

  public void setStack(int stack) {
    this.stack = stack;
  }

  public int getStack() {
    return stack;
  }

  public int getBet() {
    return bet;
  }

  public void setBet(int bet) {
    this.bet = bet;
  }

  public void setHand(Hand hand) {
    this.hand = hand;
  }

  public Hand getHand() {
    return hand;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public HandValue getHandValue() {
    return handValue;
  }

  public void setHandValue(HandValue handValue) {
    this.handValue = handValue;
  }

  @Override
  public String toString() {
    return String.format("%s [%d][%d]", name, bet,stack);
  }

  // TODO try different hashcode when running load tests
  @Override
  public int hashCode() {
    return id % 2;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Player && ((Player) obj).id == this.id;
  }
}
