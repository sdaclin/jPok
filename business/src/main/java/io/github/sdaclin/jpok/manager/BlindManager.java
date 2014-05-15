package io.github.sdaclin.jpok.manager;

import io.github.sdaclin.jpok.model.BlindRank;
import io.github.sdaclin.jpok.model.BlindSchema;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 03/05/14
 * Time: 17:17
 */
public class BlindManager {
  private final BlindSchema schema;
  private int count = 0;

  public BlindManager(BlindSchema schema){
    this.schema = schema;
    schema.setActiveRank(0);
  }

  public BlindRank getBlindRank() {
    return schema.getRanks().get(schema.getActiveRank());
  }

  /**
   * Verify if conditions are good to go to the next level of blind
   */
  public void handleRankChanging() {
    count = count+1 % 10;
    if (count == 0) {
      schema.setActiveRank(schema.getActiveRank()+1);
    }
  }
}
