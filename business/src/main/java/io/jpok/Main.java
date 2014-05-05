package io.jpok;

import io.jpok.manager.PartyManager;
import io.jpok.model.BlindRank;
import io.jpok.model.BlindSchema;
import io.jpok.model.Party;

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
        .addRank(new BlindRank(5, 40, 20, 0))
        .addRank(new BlindRank(5, 60, 30, 0))
        .addRank(new BlindRank(5, 100, 50, 0))
        .addRank(new BlindRank(5, 200, 100, 0))
        .addRank(new BlindRank(5, 400, 200, 20))
        .build();

    // Create a party for 6 player according to the blind schema and with a 2.000 starting stack
    Party party = new Party(6, blindSchema, 2000);

    // Create a new manager to handle this party
    partyManager = new PartyManager(party);

    // Registering players
    partyManager.registerPlayer("Sylvain");
    partyManager.registerPlayer("Jessica");
    partyManager.registerPlayer("Alice");
    partyManager.registerPlayer("Antoine");
    partyManager.registerPlayer("Bob");
    partyManager.registerPlayer("Brian");

    // Start party
    partyManager.play();
  }
}