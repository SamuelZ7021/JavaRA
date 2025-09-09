import java.util.Scanner;

public class Reto_1 {
    public static void main(String[] args){
        // Entradas (con Scanner)
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingresa tu datos");

        System.out.println("Nombre Completo:");
        String nombre = sc.nextLine();

        System.out.println("Edad:");
        int edad = sc.nextInt();

        System.out.println("Altura: ej: 1.65 o 1.82");
        double altura = sc.nextDouble();

        System.out.println("Peso: ej: 59.6 o 37.0");
        double peso = sc.nextDouble();

        System.out.println("Plan deseado (BASICO, PLUS O PREMIUM):");
        String plan = sc.next();

        System.out.println("Es tu primera vez? (TRUE O FALSE):");
        boolean primeraVez = sc.nextBoolean();

        // REGLAS DE NEGOCIOS
        double precioBase = 0.0;
        double  descuentoTotal = 0.0;
        String categoriaBMI = "";

        // Edad minima
        // Regla 3: Edad mínima
        if (edad < 14) {
            System.out.println(nombre + ", no eres elegible. La edad mínima es 14 años.");
            sc.close();
            return;
        } else if (edad < 18) {
            System.out.println("AVISO: Requiere autorización de acudiente.");
        }

        // Use toUpperCase() para que no importe si el usuario escribe "basico" o "BASICO"
        switch (plan.toUpperCase()) {
            case "BASICO":
                precioBase = 80.0;
                break;
            case "PLUS":
                precioBase = 120.0;
                break;
            case "PREMIUM":
                precioBase = 180.0;
                break;
            default:
                System.out.println("Plan no válido. Por favor, elige entre BASICO, PLUS o PREMIUM.");
                return;
        }

        // Regla 2: Descuentos
        if (primeraVez) {
            descuentoTotal += 0.10;
        }
        if (edad >= 16 && edad <= 25) {
            descuentoTotal += 0.10;
        }
        // Asegura que el descuento no supere el 20%
        if (descuentoTotal > 0.20) {
            descuentoTotal = 0.20;
        }

        // Regla 4: Cálculo de BMI
        double bmi = peso / (altura * altura);

        if (bmi < 18.5) {
            categoriaBMI = "Bajo peso";
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            categoriaBMI = "Normal";
        } else if (bmi >= 25 && bmi <= 29.9) {
            categoriaBMI = "Sobrepeso";
        } else {
            categoriaBMI = "Obesidad";
        }

        // Cálculos finales
        double montoDescuento = precioBase * descuentoTotal;
        double precioFinal = precioBase - montoDescuento;

        // Regla 5: Resultado final
        System.out.println("\n--- Resumen de Cotización FitPro Gym ---");
        System.out.println("Cliente: " + nombre);
        System.out.println("Plan Elegido: " + plan.toUpperCase());
        System.out.println("Precio Base Mensual: $" + precioBase);
        System.out.println("Descuento Aplicado (" + (descuentoTotal * 100) + "%): -$" + montoDescuento);
        System.out.println("Precio Final Mensual: $" + precioFinal);
        System.out.println("-----------------------------------------");
        System.out.println("Categoría BMI: " + categoriaBMI);

        sc.close();
    }
}
