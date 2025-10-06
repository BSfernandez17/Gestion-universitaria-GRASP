package com.mycompany.app.Patterns.Observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CursoSubject {
  private final List<CursoObserver> observers = new CopyOnWriteArrayList<>();

  public void subscribe(CursoObserver o) { observers.add(o); }
  public void unsubscribe(CursoObserver o) { observers.remove(o); }

  public void notify(CursoEvent event) {
    for (CursoObserver o : observers) {
      try { o.onCursoChanged(event); } catch (Exception e) { e.printStackTrace(); }
    }
  }
}
