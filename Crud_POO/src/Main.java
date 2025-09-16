import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GestionFrutas miGestion = new GestionFrutas();
        double total;
        boolean continua = true;

        while(continua){
             String menu = "--- CRUD de Frutas ---\n"
                 + "1. Crear fruta\n"
                 + "2. Listar todas las frutas\n"
                 + "3. Buscar fruta por ID\n"
                 + "4. Actualizar precio de fruta\n"
                 + "5. Eliminar fruta\n"
                 + "6. Salir\n\n"
                 + "Elige una opción:";

             String opcionStr = JOptionPane.showInputDialog(null, menu);
             if (opcionStr == null){
                 continua = false;
             }else {
                 try{

                     int option = Integer.parseInt(opcionStr);
                     switch (option){
                         case 1:
                             String nombre = JOptionPane.showInputDialog(null, "El nombre de la fruta");
                             if (nombre != null && !nombre.trim().isEmpty()){
                                 double pesoKg = Double.parseDouble(JOptionPane.showInputDialog(null, "peso de la fruta"));
                                 double precio = Double.parseDouble(JOptionPane.showInputDialog(null, "precio de la fruta"));
                                 String color = String.format(JOptionPane.showInputDialog(null, "color para la fruta"));
                                 boolean agregado = miGestion.agregarFruta(nombre, color, precio, pesoKg);
                                 if (agregado){
                                     JOptionPane.showMessageDialog(null, "Fruta agregada con exito");
                                 } else {
                                     JOptionPane.showMessageDialog(null, "La fruta "+ nombre + " Ya existe", "Error", 0);
                                 }
                                 break;
                             }
                             JOptionPane.showMessageDialog(null, "El nombre esta vacio", "Error", 0);
                             break;
                         case 2:
                             String lista = miGestion.ListarFrutas();
                             JOptionPane.showMessageDialog(null, lista);
                             break;
                         case 3:
                             int IdBuscar = Integer.parseInt(JOptionPane.showInputDialog(null, "ID de la fruta que estas buscando"));
                             Frutas frutaEncontrada = miGestion.buscarFrutasPorId(IdBuscar);
                             if (frutaEncontrada != null){
                                 JOptionPane.showMessageDialog(null, "Fruta encontrada:\n" + frutaEncontrada);
                             } else {
                                 JOptionPane.showMessageDialog(null, "No se encontro la fruta con este ID:"+ IdBuscar);
                             }
                             break;
                         case 4:
                             int IdActualizar = Integer.parseInt(JOptionPane.showInputDialog(null, "ID de la fruta que quieres actualizar."));
                             double nuevoPrecio = Double.parseDouble(JOptionPane.showInputDialog(null, "nuevo precio de la fruta"));
                             if (miGestion.actualizar(IdActualizar, nuevoPrecio)){
                                 JOptionPane.showMessageDialog(null, "Precio actualizado");
                             } else {
                                 JOptionPane.showMessageDialog(null, "nio se pudo actualizar el precio");
                             }
                             break;
                         case 5:
                             int IdEliminar = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingresa el ID que quieres eliminar"));
                             if (miGestion.eliminarFruta(IdEliminar)){
                                 JOptionPane.showMessageDialog(null, "Esta fruta fue eliminada con exito");
                             } else {
                                 JOptionPane.showMessageDialog(null, "No se encontro la fruta con este ID"+ IdEliminar);
                             }
                             break;
                         case 6: // Salir
                             JOptionPane.showMessageDialog(null, "¡Hasta luego!");
                             System.exit(0); // Cierra la aplicación
                             break;

                         default:
                             JOptionPane.showMessageDialog(null, "Opción no válida. Intenta de nuevo.");
                             break;
                     }
                 } catch (NumberFormatException e) {
                     JOptionPane.showMessageDialog(null, "Error: Debes ingresar un número válido.");
                 } catch (Exception e) {
                     JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado: " + e.getMessage());
                 }
             }
        }
    }
}
