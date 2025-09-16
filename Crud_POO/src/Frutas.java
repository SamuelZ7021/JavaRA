public class Frutas {
    private static int aunmentId = 1;

    private int id;
    private String nombre;
    private double pesoKg;
    private String color;
    private double precio;


    public Frutas(double precio, String color, double pesoKg, String nombre){
        this.id = aunmentId++;
        this.precio = precio;
        this.color = color;
        this.pesoKg = pesoKg;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        }
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public boolean setPesoKg(double pesoKg) {
        if (pesoKg > 0){
            this.pesoKg = pesoKg;
            return true;
        }
        return false;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrecio() {
        return precio;

    }

    public boolean setPrecio(double precio) {
        if (precio >= 0){
            this.precio = precio;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n" +
               "Nombre: " + nombre + "\n" +
               "Peso (Kg): " + pesoKg + "\n" +
               "Color: " + color + "\n" +
               "Precio: " + precio;
    }
}
