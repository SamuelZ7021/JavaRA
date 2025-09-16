import javax.swing.JOptionPane;

public class Menu {
    private GestionFrutas gestionFrutas;

    public Menu(GestionFrutas gestionFrutas) {
        this.gestionFrutas = gestionFrutas;
    }

    public void mostrarMenu() {
        boolean continua = true;
        while (continua) {
            String menu = "--- CRUD de Frutas ---\n"
                    + "1. Crear fruta\n"
                    + "2. Listar todas las frutas\n"
                    + "3. Buscar fruta por ID\n"
                    + "4. Actualizar precio de fruta\n"
                    + "5. Eliminar fruta\n"
                    + "6. Salir\n\n"
                    + "Elige una opción:";

            String opcionStr = JOptionPane.showInputDialog(null, menu);
            if (opcionStr == null) {
                continua = false;
            } else {
                try {
                    int option = Integer.parseInt(opcionStr);
                    switch (option) {
                        case 1:
                            crearFruta();
                            break;
                        case 2:
                            listarFrutas();
                            break;
                        case 3:
                            buscarFruta();
                            break;
                        case 4:
                            actualizarFruta();
                            break;
                        case 5:
                            eliminarFruta();
                            break;
                        case 6: // Salir
                            JOptionPane.showMessageDialog(null, "¡Hasta luego!");
                            continua = false;
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

    private void crearFruta() {
        String nombre = JOptionPane.showInputDialog(null, "El nombre de la fruta");
        if (nombre != null && !nombre.trim().isEmpty()) {
            double pesoKg = Double.parseDouble(JOptionPane.showInputDialog(null, "peso de la fruta"));
            double precio = Double.parseDouble(JOptionPane.showInputDialog(null, "precio de la fruta"));
            String color = JOptionPane.showInputDialog(null, "color para la fruta");

            boolean agregado = gestionFrutas.agregarFruta(nombre, color, pesoKg, precio);
            if (agregado) {
                JOptionPane.showMessageDialog(null, "Fruta agregada con exito");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo agregar la fruta. Verifique que el nombre no exista y que el peso y precio sean válidos.", "Error", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "El nombre esta vacio", "Error", 0);
        }
    }

    private void listarFrutas() {
        String lista = gestionFrutas.ListarFrutas();
        JOptionPane.showMessageDialog(null, lista);
    }

    private void buscarFruta() {
        int IdBuscar = Integer.parseInt(JOptionPane.showInputDialog(null, "ID de la fruta que estas buscando"));
        Frutas frutaEncontrada = gestionFrutas.buscarFrutasPorId(IdBuscar);
        if (frutaEncontrada != null) {
            JOptionPane.showMessageDialog(null, "Fruta encontrada:\n" + frutaEncontrada);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontro la fruta con este ID:" + IdBuscar);
        }
    }

    private void actualizarFruta() {
        int IdActualizar = Integer.parseInt(JOptionPane.showInputDialog(null, "ID de la fruta que quieres actualizar."));
        Frutas frutaParaActualizar = gestionFrutas.buscarFrutasPorId(IdActualizar);

        if (frutaParaActualizar != null) {
            double nuevoPrecio = Double.parseDouble(JOptionPane.showInputDialog(null, "nuevo precio de la fruta"));
            if (gestionFrutas.actualizar(IdActualizar, nuevoPrecio)) {
                JOptionPane.showMessageDialog(null, "Precio actualizado");
            } else {
                JOptionPane.showMessageDialog(null, "El precio no puede ser negativo.", "Error", 0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontro la fruta con este ID: " + IdActualizar, "Error", 0);
        }
    }

    private void eliminarFruta() {
        int IdEliminar = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingresa el ID que quieres eliminar"));
        if (gestionFrutas.eliminarFruta(IdEliminar)) {
            JOptionPane.showMessageDialog(null, "Esta fruta fue eliminada con exito");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontro la fruta con este ID" + IdEliminar);
        }
    }
}
