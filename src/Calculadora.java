import java.util.Scanner;

public class Calculadora {
    public static void main(String[] args){
        System.out.println("Calculadora");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingresa un numero para a");
        int a = scanner.nextInt();
        System.out.println("Ingresa un numero para b");
        int b = scanner.nextInt();
        boolean salir = false;

        while (!salir){
            System.out.println("1. Sumar");
            System.out.println("2. Restar");
            System.out.println("3. Multiplicar");
            System.out.println("4. Dividir");
            System.out.println("5. Modulo");
            System.out.println("6. Salir");

            int opcion = scanner.nextInt();

            switch (opcion){
                case 1:
                    System.out.println("El resultado es:" +(a + b));
                    break;
                case 2:
                    System.out.println("El resultado es:" +(a - b));
                    break;
                case 3:
                    System.out.println("El resultado es:" +(a * b));
                    break;
                case 4:
                    System.out.println("El resultado es:" +(a / b));
                    break;
                case 5:
                    System.out.println("El resultado es:" +(a & b));
                    break;
                case 6:
                    salir = true;
                    System.out.println("Calculadora cerrada");
                    break;
                default:
            }
        }
        scanner.close();
    }
}