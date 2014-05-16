package io.github.sdaclin.jpok.model;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 05/05/14
 * Time: 16:43
 */
public class Action {
  public enum Type {
    SMALL_BLIND,
    BIG_BLIND,
    ANT,
    FOLD,
    CALL,
    RAISE,
    ALL_IN;
  }

  private final Type type;
  private int amount;

  private Action(Type type, int amount ){
    this.type = type;
    this.amount = amount;
  }

  public Type getType() {
    return type;
  }

  public int getAmount() {
    return amount;
  }

  public static class Builder {
    private int amount;
    private Type type;

    public Action build(){
      return new Action(type,amount);
    }

    public Builder smallBlind(int amount) {
      this.type = Type.SMALL_BLIND;
      this.amount = amount;
      return this;
    }

    public Builder bigBlind(int amount) {
      this.type = Type.BIG_BLIND;
      this.amount = amount;
      return this;
    }

    public Builder ant(int amount) {
      this.type = Type.ANT;
      this.amount = amount;
      return this;
    }


    public Builder fold() {
      this.type = Type.FOLD;
      return this;
    }

    public Builder call() {
      this.type = Type.CALL;
      return this;
    }

    public Builder call(int amount) {
      this.type = Type.CALL;
      this.amount = amount;
      return this;
    }

    public Builder raise(int amount) {
      this.type = Type.RAISE;
      this.amount = amount;
      return this;
    }

    public Builder allIn(int amount) {
      this.type = Type.ALL_IN;
      this.amount = amount;
      return this;
    }
  }
}
