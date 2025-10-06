package com.mycompany.app.Services;

import com.mycompany.app.Model.Inscripcion;
import java.util.ArrayList;
import java.util.List;

public class CursosInscritos implements Servicios {
  private List<Inscripcion> listado = new ArrayList<>();

  public void inscribir(Inscripcion inscripcion) {
    listado.add(inscripcion);
  }

  public void eliminar(Inscripcion inscripcion) {
    listado.remove(inscripcion);
  }

  public void actualizar(Inscripcion inscripcion) {
    // lógica de actualización
  }

  public void guardarInformacion(Inscripcion inscripcion) {
    // guardar en archivo o BD
  }

  public void cargarDatos() {
    // cargar desde archivo
  }

  @Override
  public String imprimirPosicion(int posicion) {
    return listado.get(posicion).toString();
  }

  @Override
  public Integer cantidadActual() {
    return listado.size();
  }

  @Override
  public List<String> imprimirListado() {
    return listado.stream().map(Object::toString).toList();
  }
}
