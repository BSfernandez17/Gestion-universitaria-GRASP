package com.mycompany.app.Services;

import java.util.List;

//----------------------------------------------INTERFAZ-------------------------------------------------------------------------------------------
public interface Servicios {
  String imprimirPosicion(int posicion);

  Integer cantidadActual();

  List<String> imprimirListado();
}
