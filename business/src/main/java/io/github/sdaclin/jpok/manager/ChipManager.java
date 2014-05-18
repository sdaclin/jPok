package io.github.sdaclin.jpok.manager;

import io.github.sdaclin.jpok.model.Player;
import org.slf4j.LoggerFactory;

/**
 * Manage all chips move between stacks or players
 *
 * There are 2 type of move :
 *  * From player stack to player bet stack
 *  * From player bet stack to someone stack (when retributing players). "someone" including himself
 *
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 13/05/14
 * Time: 13:00
 */
public class ChipManager {
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ChipManager.class);

  /**
   * Moving chips from player stack to players bet stack
   * @param player
   * @param amount the amount to try to take
   */
  public static void bet(Player player, int amount) {
    if (player.getStack() < amount){
      throw new RuntimeException("["+player.getName()+"] has not enough chip to bet ["+amount+"]");
    }
    player.setStack(player.getStack() - amount);
    player.setBet(player.getBet() + amount);
    logger.info("[{}] bets [{}]", player.getName(), amount);
  }

  /**
   * Moving chips from player stack to players bet stack
   * If players has not enough chips, all his remaining chips are moved instead
   * @param player
   * @param amount
   */
  public static int betForced(Player player, int amount) {
    if (player.getStack() < amount){
      amount = player.getStack();
    }
    player.setStack(player.getStack() - amount);
    player.setBet(player.getBet() + amount);
    logger.info("[{}] bets [{}]", player.getName(), amount);
    return amount;
  }

  /**
   * Transfer betted chips from a player (looser) to another (winner)
   * @param from looser
   * @param to winner
   * @param amount
   * return the real amount transfered
   */
  public static void transfer(Player from, Player to, int amount) {
    if (from.getBet() < amount) {
      throw new RuntimeException("["+from.getName()+"] has not enough chip in bet stack to transfer ["+amount+"]");
    }
    from.setBet(from.getBet() - amount);
    to.setStack(to.getStack() + amount);
    logger.info("[{}] transfers [{]] chips to [{}]", from.getName(), amount, to.getName());
  }

  /**
   * Try to take amount chips in player's bet stack
   * @param player
   * @param amount the amount to try to take
   * @return the real amount taken (if the stack is less than amount wanted)
   */
  @Deprecated
  public static int debitBetStack(Player player, int amount) {
    if (player.getBet() < amount){
      logger.debug("[{}] bet stack is smaller than [{}]", amount, player.getName());
      amount = player.getBet();
    }
    logger.info("Debiting [{}] bet stack from [{}]", amount, player.getName());
    player.setBet(player.getBet() - amount);
    return amount;
  }

  @Deprecated
  public static void creditStack(Player player, int amount) {
    logger.info("Crediting [{}] stack from [{}]", amount, player.getName());
    player.setStack(player.getStack() + amount);
  }

  /**
   * Move chips from stack to bet stack
   * @param player
   * @param amount
   */
  @Deprecated
  public static void stackToBet(Player player, int amount) {
    if (player.getStack() < amount) {
      throw new RuntimeException("Not enough chips");
    }
    logger.info("Moving [{}] chips from stack to bet", amount);
    player.setBet(player.getBet() + amount);
    player.setStack(player.getStack() - amount);
  }

  /**
   * Move chips from bet to stack
   * @param player
   * @param amount
   */
  @Deprecated
  public static void betToStack(Player player, int amount) {
    if (player.getBet() < amount) {
      throw new RuntimeException("Not enough chips");
    }
    logger.info("Moving [{}] chips from bet to stack", amount);
    player.setStack(player.getStack() + amount);
    player.setBet(player.getBet() - amount);
  }
}
