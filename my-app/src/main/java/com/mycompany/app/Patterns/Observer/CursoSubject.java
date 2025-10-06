package com.mycompany.app.Patterns.Observer;

import java.util.concurrent.CopyOnWriteArrayList;

public class CursoSubject {
  private static final CursoSubject INSTANCE = new CursoSubject();
  private final CopyOnWriteArrayList<CursoObserver> observers = new CopyOnWriteArrayList<>();

  private CursoSubject() {}

  public static CursoSubject getInstance() { return INSTANCE; }

  public void register(CursoObserver o) { if (o != null) observers.addIfAbsent(o); }
  public void unregister(CursoObserver o) { observers.remove(o); }

  public void notify(CursoEvent e) {
    for (CursoObserver o : observers) {
      try { o.onCursoChanged(e); } catch (Exception ex) { ex.printStackTrace(); }
    }
  }
}
