package com.marcelo.bibliotech;

import com.marcelo.bibliotech.view.BibliotecaView;
import com.marcelo.bibliotech.controller.BibliotecaController;

public class BibliotechApplication {
     public static void main(String[] args) {
        BibliotecaController controller = new BibliotecaController(null);
        BibliotecaView view = new BibliotecaView(controller);
        view.iniciar();
     }
}
