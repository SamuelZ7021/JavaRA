package com.example1.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HolaMundoController {

    @GetMapping("/hola")
    public String decirHola(){
        return "Esta es mi primera prueba de framework con API y Spring Boot!";
    }

    @GetMapping("/hola/{nombre}")
    public String saludoPerzonalizado(@PathVariable String nombre){
        return "Hola " + nombre  + decirHola();
    }

    @GetMapping("/calcular")
    public String calcular(@RequestParam Integer numero, @RequestParam Integer numero2){
        int resultado = numero * numero2;
        return "El calculo de  " + numero + " y " + numero2 + " es: " + resultado;
    }

}
