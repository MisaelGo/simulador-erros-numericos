
package simuladorerrosnumericos;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Controla a precisão numérica por quantidade de algarismos significativos
 * e representa valores em notação científica.
 */
public class PrecisaoNumerica {

    /**
     * Representa um número na forma mantissa x 10^expoente.
     */
    public static class NotacaoCientifica {
        private final double mantissa;
        private final int expoente;

        public NotacaoCientifica(double mantissa, int expoente) {
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
     * Normaliza um valor para a forma mantissa x 10^expoente.
     * Exemplo: 123.45 = 0.12345 x 10^3.
     */
    public NotacaoCientifica normalizar(double valor) {
        validarValorFinito(valor);

        if (valor == 0.0) {
            return new NotacaoCientifica(0.0, 0);
        }

        // precision - scale determina o expoente decimal do valor.
        BigDecimal decimal = BigDecimal.valueOf(valor);
        int expoente = decimal.precision() - decimal.scale();

        double mantissa = decimal
                .scaleByPowerOfTen(-expoente)
                .doubleValue();

        return new NotacaoCientifica(mantissa, expoente);
    }

    /**
     * Converte o valor para uma String legível em notação científica.
     */
    public String formatarNotacaoCientifica(double valor) {
        NotacaoCientifica nc = normalizar(valor);

        String mantissaFormatada = BigDecimal
                .valueOf(nc.getMantissa())
                .stripTrailingZeros()
                .toPlainString();

        return mantissaFormatada + " x 10^" + nc.getExpoente();
    }

    /**
     * Trunca uma mantissa normalizada para a quantidade informada
     * de algarismos significativos.
     */
    public double truncar(double mantissa, int algarismosSignificativos) {
        validarAlgarismosSignificativos(algarismosSignificativos);
        validarValorFinito(mantissa);

        return BigDecimal.valueOf(mantissa)
                .setScale(algarismosSignificativos, RoundingMode.DOWN)
                .doubleValue();
    }

    /**
     * Arredonda uma mantissa normalizada para a quantidade informada
     * de algarismos significativos, usando HALF_UP.
     */
    public double arredondar(double mantissa, int algarismosSignificativos) {
        validarAlgarismosSignificativos(algarismosSignificativos);
        validarValorFinito(mantissa);

        return BigDecimal.valueOf(mantissa)
                .setScale(algarismosSignificativos, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Aplica a precisão escolhida à mantissa normalizada e reconstrói o valor.
     *
     * @param valor valor original
     * @param algarismosSignificativos quantidade de algarismos significativos (>= 1)
     * @param metodo 1 = truncamento; 2 = arredondamento
     */
    public double ajustarPrecisao(
            double valor,
            int algarismosSignificativos,
            int metodo
    ) {
        validarAlgarismosSignificativos(algarismosSignificativos);

        if (metodo != 1 && metodo != 2) {
            throw new IllegalArgumentException("Método de precisão inválido.");
        }

        validarValorFinito(valor);

        if (valor == 0.0) {
            return 0.0;
        }

        NotacaoCientifica nc = normalizar(valor);

        double mantissaAjustada = (metodo == 1)
                ? truncar(nc.getMantissa(), algarismosSignificativos)
                : arredondar(nc.getMantissa(), algarismosSignificativos);

        int expoenteAjustado = nc.getExpoente();

        // Ex.: 0.9999 arredondado pode virar 1.0; normalize novamente.
        if (Math.abs(mantissaAjustada) >= 1.0) {
            NotacaoCientifica novaNormalizacao = normalizar(mantissaAjustada);
            mantissaAjustada = novaNormalizacao.getMantissa();
            expoenteAjustado += novaNormalizacao.getExpoente();
        }

        return BigDecimal.valueOf(mantissaAjustada)
                .scaleByPowerOfTen(expoenteAjustado)
                .doubleValue();
    }

    private void validarAlgarismosSignificativos(int algarismosSignificativos) {
        if (algarismosSignificativos < 1) {
            throw new IllegalArgumentException(
                    "A quantidade de algarismos significativos deve ser maior que zero."
            );
        }
    }

    private void validarValorFinito(double valor) {
        if (!Double.isFinite(valor)) {
            throw new IllegalArgumentException(
                    "O valor deve ser um número finito."
            );
        }
    }
}
