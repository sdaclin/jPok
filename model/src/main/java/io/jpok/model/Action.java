package io.jpok.model;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 05/05/14
 * Time: 16:43
 */
public class Action {
  public enum Type {
    FOLD,
    CALL,
    RAISE;
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

    public Builder raise(int amount) {
      this.type = Type.RAISE;
      this.amount = amount;
      return this;
    }

    public Builder call() {
      this.type = Type.CALL;
      return this;
    }

    public Builder fold() {
      this.type = Type.FOLD;
      return this;
    }
  }
}
