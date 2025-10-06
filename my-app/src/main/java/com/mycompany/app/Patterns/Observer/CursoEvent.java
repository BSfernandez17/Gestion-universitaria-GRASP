package com.mycompany.app.Patterns.Observer;

public class CursoEvent {
  public enum Action { INSERT, UPDATE, DELETE }

  private final Action action;
  private final Object id;

  public CursoEvent(Action action, Object id) {
    this.action = action;
    this.id = id;
  }

  public Action getAction() { return action; }
  public Object getId() { return id; }
}
