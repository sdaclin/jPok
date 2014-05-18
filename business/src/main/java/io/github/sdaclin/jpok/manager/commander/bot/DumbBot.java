package io.github.sdaclin.jpok.manager.commander.bot;

import io.github.sdaclin.jpok.manager.commander.PlayerCommander;
import io.github.sdaclin.jpok.model.Action;
import io.github.sdaclin.jpok.model.Card;
import io.github.sdaclin.jpok.model.Party;
import io.github.sdaclin.jpok.model.Player;

/**
 * A bot that play only 1 hand over 3 and call after raise 1 time over 2
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 08/05/14
 * Time: 13:13
 */
public class DumbBot extends AbstractBot implements PlayerCommander {
  private boolean play = false;

  /**
   * Append when starting pre flop, flop, tun and river
   * @param party
   */
  @Override
  public void notify(Party party) {
    updateBotState(party);
    if (party.getState() == Party.State.PREFLOP) {
      play = (int)(Math.random() * 3) == 0;
    }
  }

  /**
   * Append each time a player makes an action
   */
  @Override
  public void notify(Player player, Action action) {
    if (action.getType() == Action.Type.RAISE) {
      play = (int)(Math.random() * 2) == 0;
    }
  }

  @Override
  public Action request() {
    if (play) {
      return new Action.Builder().call().build();
    }else{
      return new Action.Builder().fold().build();
    }
  }
}
