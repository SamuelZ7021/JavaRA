import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bienvenido a mi Cajero");

        double saldo = 1000000;
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        // Menu
        while (!salir) {
            System.out.println("\n---Cajero");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Depositar dinero");
            System.out.println("3. Retirar dinero");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Su saldo actual es: $" + saldo);
                    break;
                case 2:
                    System.out.print("Ingrese la cantidad a depositar: $");
                    double deposito = scanner.nextDouble();
                    saldo += deposito;
                    break;
                case 3:
                    System.out.print("Ingrese la cantidad a retirar: $");
                    double retiro = scanner.nextDouble();
                    if (retiro > saldo){
                        System.out.println("Saldo insuficiente.");
                        } else if(retiro <= 0) {
                        saldo -= retiro;
                        System.out.println("El monto a retirar debe ser mayor a cero");
                    } else {
                        saldo -= retiro;
                        System.out.println("Retiro exitoso. Su saldo actual es: $" + saldo);
                    }
                    break;
                case 4:
                    salir = true;
                    System.out.println("Haz salido del cagero... ¡Regresa pronto!");
                    break;
                default:
                    System.out.println("Opción no valida. Intente de nuevo.");
            }
        }
        scanner.close();
    }
}