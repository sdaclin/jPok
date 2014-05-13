package io.jpok.manager;

import io.jpok.model.Player;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 13/05/14
 * Time: 13:00
 */
public class PlayerManager {
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PlayerManager.class);

  /**
   * Try to take amount chips in player's bet stack
   * @param player
   * @param amount the amount to try to take
   * @return the real amount taken (if the stack is less than amount wanted)
   */
  public static int debitBetStack(Player player, int amount) {
    if (player.getBet() < amount){
      logger.debug("[{}] bet stack is smaller than [{}]", player.getName(), amount);
      amount = player.getBet();
    }
    logger.info("Debiting [{}] bet stack from [{}]", amount, player.getName());
    player.setBet(player.getBet() - amount);
    return amount;
  }

  public static void creditStack(Player player, int amount) {
    logger.info("Crediting [{}] stack from [{}]", amount, player.getName());
    player.setStack(player.getStack() + amount);
  }
}