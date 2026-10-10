package simuladorerrosnumericos;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Controla a precisão dos valores por truncamento
 * ou arredondamento e gera a notação científica.
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
     * Normaliza um valor seguindo a forma usada no trabalho:
     * 123.45 = 0.12345 x 10^3.
     */
    public NotacaoCientifica normalizar(
            double valor
    ) {

        // Zero não precisa de deslocamento da vírgula.
        if (valor == 0.0) {
            return new NotacaoCientifica(
                    0.0,
                    0
            );
        }

        double valorAbsoluto =
                Math.abs(valor);

        // Determina quantas posições a vírgula deve ser deslocada.
        int expoente =
                (int) Math.floor(
                        Math.log10(valorAbsoluto)
                ) + 1;

        double mantissa =
                valor
                / Math.pow(10, expoente);

        return new NotacaoCientifica(
                mantissa,
                expoente
        );
    }

    /**
     * Converte o valor normalizado para uma String legível.
     */
    public String formatarNotacaoCientifica(
            double valor
    ) {

        NotacaoCientifica nc =
                normalizar(valor);

        // Remove zeros desnecessários da mantissa.
        String mantissaFormatada =
                BigDecimal.valueOf(
                        nc.getMantissa()
                )
                .stripTrailingZeros()
                .toPlainString();

        return mantissaFormatada
                + " x 10^"
                + nc.getExpoente();
    }

    /**
     * Trunca um valor para a quantidade informada
     * de casas decimais.
     */
    public double truncar(
            double valor,
            int casasDecimais
    ) {

        validarCasasDecimais(
                casasDecimais
        );

        // RoundingMode.DOWN remove as casas excedentes.
        return BigDecimal.valueOf(valor)
                .setScale(
                        casasDecimais,
                        RoundingMode.DOWN
                )
                .doubleValue();
    }

    /**
     * Arredonda um valor para a quantidade informada
     * de casas decimais.
     */
    public double arredondar(
            double valor,
            int casasDecimais
    ) {

        validarCasasDecimais(
                casasDecimais
        );

        // HALF_UP aplica o arredondamento decimal convencional.
        return BigDecimal.valueOf(valor)
                .setScale(
                        casasDecimais,
                        RoundingMode.HALF_UP
                )
                .doubleValue();
    }

    /**
     * Aplica o método de precisão escolhido pelo usuário.
     * 1 = truncamento
     * 2 = arredondamento
     */
    public double ajustarPrecisao(
            double valor,
            int casasDecimais,
            int metodo
    ) {

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
     * Verifica se a quantidade de casas é válida.
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
}
