package simuladorerrosnumericos;

public class PrecisaoNumerica {

    public double normalizar(double valor) {

        // TODO: implementar normalização

        return Double.NaN;
    }

    public double truncar(
            double valor,
            int digitos
    ) {

        // TODO: implementar truncamento

        return Double.NaN;
    }

    public double arredondar(
            double valor,
            int digitos
    ) {

        // TODO: implementar arredondamento

        return Double.NaN;
    }

    public double ajustarPrecisao(
            double valor,
            int digitos,
            int metodo
    ) {

        if (metodo == 1) {
            return truncar(valor, digitos);
        }

        if (metodo == 2) {
            return arredondar(valor, digitos);
        }

        throw new IllegalArgumentException(
                "Método de precisão inválido."
        );
    }
}