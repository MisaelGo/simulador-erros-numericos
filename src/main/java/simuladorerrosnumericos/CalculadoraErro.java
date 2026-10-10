package simuladorerrosnumericos;

/**
 * Calcula os erros associados ao resultado aproximado.
 */
public class CalculadoraErro {

    /**
     * Calcula o erro absoluto entre os valores.
     */
    public double erroAbsoluto(
            double valorExato,
            double valorAproximado
    ) {
        return Math.abs(
                valorExato - valorAproximado
        );
    }

    /**
     * Calcula o erro relativo usando o valor aproximado
     * como denominador, conforme o enunciado do trabalho.
     */
    public double erroRelativo(
            double valorExato,
            double valorAproximado
    ) {

        // O erro relativo não é definido se o aproximado for zero.
        if (valorAproximado == 0.0) {
            return Double.NaN;
        }

        double erroAbsoluto =
                erroAbsoluto(
                        valorExato,
                        valorAproximado
                );

        return erroAbsoluto
                / Math.abs(valorAproximado);
    }
}
