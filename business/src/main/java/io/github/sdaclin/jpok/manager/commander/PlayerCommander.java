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
  /**
   * The room tells which seat the player occupy
   * @param playerIndex the index of the player in party.getPlayers().get(playerIndex)
   */
  public void notify(int playerIndex);

  /**
   * the room notify the party state when starting pre flop, flop, turn and river
   * @param party
   */
  public void notify(Party party);

  /**
   * The room notify a player's action
   * @param player
   * @param action
   */
  public void notify(Player player, Action action);

  /**
   * The room asks for a move
   * @return
   */
  public Action request();
}
