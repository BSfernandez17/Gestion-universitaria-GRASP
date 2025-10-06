package com.mycompany.app.Patterns.Observer;

import java.util.concurrent.CopyOnWriteArrayList;

public class CursoSubject {
  private static final CursoSubject INSTANCE = new CursoSubject();
  // Register a default observer so headless runs (which may not load UiContext)
  // still get notifications. This is safe because CursoCreationObserver is
  // lightweight and uses a background executor.
  static {
    try {
      INSTANCE.register(new CursoCreationObserver());
      System.out.println("[CursoSubject] default CursoCreationObserver registered");
    } catch (Throwable t) {
      System.out.println("[CursoSubject] failed to register default observer: " + t.getMessage());
    }
  }
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
