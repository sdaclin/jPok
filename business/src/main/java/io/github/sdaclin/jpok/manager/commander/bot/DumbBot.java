package io.github.sdaclin.jpok.manager.commander.bot;

import io.github.sdaclin.jpok.manager.commander.PlayerCommander;
import io.github.sdaclin.jpok.model.Action;
import io.github.sdaclin.jpok.model.Party;
import io.github.sdaclin.jpok.model.Player;

/**
 * A bot that play only 1 hand over 3 and call over bet 1 time over 2
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 08/05/14
 * Time: 13:13
 */
public class DumbBot implements PlayerCommander {
  private boolean play = false;

  @Override
  public void notify(Party party) {
    play = (Math.random() * 3 == 0);
  }

  @Override
  public void notify(Player player, Action action) {
    if (action.getType() == Action.Type.RAISE) {
      play = (Math.random() * 2 == 0);
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
