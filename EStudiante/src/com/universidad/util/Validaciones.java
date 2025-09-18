package com.universidad.util;

public class Validaciones {

    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean esEdadValida(int edad) {
        return edad >= 0;
    }

    public static boolean esNotaValida(double nota) {
        return nota >= 0 && nota <= 5;
    }
}
