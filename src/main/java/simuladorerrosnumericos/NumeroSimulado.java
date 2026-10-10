package simuladorerrosnumericos;

/**
 * Armazena as informações de um valor processado
 * pelo simulador.
 */
public class NumeroSimulado {

    private final double valorOriginal;
    private final double valorAproximado;
    private final int casasDecimais;
    private final int metodoUtilizado;

    /**
     * Cria um registro com os dados da simulação.
     */
    public NumeroSimulado(
            double valorOriginal,
            double valorAproximado,
            int casasDecimais,
            int metodoUtilizado
    ) {
        this.valorOriginal = valorOriginal;
        this.valorAproximado = valorAproximado;
        this.casasDecimais = casasDecimais;
        this.metodoUtilizado = metodoUtilizado;
    }

    public double getValorOriginal() {
        return valorOriginal;
    }

    public double getValorAproximado() {
        return valorAproximado;
    }

    public int getCasasDecimais() {
        return casasDecimais;
    }

    public int getMetodoUtilizado() {
        return metodoUtilizado;
    }

    /**
     * Converte o código do método para texto.
     */
    public String getNomeMetodo() {
        return metodoUtilizado == 1
                ? "Truncamento"
                : "Arredondamento";
    }
}
