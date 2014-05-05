package io.jpok.manager;

import io.jpok.model.BlindRank;
import io.jpok.model.BlindSchema;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 03/05/14
 * Time: 17:17
 */
public class BlindManager {
  private final BlindSchema schema;
  private short activeRank;
  private int count = 0;

  public BlindManager(BlindSchema schema){
    this.schema = schema;
    activeRank = 0;
  }

  public BlindRank getBlindRank() {
    return schema.getRank(activeRank);
  }

  public void handleRankChange() {
    if (count++ % 10 == 0) {
      activeRank++;
    }
  }
}
