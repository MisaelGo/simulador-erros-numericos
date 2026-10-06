package simuladorerrosnumericos;

public class Testes {

    private static final int TRUNCAMENTO = 1;
    private static final int ARREDONDAMENTO = 2;

    private final Calculadora calculadora;
    private final PrecisaoNumerica precisaoNumerica;
    private final CalculadoraErro calculadoraErro;

    public Testes() {
        calculadora = new Calculadora();
        precisaoNumerica = new PrecisaoNumerica();
        calculadoraErro = new CalculadoraErro();
    }

    /**
     * Permite executar todos os casos de teste diretamente pelo terminal.
     */
    public static void main(String[] args) {
        Testes testes = new Testes();

        testes.testeSomaSimples();
        testes.testeCancelamentoSubtrativo();
        testes.testePropagacaoSomas();
    }

    public void testeSomaSimples() {
        double primeiroValor = 12.3456;
        double segundoValor = 0.78901;
        int digitos = 4;

        ResultadoTeste truncamento = executarSoma(
                primeiroValor,
                segundoValor,
                digitos,
                TRUNCAMENTO
        );

        ResultadoTeste arredondamento = executarSoma(
                primeiroValor,
                segundoValor,
                digitos,
                ARREDONDAMENTO
        );

        exibirComparacao(
                "SOMA SIMPLES",
                digitos,
                truncamento,
                arredondamento
        );
    }

    public void testeCancelamentoSubtrativo() {
        double primeiroValor = 1.23456;
        double segundoValor = 1.23444;
        int digitos = 4;

        ResultadoTeste truncamento = executarSubtracao(
                primeiroValor,
                segundoValor,
                digitos,
                TRUNCAMENTO
        );

        ResultadoTeste arredondamento = executarSubtracao(
                primeiroValor,
                segundoValor,
                digitos,
                ARREDONDAMENTO
        );

        exibirComparacao(
                "CANCELAMENTO SUBTRATIVO",
                digitos,
                truncamento,
                arredondamento
        );
    }

    public void testePropagacaoSomas() {
        double parcela = 0.123456;
        int repeticoes = 10;
        int digitos = 4;

        ResultadoTeste truncamento = executarSomasSucessivas(
                parcela,
                repeticoes,
                digitos,
                TRUNCAMENTO
        );

        ResultadoTeste arredondamento = executarSomasSucessivas(
                parcela,
                repeticoes,
                digitos,
                ARREDONDAMENTO
        );

        exibirComparacao(
                "PROPAGACAO DE ERRO EM SOMAS SUCESSIVAS",
                digitos,
                truncamento,
                arredondamento
        );
    }

    private ResultadoTeste executarSoma(
            double primeiroValor,
            double segundoValor,
            int digitos,
            int metodo
    ) {
        double resultadoExato = calculadora.somar(
                primeiroValor,
                segundoValor
        );

        double primeiroAproximado = ajustar(
                primeiroValor,
                digitos,
                metodo
        );

        double segundoAproximado = ajustar(
                segundoValor,
                digitos,
                metodo
        );

        double resultadoAproximado = ajustar(
                calculadora.somar(
                        primeiroAproximado,
                        segundoAproximado
                ),
                digitos,
                metodo
        );

        return criarResultado(
                metodo,
                resultadoExato,
                resultadoAproximado
        );
    }

    private ResultadoTeste executarSubtracao(
            double primeiroValor,
            double segundoValor,
            int digitos,
            int metodo
    ) {
        double resultadoExato = calculadora.subtrair(
                primeiroValor,
                segundoValor
        );

        double primeiroAproximado = ajustar(
                primeiroValor,
                digitos,
                metodo
        );

        double segundoAproximado = ajustar(
                segundoValor,
                digitos,
                metodo
        );

        double resultadoAproximado = ajustar(
                calculadora.subtrair(
                        primeiroAproximado,
                        segundoAproximado
                ),
                digitos,
                metodo
        );

        return criarResultado(
                metodo,
                resultadoExato,
                resultadoAproximado
        );
    }

    private ResultadoTeste executarSomasSucessivas(
            double parcela,
            int repeticoes,
            int digitos,
            int metodo
    ) {
        double resultadoExato = 0.0;
        double resultadoAproximado = 0.0;
        double parcelaAproximada = ajustar(
                parcela,
                digitos,
                metodo
        );

        for (int i = 0; i < repeticoes; i++) {
            resultadoExato = calculadora.somar(
                    resultadoExato,
                    parcela
            );

            resultadoAproximado = ajustar(
                    calculadora.somar(
                            resultadoAproximado,
                            parcelaAproximada
                    ),
                    digitos,
                    metodo
            );
        }

        return criarResultado(
                metodo,
                resultadoExato,
                resultadoAproximado
        );
    }

    private double ajustar(
            double valor,
            int digitos,
            int metodo
    ) {
        return precisaoNumerica.ajustarPrecisao(
                valor,
                digitos,
                metodo
        );
    }

    private ResultadoTeste criarResultado(
            int metodo,
            double resultadoExato,
            double resultadoAproximado
    ) {
        double erroAbsoluto = calculadoraErro.erroAbsoluto(
                resultadoExato,
                resultadoAproximado
        );

        double erroRelativo = calculadoraErro.erroRelativo(
                resultadoExato,
                resultadoAproximado
        );

        return new ResultadoTeste(
                nomeMetodo(metodo),
                resultadoExato,
                resultadoAproximado,
                erroAbsoluto,
                erroRelativo
        );
    }

    private String nomeMetodo(int metodo) {
        if (metodo == TRUNCAMENTO) {
            return "Truncamento";
        }

        if (metodo == ARREDONDAMENTO) {
            return "Arredondamento";
        }

        throw new IllegalArgumentException(
                "Metodo de precisao invalido."
        );
    }

    private void exibirComparacao(
            String titulo,
            int digitos,
            ResultadoTeste truncamento,
            ResultadoTeste arredondamento
    ) {
        System.out.println("\n=== " + titulo + " ===");
        System.out.println("Digitos significativos: " + digitos);
        System.out.printf(
                "%-15s %18s %18s %18s %18s%n",
                "Metodo",
                "Valor exato",
                "Valor aproximado",
                "Erro absoluto",
                "Erro relativo"
        );

        exibirLinha(truncamento);
        exibirLinha(arredondamento);

        if (Double.isNaN(truncamento.resultadoAproximado)
                || Double.isNaN(arredondamento.resultadoAproximado)) {
            System.out.println(
                    "Observacao: a comparacao numerica depende da "
                    + "implementacao de PrecisaoNumerica."
            );
        }
    }

    private void exibirLinha(ResultadoTeste resultado) {
        System.out.printf(
                "%-15s %18s %18s %18s %18s%n",
                resultado.metodo,
                formatar(resultado.resultadoExato),
                formatar(resultado.resultadoAproximado),
                formatar(resultado.erroAbsoluto),
                formatar(resultado.erroRelativo)
        );
    }

    private String formatar(double valor) {
        if (Double.isNaN(valor)) {
            return "indisponivel";
        }

        if (Double.isInfinite(valor)) {
            return valor > 0
                    ? "+infinito"
                    : "-infinito";
        }

        return String.format("%.10e", valor);
    }

    private static class ResultadoTeste {

        private final String metodo;
        private final double resultadoExato;
        private final double resultadoAproximado;
        private final double erroAbsoluto;
        private final double erroRelativo;

        private ResultadoTeste(
                String metodo,
                double resultadoExato,
                double resultadoAproximado,
                double erroAbsoluto,
                double erroRelativo
        ) {
            this.metodo = metodo;
            this.resultadoExato = resultadoExato;
            this.resultadoAproximado = resultadoAproximado;
            this.erroAbsoluto = erroAbsoluto;
            this.erroRelativo = erroRelativo;
        }
    }
}
