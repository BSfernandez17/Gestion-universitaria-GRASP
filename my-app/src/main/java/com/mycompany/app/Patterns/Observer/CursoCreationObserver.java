package com.mycompany.app.Patterns.Observer;

public class CursoCreationObserver implements CursoObserver {

  @Override
  public void onCursoChanged(CursoEvent event) {
    if (event == null) return;
    if (event.getAction() == CursoEvent.Action.INSERT) {
      Thread t = new Thread(() -> {
        try {
          System.out.println("[CursoCreationObserver] Hilo iniciado para nuevo curso id=" + event.getId());
          Thread.sleep(50);
          System.out.println("[CursoCreationObserver] Procesamiento finalizado para curso id=" + event.getId());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }, "CursoObserver-" + event.getId());
      t.setDaemon(true);
      t.start();
    }
  }
}
