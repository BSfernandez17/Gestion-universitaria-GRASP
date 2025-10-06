package com.mycompany.app.Patterns.Observer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class CursoCreationObserver implements CursoObserver {

  private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(new ThreadFactory() {
    @Override
    public Thread newThread(Runnable r) {
      Thread t = new Thread(r, "CursoObserverWorker");
      t.setDaemon(true);
      return t;
    }
  });

  static {
    // Shutdown executor gracefully when JVM exits
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        EXECUTOR.shutdown();
        if (!EXECUTOR.awaitTermination(500, TimeUnit.MILLISECONDS)) {
          EXECUTOR.shutdownNow();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }, "CursoObserver-Shutdown"));
  }

  @Override
  public void onCursoChanged(CursoEvent event) {
    if (event == null) return;
      System.out.println("[CursoCreationObserver] onCursoChanged received event: action=" + event.getAction() + " id=" + event.getId() + " thread=" + Thread.currentThread().getName());
    switch (event.getAction()) {
      case INSERT:
        submitAsyncTask(event.getId(), "nuevo curso");
        break;
      case UPDATE:
        submitAsyncTask(event.getId(), "curso actualizado");
        break;
      case DELETE:
        submitAsyncTask(event.getId(), "curso eliminado");
        break;
      default:
        // no-op
    }
  }

  private void submitAsyncTask(Object id, String reason) {
    EXECUTOR.submit(() -> {
      try {
          System.out.println("[CursoCreationObserver] Task started for " + reason + " id=" + id + " thread=" + Thread.currentThread().getName());
        Thread.sleep(50);
          System.out.println("[CursoCreationObserver] Task finished for " + reason + " id=" + id + " thread=" + Thread.currentThread().getName());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
  }
}
