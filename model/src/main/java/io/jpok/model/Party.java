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
  private BlindSchema blinds;
  private BlindRank activeRank;

  private int startingStack;
  private List<Player> players;
  private int buttonIndex;
  private Stage stage;

  private boolean started = false;

  public enum Stage { PREFLOP, FLOP, TURN, RIVER }

  /**
   * @param size          number of seats
   * @param blindSchema
   * @param startingStack
   */
  public Party(int size, BlindSchema blindSchema, int startingStack) {
    this.size = size;
    this.blinds = blindSchema;
    this.startingStack = startingStack;
    this.players = new ArrayList<>(size);
    this.activeRank = blindSchema.getRank(0);
  }

  public int getSize() {
    return size;
  }

  public BlindSchema getBlinds() {
    return blinds;
  }

  public int getStartingStack() {
    return startingStack;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public int getButtonIndex() {
    return buttonIndex;
  }

  public void setButtonIndex(int buttonIndex) {
    this.buttonIndex = buttonIndex;
  }

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public Party add(Player player) {
    this.players.add(player);
    return this;
  }

  public boolean isStarted() {
    return started;
  }
}
