package simuladorerrosnumericos;

import java.math.BigDecimal;

/**
 * Classe responsável pelo controle da quantidade de dígitos significativos e 
 * representação em notação científica.
 */
public class PrecisaoNumerica {
    
    /*
     * JUSTIFICATIVA ARQUITETURAL:
     * A estrutura NotacaoCientifica foi implementada como uma classe estática aninhada 
     * como forma de manter o encapsulamento desta representação intermediária, afinal, ela é
     * uma estrutura auxiliar exclusivamente utilizada pela classe PrecisaoNumerica.
     */
    public static class NotacaoCientifica {
        public double mantissa;
        public int expoente;

        public NotacaoCientifica(double mantissa, int expoente) {
            this.mantissa = mantissa;
            this.expoente = expoente;
        }
    }
    
    /**
     * Normaliza o resultado para a notação científica utilizando a convenção: 0.1 <= |mantissa| < 1.0.
     */
    // Nota: A assinatura foi alterada de 'double' para 'NotacaoCientifica' para encapsular os dados.
    public NotacaoCientifica normalizar(double valor) {
        if (valor == 0.0) {
            return new NotacaoCientifica(0.0, 0);
        }

        double valorAbsoluto = Math.abs(valor);

        // Determina a ordem de grandeza do valor.
        int expoente = (int) Math.floor(Math.log10(valorAbsoluto)) + 1;

        // Desloca a vírgula para obter 0.1 <= |mantissa| < 1.0.
        double mantissa = valor / Math.pow(10, expoente);

        /*
         * Garante a normalização mesmo diante de eventuais
         * imprecisões da representação em ponto flutuante.
         */
        if (Math.abs(mantissa) >= 1.0) {
            mantissa /= 10.0;
            expoente++;
        } else if (Math.abs(mantissa) < 0.1) {
            mantissa *= 10.0;
            expoente--;
        }

        return new NotacaoCientifica(mantissa, expoente);
    }

    /**
     * Trunca a mantissa para 'n' dígitos significativos.
     */
    public double truncar(double valor, int digitos) {
        double fator = Math.pow(10, digitos);
        
        // Multiplica pelo fator, descarta a parte fracionária e devolve à escala original.
        // O uso de Math.signum garante que números negativos sejam truncados simetricamente em direção ao zero.
        double valorAbsolutoTruncado = Math.floor(Math.abs(valor) * fator);
        
        return (valorAbsolutoTruncado / fator) * Math.signum(valor);
    }
    
    /**
     * Arredonda a mantissa para 'n' dígitos significativos utilizando o método de arredondamento simétrico.
     */
    public double arredondar(double mantissa, int n) {
        double fator = Math.pow(10, n);
        
        // Adiciona 0.5 ao valor absoluto para forçar o arredondamento simétrico,
        // garantindo consistência em números negativos.
        double valorAbsolutoArredondado = Math.floor(Math.abs(mantissa) * fator + 0.5);
        
        return (valorAbsolutoArredondado / fator) * Math.signum(mantissa);
    }

    /**
     * Centraliza a aplicação do método de precisão escolhido (truncamento ou arredondamento)
     * e retorna o valor final em formato de ponto flutuante padronizado.
     */
    public double ajustarPrecisao(double mantissa, int digitos, int metodo) {
        if (mantissa == 0.0) return 0.0;
        
        if (digitos <= 0) {
            throw new IllegalArgumentException("A quantidade de dígitos significativos deve ser maior que zero.");
        }
        
        // 1. Normalizar
        NotacaoCientifica nc = normalizar(mantissa);
        double mantissaAjustada;

        // 2. Aplicar o método de ajuste para manter apenas n dígitos significativos
        if (metodo == 1) {
            mantissaAjustada = truncar(nc.mantissa, digitos);
        } else if (metodo == 2) {
            mantissaAjustada = arredondar(nc.mantissa, digitos);
            
            /*
             * O arredondamento pode fazer a mantissa atingir ou ultrapassar
             * o limite superior da normalização (|mantissa| >= 1.0).
             *
             * Nesse caso, a mantissa é normalizada novamente e o expoente
             * original é atualizado para preservar o valor numérico.
             */
            if (Math.abs(mantissaAjustada) >= 1.0 || Math.abs(mantissaAjustada) < 0.1) {
                NotacaoCientifica novaNormalizacao = normalizar(mantissaAjustada);
                mantissaAjustada = novaNormalizacao.mantissa;
                nc.expoente += novaNormalizacao.expoente;
            }
        } else {
            throw new IllegalArgumentException("Método de precisão inválido.");
        }

        /* 
         * 3. Desnormalizar para retornar o valor aproximado usando BigDecimal para
        * reduzir inconsistências de representação decimal durante a reconstrução do valor.
        */
        BigDecimal bdMantissa = BigDecimal.valueOf(mantissaAjustada);
        
        return bdMantissa.scaleByPowerOfTen(nc.expoente).doubleValue();
    }
}