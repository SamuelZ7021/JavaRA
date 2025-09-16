import app.model.Frutas;

import java.util.ArrayList;

public class GestionFrutas {
    private ArrayList<Frutas> listaFrutas = new ArrayList<>();

    public boolean agregarFruta(String nombre,  String color, double pesoKg, double precio) {
        if (precio < 0 || pesoKg <= 0) {
            return false;
        }
        for (Frutas fruta : listaFrutas) {
            if (fruta.getNombre().equalsIgnoreCase(nombre)) {
                return false; // La fruta ya existe
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
        StringBuilder lista = new StringBuilder("--- Lista de app.model.Frutas ---\n");
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
            return frutaActualizada.setPrecio(nuevoPrecio);
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
