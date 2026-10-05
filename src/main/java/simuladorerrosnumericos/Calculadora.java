package simuladorerrosnumericos;

public class Calculadora {

    public double somar(double x, double y) {
        return x + y;
    }

    public double subtrair(double x, double y) {
        return x - y;
    }

    public double multiplicar(double x, double y) {
        return x * y;
    }

    public double dividir(double x, double y) {

        if (y == 0) {
            throw new ArithmeticException("Não é possível dividir por zero.");
        }

        return x / y;
    }
}