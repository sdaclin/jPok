package io.jpok.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 03/05/14
 * Time: 11:53
 */
public class Party {
  // nb seats
  private int size;

  // Blind strategie
  private BlindSchema blindSchema;

  // Party state
  private State state;

  private int startingStack;
  private List<Player> players;
  private int currentPlayer;
  private int dealer;
  private List<Card> board = new ArrayList<>(5);

  public enum State { PREFLOP, FLOP, TURN, RIVER }

  /**
   * @param size number of seats
   */
  public Party(int size, BlindSchema blindSchema, int startingStack) {
    this.size = size;
    this.players = new ArrayList<>(size);
    this.blindSchema = blindSchema;
    this.startingStack = startingStack;
  }

  public int getSize() {
    return size;
  }

  public BlindSchema getBlindSchema() {
    return blindSchema;
  }

  public int getStartingStack() {
    return startingStack;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public int getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(int currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public int getDealer() {
    return dealer;
  }

  public void setDealer(int dealer) {
    this.dealer = dealer;
  }

  public List<Card> getBoard() {
    return board;
  }

  public void setBoard(List<Card> board) {
    this.board = board;
  }
}
