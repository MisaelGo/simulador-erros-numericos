package simuladorerrosnumericos;

public class CalculadoraErro {

    /**
     * Calcula a distancia entre o valor exato e o valor aproximado.
     *
     * @param valorExato valor usado como referencia
     * @param valorAproximado valor obtido com precisao limitada
     * @return erro absoluto entre os valores
     */
    public double erroAbsoluto(
            double valorExato,
            double valorAproximado
    ) {
        return Math.abs(valorExato - valorAproximado);
    }

    /**
     * Calcula o erro absoluto em relacao ao modulo do valor exato.
     *
     * Quando os dois valores sao zero, considera-se que nao ha erro. Se
     * apenas o valor exato for zero, o erro relativo e infinito, pois nao
     * existe uma referencia diferente de zero para realizar a divisao.
     *
     * @param valorExato valor usado como referencia
     * @param valorAproximado valor obtido com precisao limitada
     * @return erro relativo entre os valores
     */
    public double erroRelativo(
            double valorExato,
            double valorAproximado
    ) {
        double erroAbsoluto = erroAbsoluto(
                valorExato,
                valorAproximado
        );

        if (valorExato == 0.0) {
            return erroAbsoluto == 0.0
                    ? 0.0
                    : Double.POSITIVE_INFINITY;
        }

        return erroAbsoluto / Math.abs(valorExato);
    }
}
