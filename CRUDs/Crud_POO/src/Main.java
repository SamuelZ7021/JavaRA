public class Main {
    public static void main(String[] args) {
        GestionFrutas miGestion = new GestionFrutas();
        Menu menu = new Menu(miGestion);
        menu.mostrarMenu();
    }
}
