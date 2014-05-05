package io.jpok.manager;

import io.jpok.model.Party;
import io.jpok.model.Player;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 03/05/14
 * Time: 13:37
 */
public class PartyManager {
  private short playerId = 0;
  private final Party party;
  private final BlindManager blindManager;
  private List<Player> playersInGame;
  private List<Player> playersInBet;
  private int maxBetAmount;
  private int raiserIndex;
  private int currentPlayerIndex;

  public PartyManager(Party party) {
    this.party = party;
    blindManager = new BlindManager(party.getBlinds());
  }

  /**
   * Register a player if the party is not full
   *
   * @param player
   */
  public void registerPlayer(String player) {
    if (party.getSize() == party.getPlayers().size()) {
      throw new RuntimeException("Party is full");
    }
    party.add(new Player(playerId++, player, party.getStartingStack()));
    Collections.copy(playersInGame, party.getPlayers());
  }

  /**
   * Play a whole poker party
   * A poker party is divided in many round while there are at least 2 players in game
   */
  public void play() {
    while (!isOver()) {
      setButton();
      blindManager.handleRankChange();
      maxBetAmount = 0;
      Collections.copy(playersInBet, playersInGame);

      for (Party.Stage currentStage : Party.Stage.values()) {
        party.setStage(currentStage);
        takeBets();
        /*if (onePlayerLeft()) {
          break;
        }*/
      }

      //retributeWinners();
    }
  }

  /**
   * True if the party is over
   * A party is over when there is just one player with a positive stack
   *
   * @return
   */
  private boolean isOver() {
    int count = 0;
    for (Player player : party.getPlayers()) {
      if (player.getStack() > 0) {
        count++;
        if (count >= 2) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Get bets from each players
   * Handle BigBlind, SmallBlind and Ants at flop time
   */
  private void takeBets() {
    // Handle preflop
    if (party.getStage() == Party.Stage.PREFLOP) {
      bet(nextPlayer(party.getButtonIndex()), blindManager.getBlindRank().getBigBlind());
      bet(nextPlayer(), blindManager.getBlindRank().getSmallBlind());
      while (nextPlayer() != dealerPlayer()) {
        bet(currentPlayer(), blindManager.getBlindRank().getAnt());
      }
    }

    // Handle bets
    while(nextPlayer() != raiserPlayer()) {

    }
  }

  private Player dealerPlayer() {
    return playersInGame.get(party.getButtonIndex());
  }

  private Player nextPlayer(int indexPlayer) {
    currentPlayerIndex = indexPlayer;
    return nextPlayer();
  }

  private Player nextPlayer() {
    currentPlayerIndex = currentPlayerIndex +1%playersInBet.size();
    return playersInBet.get(currentPlayerIndex);
  }

  private Player currentPlayer() {
    return playersInBet.get(currentPlayerIndex);
  }

  /**
   * The last player that had made a RAISE
   * @return
   */
  private Player raiserPlayer() {
    return playersInBet.get(raiserIndex);
  }

  /**
   * Make a bet for a player
   *
   * @param player
   * @param amount      requested to bet
   * @return max the player can bet
   */
  private int bet(Player player, int amount) {
    if (player.getStack() < amount) {
      amount = player.getStack();
    }
    player.setStack(player.getStack() - amount);
    if (amount > maxBetAmount) {
      maxBetAmount = amount;
      raiserIndex = currentPlayerIndex;
    }
    return amount;
  }

  private void setButton() {
    if (!party.isStarted()) {
      // Randomly set the dealer
      party.setButtonIndex(Double.valueOf(Math.random()).intValue() * party.getSize());
    } else {
      // Set the button to the next player
      party.setButtonIndex(whoIsNextInGame(party.getButtonIndex()));
    }
  }

  /**
   * Get the index of the next player in game
   *
   * @param startingFrom
   * @return
   */
  private int whoIsNextInGame(int startingFrom) {
    return startingFrom + 1 % playersInGame.size();
  }

  /**
   * Get the index of the next player in bet
   *
   * @param startingFrom
   * @return
   */
  private int whoIsNextInBet(int startingFrom) {
    return startingFrom + 1 % playersInBet.size();
  }
}
