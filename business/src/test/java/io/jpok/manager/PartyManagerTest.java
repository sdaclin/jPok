package io.jpok.manager;

import io.jpok.manager.commander.bot.DumbBot;
import io.jpok.model.BlindRank;
import io.jpok.model.BlindSchema;
import io.jpok.model.Party;
import io.jpok.model.Player;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 08/05/14
 * Time: 13:04
 */
public class PartyManagerTest {
  private BlindSchema blindSchema;
  private Party party;
  private PartyManager partyManager;


  @Before
  public void setUp() throws Exception {
    // Create a schema to handle blind leveling up during the game
    blindSchema = new BlindSchema.Builder()
        .addRank(new BlindRank(5, 20, 10, 0))
        .addRank(new BlindRank(5, 40, 20, 0))
        .addRank(new BlindRank(5, 60, 30, 0))
        .addRank(new BlindRank(5, 100, 50, 0))
        .addRank(new BlindRank(5, 200, 100, 0))
        .addRank(new BlindRank(5, 400, 200, 20))
        .build();

    // Create a party for 6 player according to the blind schema and with a 2.000 starting stack
    party = new Party(6, blindSchema, 2000);

    // Register 6 players
    partyManager = new PartyManager(party);
    partyManager.registerPlayer("Alice",new DumbBot());
    partyManager.registerPlayer("Bob",new DumbBot());
    partyManager.registerPlayer("Cala",new DumbBot());
    partyManager.registerPlayer("Damon",new DumbBot());
    partyManager.registerPlayer("Edith",new DumbBot());
    partyManager.registerPlayer("Felix",new DumbBot());
  }

  @Test(expected = Exception.class)
  public void testRegisterPlayer() throws Exception {
    PartyManager pm = new PartyManager(new Party(1,blindSchema,10));
    pm.registerPlayer("Sylvain",new DumbBot());
    pm.registerPlayer("Sylvain",new DumbBot());
  }

  @Test
  public void testIsOver() throws Exception {
    PartyManager pm = new PartyManager(party);
    for (Player player: party.getPlayers()) {
      player.setState(Player.State.LOOSE);
    }
    party.getPlayers().get(1).setState(Player.State.IN);
    assertTrue(pm.isOver());
  }
}
