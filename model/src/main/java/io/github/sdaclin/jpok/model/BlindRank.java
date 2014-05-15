package io.github.sdaclin.jpok.model;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 03/05/14
 * Time: 11:43
 */
public class BlindRank {
    private int duration;
    private int smallBlind;
    private int bigBlind;
    private int ant;

    /**
     * A blind rank to use during the party
     * @param duration in minutes
     * @param smallBlind
     * @param bigBlind
     * @param ant
     */
    public BlindRank(int duration, int bigBlind, int smallBlind, int ant){
        this.duration = duration;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.ant = ant;
    }

    public int getDuration() {
        return duration;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getAnt() {
        return ant;
    }
}
