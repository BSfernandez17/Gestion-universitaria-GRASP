package com.mycompany.app.Services;

import com.mycompany.app.Model.CursoProfesor;
import java.util.ArrayList;
import java.util.List;

public class CursosProfesores implements Servicios {
  private List<CursoProfesor> listado = new ArrayList<>();

  public void inscribir(CursoProfesor cursoProfesor) {
    listado.add(cursoProfesor);
  }

  public void guardarInformacion(CursoProfesor cursoProfesor) {
    // guardar en BD o archivo
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
