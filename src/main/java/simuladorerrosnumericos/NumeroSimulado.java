package simuladorerrosnumericos;

/**
 * Classe que modela o tipo NumeroSimulado, responsável por armazenar o valor
 * original, o valor aproximado, a quantidade de dígitos significativos e o
 * método utilizado controle da quantidade de dígitos significativos e
 * representação em notação científica.
 */
public class NumeroSimulado {
    
    private final double valorOriginal;
    private final double valorAproximado;
    private final int digitosSignificativos;
    private final int metodoUtilizado;

    public NumeroSimulado(double valorOriginal, double valorAproximado, int digitosSignificativos, int metodoUtilizado) {
        this.valorOriginal = valorOriginal;
        this.valorAproximado = valorAproximado;
        this.digitosSignificativos = digitosSignificativos;
        this.metodoUtilizado = metodoUtilizado;
    }

    public double getValorOriginal() {
        return valorOriginal;
    }

    public double getValorAproximado() {
        return valorAproximado;
    }

    public int getDigitosSignificativos() {
        return digitosSignificativos;
    }

    public int getMetodoUtilizado() {
        return metodoUtilizado;
    }
    
    // Método utilitário para converter o código do método em texto
    public String getNomeMetodo() {
        return (metodoUtilizado == 1) ? "Truncamento" : "Arredondamento";
    }
}