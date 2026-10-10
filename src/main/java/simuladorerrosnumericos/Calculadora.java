package simuladorerrosnumericos;

/**
 * Realiza as quatro operações aritméticas básicas.
 */
public class Calculadora {

    /** Soma dois valores. */
    public double somar(double x, double y) {
        return x + y;
    }

    /** Subtrai y de x. */
    public double subtrair(double x, double y) {
        return x - y;
    }

    /** Multiplica dois valores. */
    public double multiplicar(double x, double y) {
        return x * y;
    }

    /**
     * Divide x por y.
     *
     * @throws ArithmeticException se y for zero.
     */
    public double dividir(double x, double y) {

        // Impede divisão por zero.
        if (y == 0) {
            throw new ArithmeticException(
                    "Não é possível dividir por zero."
            );
        }

        return x / y;
    }
}
