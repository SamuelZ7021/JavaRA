import javax.swing.*;
import java.util.ArrayList;

public class GestionFrutas {
    private ArrayList<Frutas> listaFrutas = new ArrayList<>();

    public boolean agregarFruta(String nombre,  String color, double pesoKg, double precio) {
        for (Frutas fruta : listaFrutas) {
            if (fruta.getNombre().equalsIgnoreCase(nombre)) {
                return false;
            }
        }
        Frutas nuevaFruta = new Frutas(precio, color, pesoKg, nombre);
        this.listaFrutas.add(nuevaFruta);
        return true;
    }

    public String ListarFrutas() {
        if (listaFrutas.isEmpty()) {
            return "La lista de frutas esta vacia";
        }
        StringBuilder lista = new StringBuilder("--- Lista de Frutas ---\n");
        for (Frutas fruta : this.listaFrutas) {
            lista.append(fruta.toString()).append("\n---------------------\n");
        }
        return lista.toString();
    }
    public Frutas buscarFrutasPorId(int id){
        for (Frutas frutas:this.listaFrutas) {
            if(frutas.getId() == id){
                return frutas;
            }
        }
        return null;
    }

    public boolean actualizar(int id, double nuevoPrecio){
        Frutas frutaActualizada = buscarFrutasPorId(id);

        if (frutaActualizada != null) {
            frutaActualizada.setPrecio(nuevoPrecio);
            return true;
        } else{
            return false;
        }
    }
    public boolean eliminarFruta(int id){
        Frutas frutaEliminada = buscarFrutasPorId(id);
        if (frutaEliminada != null ){
            this.listaFrutas.remove(frutaEliminada);
            return true;
        } else {
            return false;
        }
    }
}
