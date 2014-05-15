package io.github.sdaclin.jpok.manager.commander;

import io.github.sdaclin.jpok.model.Action;
import io.github.sdaclin.jpok.model.Party;
import io.github.sdaclin.jpok.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 08/05/14
 * Time: 13:13
 */
public interface PlayerCommander {
  public void notify(Party party);
  public void notify(Player player, Action action);
  public Action request();
}
