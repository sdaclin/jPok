package io.github.sdaclin.jpok.manager.commander.bot;

import io.github.sdaclin.jpok.model.Party;
import io.github.sdaclin.jpok.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 18/05/14
 * Time: 10:12
 */
public class AbstractBot {
  private int playerIndex;
  protected Player bot;

  /**
   * Append when player is registered in the party
   * @param playerIndex
   */
  public void notify(int playerIndex) {
    this.playerIndex = playerIndex;
  }

  /**
   * Update me with party state
   */
  protected void updateBotState(Party party){
    bot = party.getPlayers().get(playerIndex);
  }
}
