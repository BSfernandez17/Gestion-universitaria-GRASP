package com.mycompany.app.Services;

import com.mycompany.app.Model.Persona;
import java.util.ArrayList;
import java.util.List;

public class InscripcionesPersonas implements Servicios {
  private List<Persona> listado = new ArrayList<>();

  public void inscribir(Persona persona) {
    listado.add(persona);
  }

  public void eliminar(Persona persona) {
    listado.remove(persona);
  }

  public void actualizar(Persona persona) {
    // lógica de actualización...
  }

  public void guardarInformacion(Persona persona) {
    // guardar en BD o archivo
  }

  public void cargarDatos() {
    // cargar desde archivo o BD
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
