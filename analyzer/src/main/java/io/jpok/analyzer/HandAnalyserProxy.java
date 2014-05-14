package io.jpok.analyzer;

import io.jpok.model.Card;
import io.jpok.model.HandValue;

/**
 * Proxy utilise pour calculer la valeur d'une main
 *
 * @author avianey
 */
public class HandAnalyserProxy implements HandAnalyser {

  private static final HandAnalyser analyser2 = new HandAnalyser2();
  private static final HandAnalyser analyser5 = new HandAnalyser5();
  private static final HandAnalyser analyser6 = new HandAnalyser6();
  private static final HandAnalyser analyser7 = new HandAnalyser7();

  /*
   * (non-Javadoc)
   * @see poker.common.analyser.io.jpok.analyzer.HandAnalyser#getHandStrength(poker.common.model.Player, poker.common.model.Table)
   */
  public HandValue getHandStrength(Card[] hand, Card[] table) {
    switch (table.length) {
      case 0:
        return analyser2.getHandStrength(hand, table);
      case 3:
        return analyser5.getHandStrength(hand, table);
      case 4:
        return analyser6.getHandStrength(hand, table);
      case 5:
        return analyser7.getHandStrength(hand, table);
      default:
        return null;
    }
  }
}
