package io.github.sdaclin.jpok;

import io.github.sdaclin.jpok.manager.PartyManager;
import io.github.sdaclin.jpok.manager.commander.bot.DumbBot;
import io.github.sdaclin.jpok.model.BlindRank;
import io.github.sdaclin.jpok.model.BlindSchema;
import io.github.sdaclin.jpok.model.Party;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 03/05/14
 * Time: 11:09
 */
public class Main {
  private static PartyManager partyManager;
  public static void main(String[] args) {

    // Create a schema to handle blind leveling up during the game
    BlindSchema blindSchema = new BlindSchema.Builder()
        .addRank(new BlindRank(5, 20, 10, 0))
        .addRank(new BlindRank(5, 30, 15, 0))
        .addRank(new BlindRank(5, 50, 25, 0))
        .addRank(new BlindRank(5, 100, 50, 0))
        .addRank(new BlindRank(5, 150, 75, 0))
        .addRank(new BlindRank(5, 200, 100, 20))
        .addRank(new BlindRank(5, 400, 200, 20))
        .addRank(new BlindRank(5, 600, 300, 20))
        .build();

    // Create a party for 6 player according to the blind schema and with a 2.000 starting stack
    Party party = new Party(6, blindSchema, 1500);

    // Create a new manager to handle this party
    partyManager = new PartyManager(party);

    // Registering players
    partyManager.registerPlayer("Sylvain", new DumbBot());
    partyManager.registerPlayer("Jessica", new DumbBot());
    partyManager.registerPlayer("Alice", new DumbBot());
    partyManager.registerPlayer("Antoine", new DumbBot());
    partyManager.registerPlayer("Bob", new DumbBot());
    partyManager.registerPlayer("Brian", new DumbBot());

    // Start party
    partyManager.play();
  }
}
