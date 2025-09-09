import java.util.Scanner;

public class TrueoFalse{
    public static void main(String[] args){
        int edad;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Ingresa tu edad");
            edad = scanner.nextInt();
        } while (edad >= 18);
        }
    }
