import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        String nombre = JOptionPane.showInputDialog(null,"Cual es tu nombre");
        String apellido = JOptionPane.showInputDialog(null, "Cual es tu apellido");

        try(PrintWriter writer = new PrintWriter("Registro.txt")) {
            writer.println("Nombre: "+ nombre);
            writer.println("Apellido: "+ apellido);
            JOptionPane.showMessageDialog(null, "Registro guardado con exito");
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "No se pudo guardar el registro: " + e.getMessage());
        }

        StringBuffer leido = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new FileReader("Registro.txt"))){
            String linea;
            leido.append("Se leyó el siguiente archivo: \n\n");
            while ((linea = reader.readLine()) != null){
                leido.append(linea).append("\n");
            }
            JOptionPane.showMessageDialog(null, leido.toString());
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "No se pudo leer el archivo: " + e.getMessage());
        }
    }
}
