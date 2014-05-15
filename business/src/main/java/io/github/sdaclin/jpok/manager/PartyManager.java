package io.github.sdaclin.jpok.manager;

import io.github.sdaclin.jpok.model.*;
import io.github.sdaclin.jpok.analyzer.HandAnalyserProxy;
import io.github.sdaclin.jpok.manager.commander.PlayerCommander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 03/05/14
 * Time: 13:37
 */
public class PartyManager {
  private final Logger logger = LoggerFactory.getLogger(PartyManager.class);
  // A unique ID for each player in the party
  private short playerId = 0;
  // Party managed
  private final Party party;
  // Manager for the blind strategy
  private final BlindManager blindManager;
  // Current amount to call
  private int betAmount;
  // Current player who is doing an action
  private int currentPlayerIndex;
  // Manager that handle the deck of cards
  private final DeckManager deckManager = new DeckManager();
  // The minimal amount for raising
  private int raiseAmount;
  private List<PlayerCommander> commanders;
  private HandAnalyserProxy ha = new HandAnalyserProxy();

  public PartyManager(Party party) {
    this.party = party;
    blindManager = new BlindManager(party.getBlindSchema());
    commanders = new ArrayList<>(party.getPlayers().size());
  }

  /**
   * Register a player if the party is not full
   */
  public void registerPlayer(String playerName, PlayerCommander commander) {
    if (party.getSize() == party.getPlayers().size()) {
      throw new RuntimeException("Party is full");
    }
    party.getPlayers().add(new Player(playerId++, playerName, party.getStartingStack()));
    commanders.add(commander);
  }

  /**
   * Play a whole poker party
   * A poker party is divided in many round while there are at least 2 players in game
   */
  public void play() {
    // Init button pos with a random value
    initButton();

    // Play rounds while the party is not over
    while (!isOver()) {
      // Set button and dealer's pos
      setNextDealer();

      // verify if it's time to augment blinds
      blindManager.handleRankChanging();
      // Deal carts to players
      dealPlayersCards();

      // Init bet Amount
      betAmount = 0;

      // Handling preflop, flop, turn and river bets
      for (Party.State currentStage : Party.State.values()) {
        party.setState(currentStage);

        // Deal board
        dealBoard(currentStage);

        // If there is just one player IN, there is no need to ask action fo the winner
        if (countPlayer(Player.State.IN, Player.State.ALLIN) < 2) {
          continue;
        }

        // Asks each player to bet
        handleBets();
      }

      payWinners();
    }
  }

  /**
   * Deal board if necessary
   */
  private void dealBoard(Party.State stage) {
    switch(stage) {
      case PREFLOP:
        party.getBoard().clear();
        break;
      case FLOP:
        party.getBoard().add(deckManager.pick());
        party.getBoard().add(deckManager.pick());
        party.getBoard().add(deckManager.pick());
        break;
      case TURN:
        party.getBoard().add(deckManager.pick());
        break;
      case RIVER:
        party.getBoard().add(deckManager.pick());
        break;
    }
    if (stage != Party.State.PREFLOP) {
      for(PlayerCommander commander : commanders){
        commander.notify(party);
      }
    }
  }

  /**
   * Pay players
   */
  private void payWinners() {

    List<Player> playersInGame = new ArrayList<>();
    List<Player> playersFold = new ArrayList<>();
    Set<Card> board = new TreeSet<>(party.getBoard());
    Set<Card> playerHand;

    // Compute the hand value of in game players
    for (Player player : party.getPlayers()) {
      if (player.getState() == Player.State.LOOSE) {
        continue;
      }
      if (player.getState() == Player.State.FOLD) {
        playersFold.add(player);
        continue;
      }
      playersInGame.add(player);
      playerHand = new TreeSet<>(player.getHand().getCards());
      player.setHandValue(ha.getHandStrength(playerHand.toArray(new Card[0]), board.toArray(new Card[0])));
    }

    // Sort players in game by hand value
    Collections.sort(playersInGame, new Comparator<Player>() {
      @Override
      public int compare(Player player1, Player player2) {
        return player1.getHandValue().compareTo(player2.getHandValue());
      }
    });

    // Order player by rank
    int rankId = -1;
    int lastHandValue = 0;
    Map<Integer, List<Player>> payRanks = new HashMap<>();
    //    Starting with winners
    for (Player player : playersInGame) {
      if (player.getHandValue().getValue() != lastHandValue) {
        lastHandValue = player.getHandValue().getValue();
        rankId++;
        payRanks.put(rankId, new ArrayList<Player>());
      }
      payRanks.get(rankId).add(player);
    }
    //    And then folded
    if (playersFold.size() > 0) {
      rankId++;
      payRanks.put(rankId, new ArrayList<Player>());
      for (Player player : playersFold) {
        payRanks.get(rankId).add(player);
      }
    }

    // Distribute chips
    int maxBetForRank;
    boolean proceedToNextRank = true;
    // For each pay ranks starting from winners to loosers
    for (int i = 0; i < payRanks.size() && proceedToNextRank; i++) {
      proceedToNextRank = false;
      logger.info("Paying rank " + i);
      
      // Count the max bet amount to retribute for the current rank
      maxBetForRank = 0;
      for (Player player : payRanks.get(i)) {
        if (maxBetForRank < player.getBet()) {
          maxBetForRank = player.getBet();
        }
      }
      
      // Taking ships into inferiors ranks
      for (int j = i + 1; j < payRanks.size(); j++) {
        // Taking chips to all players in this rank
        for (Player playerToDebit : payRanks.get(j)) {
          int paieJoueurCourant = PlayerManager.debitBetStack(playerToDebit, maxBetForRank);

          // If the player bet stack is still positive we must proceed with the next rank
          if (playerToDebit.getBet() > 0) {
            proceedToNextRank = true;
          }

          // Crediting previous rank winners
          for (Player playerToCredit : payRanks.get(i)) {
            // TODO Handle chips division when paying
            PlayerManager.creditStack(playerToCredit, paieJoueurCourant * playerToCredit.getBet() / maxBetForRank);
          }
        }
      }

      // Reclaim bet stack to normal stack
      for (Player player : payRanks.get(i)) {
        PlayerManager.betToStack(player, player.getBet());
      }
    }
  }

  /**
   * Deal players cards and change state to player with a positive stack to IN
   * <p/>
   * Init a new Deck
   */
  private void dealPlayersCards() {
    deckManager.init();
    for (Player player : party.getPlayers()) {
      if (player.getState() == Player.State.LOOSE) {
        continue;
      }
      player.setState(Player.State.IN);
      player.setHand(new Hand(deckManager.pick(), deckManager.pick()));
    }
  }

  /**
   * A party is over when there is just one player in game
   *
   * @return true when there is just one player that is not in LOOSE state
   */
  protected boolean isOver() {
    return countPlayerNot(Player.State.LOOSE) < 2;
  }

  /**
   * Get bets from each players
   * Handle BigBlind, SmallBlind and Ants at flop time
   */
  private void handleBets() {
    // Handle big blind, small blind and ants during preflop
    if (party.getState() == Party.State.PREFLOP) {
      // Bet Ants
      if (blindManager.getBlindRank().getAnt() > 0) {
        for (Player player : party.getPlayers()) {
          bet(player, blindManager.getBlindRank().getAnt());
        }
      }
      // Bet small blind
      bet(nextPlayer(party.getDealer()), blindManager.getBlindRank().getSmallBlind());
      // Bet big blind
      bet(nextPlayer(), blindManager.getBlindRank().getBigBlind());
      betAmount = blindManager.getBlindRank().getBigBlind();
    } else {
      betAmount = 0;
    }
    // Minimal raise is BB amount
    raiseAmount = blindManager.getBlindRank().getBigBlind();

    // Asks bets to every player until we go back to the raiser
    while (nextPlayer().getBet() != raiseAmount) {
      // If players is allready all in there is nothing to ask
      if (isAllIn(currentPlayer())) {
        continue;
      }
      // If player is the only player left, there is nothing to ask
      if (countPlayer(Player.State.IN) == 1){
        continue;
      }
      handleBet(currentPlayer());
    }

    // if preflop ask action to the big blind if no one as raised big blind option
    if (party.getState() == Party.State.PREFLOP
        && currentPlayer().getBet() == blindManager.getBlindRank().getBigBlind()
        && countPlayer(Player.State.IN) > 1) {
      logger.info("Option to BB player");
      handleBet(currentPlayer());
    }
  }

  /**
   * Count player in a specific state
   */
  private int countPlayer(Player.State... statesToCount) {
    int acc = 0;
    for (Player player : party.getPlayers()) {
      if (Arrays.asList(statesToCount).contains(player.getState())) {
        acc++;
      }
    }
    return acc;
  }

  /**
   * Count player who are NOT in a specific state
   */
  private int countPlayerNot(Player.State... statesToCount) {
    int acc = 0;
    for (Player player : party.getPlayers()) {
      if (! Arrays.asList(statesToCount).contains(player.getState())) {
        acc++;
      }
    }
    return acc;
  }

  private void handleBet(Player player) {
    Action action = askAction(party.getPlayers().indexOf(player));
    switch (action.getType()) {
      case CALL:
        bet(player, betAmount);
        logger.info("{} calls {}", player.getName(), betAmount);
        break;
      case FOLD:
        player.setState(Player.State.FOLD);
        logger.info("{} folds", player.getName());
        break;
      case RAISE:
        // If raise is < than minimum raise and the player is not going all in
        if (action.getAmount() < raiseAmount && action.getAmount() != player.getStack()) {
          // Raise isn't enough so the player do a simple call
          bet(player, betAmount);
          logger.info("{} calls {}", player.getName(), betAmount);
        }
        // Player is raising or going all in
        int amount = bet(currentPlayer(), action.getAmount());
        // Raise is effective
        if (amount > raiseAmount) {
          raiseAmount = betAmount - amount;
          betAmount = amount;
        }
        break;
    }
  }

  private boolean isAllIn(Player player) {
    return player.getStack() == 0;
  }

  private Action askAction(int playerIndex) {
    logger.info("Asking {} to bet for [{}]", party.getPlayers().get(playerIndex).getName(), party.getPlayers().get(playerIndex).getHand());
    return commanders.get(playerIndex).request();
  }

  /**
   * Get the next player IN after indexPlayer
   */
  private Player nextPlayer(int indexPlayer) {
    return party.getPlayers().get(nextPlayerIdx(indexPlayer));
  }

  /**
   * Get index of the next player IN after indexPlayer
   */
  private int nextPlayerIdx(int indexPlayer) {
    currentPlayerIndex = indexPlayer;
    return nextPlayerIdx();
  }

  /**
   * Get the next player IN
   */
  private Player nextPlayer() {
    return party.getPlayers().get(nextPlayerIdx());
  }

  /**
   * Get index of the next player IN
   *
   * @return
   */
  private int nextPlayerIdx() {
    for (int i = 1; i <= party.getPlayers().size(); i++) {
      int indexToCheck = (currentPlayerIndex + i) % party.getPlayers().size();
      if (party.getPlayers().get(indexToCheck).getState() == Player.State.IN) {
        currentPlayerIndex = indexToCheck;
        return currentPlayerIndex;
      }
    }
    throw new RuntimeException("Can't find the next player, everybody is OUT");
  }

  private Player currentPlayer() {
    return party.getPlayers().get(currentPlayerIndex);
  }

  /**
   * Make a bet for a player
   * <p/>
   * The bet is limited by the player's stack
   *
   * @param player who made the bet
   * @param amount requested to bet
   * @return final bet amount
   */
  private int bet(Player player, int amount) {
    // Only player that are IN can bet
    if (player.getState() != Player.State.IN) {
      return 0;
    }
    // Player can't bet more than his stack
    if (player.getStack() < amount) {
      amount = player.getStack();
    }
    player.setBet(player.getBet() + amount);
    player.setStack(player.getStack() - amount);

    logger.info("{} bets {} [{},{}]", player.getName(), amount, player.getBet(), player.getStack());
    return amount;
  }

  private void initButton() {
    party.setDealer(Double.valueOf(Math.random() * party.getSize()).intValue());
  }

  private void setNextDealer() {
    // Set the button to the next player
    party.setDealer(nextPlayerIdx(party.getDealer()));
  }
}
