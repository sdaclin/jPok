package io.jpok.model;

/**
 * Created with IntelliJ IDEA.
 * User: Sylvain
 * Date: 03/05/14
 * Time: 13:39
 */
public class Player {
  private final short id;
  private final String name;
  private int stack;

  public Player(short id, String name, int stack) {
    this.id = id;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setStack(int stack) {
    this.stack = stack;
  }

  public int getStack() {
    return stack;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Player && ((Player) obj).id == this.id;
  }
}
