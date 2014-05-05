package io.jpok.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 03/05/14
 * Time: 11:27
 */
public class BlindSchema {
    private BlindSchema(List<BlindRank> ranks){
        this.ranks = ranks;
    }
    private List<BlindRank> ranks;

  public BlindRank getRank(int index) {
    return ranks.get(index);
  }

    public static class Builder {
        private List<BlindRank> ranks = new ArrayList<>();

        public Builder addRank(BlindRank rank) {
            ranks.add(rank);
            return this;
        }

        public BlindSchema build() {
            if (ranks.size() == 0){
                throw new IllegalStateException("Ranks not set");
            }
            return new BlindSchema(ranks);
        }
    }
}
