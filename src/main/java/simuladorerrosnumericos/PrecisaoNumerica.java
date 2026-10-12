package simuladorerrosnumericos;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Controla truncamento, arredondamento
 * e representação em notação científica.
 */
public class PrecisaoNumerica {

    /**
     * Representa um número na forma:
     * mantissa x 10^expoente.
     */
    public static class NotacaoCientifica {

        private final double mantissa;
        private final int expoente;

        public NotacaoCientifica(
                double mantissa,
                int expoente
        ) {
            this.mantissa = mantissa;
            this.expoente = expoente;
        }

        public double getMantissa() {
            return mantissa;
        }

        public int getExpoente() {
            return expoente;
        }
    }

    /**
     * Normaliza segundo a forma usada no trabalho:
     * 123.45 = 0.12345 x 10^3.
     */
    public NotacaoCientifica normalizar(
            double valor
    ) {

        validarValorFinito(valor);

        if (valor == 0.0) {

            return new NotacaoCientifica(
                    0.0,
                    0
            );
        }

        BigDecimal decimal =
                BigDecimal.valueOf(valor);

        int expoente =
                decimal.precision()
                - decimal.scale();

        double mantissa =
                decimal
                        .scaleByPowerOfTen(
                                -expoente
                        )
                        .doubleValue();

        return new NotacaoCientifica(
                mantissa,
                expoente
        );
    }

    /**
     * Formata um valor em notação científica
     * usando a quantidade de casas informada.
     */
    public String formatarNotacaoCientifica(
            double valor,
            int casasDecimais
    ) {

        validarCasasDecimais(
                casasDecimais
        );

        if (valor == 0.0) {

            return BigDecimal.ZERO
                    .setScale(casasDecimais)
                    .toPlainString()
                    + " x 10^0";
        }

        NotacaoCientifica nc =
                normalizar(valor);

        String mantissaFormatada =
                BigDecimal
                        .valueOf(
                                nc.getMantissa()
                        )
                        .setScale(
                                casasDecimais,
                                RoundingMode.HALF_UP
                        )
                        .toPlainString();

        return mantissaFormatada
                + " x 10^"
                + nc.getExpoente();
    }

    /**
     * Formata um resultado com a quantidade
     * de casas decimais escolhida.
     *
     * Valores muito pequenos são apresentados
     * em notação científica para não aparecerem
     * incorretamente como zero.
     */
    public String formatarResultado(
            double valor,
            int casasDecimais
    ) {

        validarCasasDecimais(
                casasDecimais
        );

        if (Double.isNaN(valor)) {
            return "não definido";
        }

        if (valor == 0.0) {

            return BigDecimal.ZERO
                    .setScale(casasDecimais)
                    .toPlainString();
        }

        BigDecimal decimal =
                BigDecimal.valueOf(valor);

        BigDecimal valorFormatado =
                decimal.setScale(
                        casasDecimais,
                        RoundingMode.HALF_UP
                );

        /*
         * Se o valor não é zero, mas ficaria
         * visualmente igual a zero, usa
         * notação científica.
         */
        if (valorFormatado.compareTo(
                BigDecimal.ZERO
        ) == 0) {

            return formatarNotacaoCientifica(
                    valor,
                    casasDecimais
            );
        }

        return valorFormatado
                .toPlainString();
    }

    /**
     * Trunca o valor para a quantidade
     * escolhida de casas decimais.
     */
    public double truncar(
            double valor,
            int casasDecimais
    ) {

        validarCasasDecimais(
                casasDecimais
        );

        validarValorFinito(valor);

        return BigDecimal
                .valueOf(valor)
                .setScale(
                        casasDecimais,
                        RoundingMode.DOWN
                )
                .doubleValue();
    }

    /**
     * Arredonda o valor para a quantidade
     * escolhida de casas decimais.
     */
    public double arredondar(
            double valor,
            int casasDecimais
    ) {

        validarCasasDecimais(
                casasDecimais
        );

        validarValorFinito(valor);

        return BigDecimal
                .valueOf(valor)
                .setScale(
                        casasDecimais,
                        RoundingMode.HALF_UP
                )
                .doubleValue();
    }

    /**
     * Aplica o método de precisão escolhido.
     * 1 = truncamento
     * 2 = arredondamento
     */
    public double ajustarPrecisao(
            double valor,
            int casasDecimais,
            int metodo
    ) {

        validarCasasDecimais(
                casasDecimais
        );

        validarValorFinito(valor);

        return switch (metodo) {

            case 1 ->
                truncar(
                        valor,
                        casasDecimais
                );

            case 2 ->
                arredondar(
                        valor,
                        casasDecimais
                );

            default ->
                throw new IllegalArgumentException(
                        "Método de precisão inválido."
                );
        };
    }

    /**
     * Valida a quantidade de casas decimais.
     */
    private void validarCasasDecimais(
            int casasDecimais
    ) {

        if (casasDecimais < 0) {

            throw new IllegalArgumentException(
                    "A quantidade de casas decimais não pode ser negativa."
            );
        }
    }

    /**
     * Impede valores infinitos ou NaN.
     */
    private void validarValorFinito(
            double valor
    ) {

        if (!Double.isFinite(valor)) {

            throw new IllegalArgumentException(
                    "O valor deve ser um número finito."
            );
        }
    }
}