package simuladorerrosnumericos;

/**
 * Executa os três exemplos usados para validar
 * o simulador.
 */
public class Testes {

    private static final int TRUNCAMENTO = 1;
    private static final int ARREDONDAMENTO = 2;

    private final Calculadora calculadora;
    private final PrecisaoNumerica precisaoNumerica;
    private final CalculadoraErro calculadoraErro;

    /**
     * Inicializa as classes usadas nos testes.
     */
    public Testes() {

        calculadora =
                new Calculadora();

        precisaoNumerica =
                new PrecisaoNumerica();

        calculadoraErro =
                new CalculadoraErro();
    }

    /**
     * Executa todos os exemplos.
     */
    public static void main(
            String[] args
    ) {

        Testes testes =
                new Testes();

        testes.testeSomaSimples();

        testes.testeCancelamentoSubtrativo();

        testes.testePropagacaoSomas();
    }

    /**
     * Exemplo 1:
     * soma simples.
     */
    public void testeSomaSimples() {

        double x = 12.34567;
        double y = 0.78901;

        int casasDecimais = 4;

        ResultadoTeste truncamento =
                executarSoma(
                        x,
                        y,
                        casasDecimais,
                        TRUNCAMENTO
                );

        ResultadoTeste arredondamento =
                executarSoma(
                        x,
                        y,
                        casasDecimais,
                        ARREDONDAMENTO
                );

        exibirComparacao(
                "EXEMPLO 1 - SOMA SIMPLES",
                casasDecimais,
                truncamento,
                arredondamento
        );
    }

    /**
     * Exemplo 2:
     * cancelamento subtrativo.
     */
    public void testeCancelamentoSubtrativo() {

        double x = 0.76545;
        double y = 0.76541;

        int casasDecimais = 4;

        System.out.println(
                "\n=== EXEMPLO 2 - CANCELAMENTO SUBTRATIVO ==="
        );

        System.out.println(
                "x original: "
                + x
        );

        System.out.println(
                "y original: "
                + y
        );

        ResultadoTeste truncamento =
                executarSubtracao(
                        x,
                        y,
                        casasDecimais,
                        TRUNCAMENTO
                );

        ResultadoTeste arredondamento =
                executarSubtracao(
                        x,
                        y,
                        casasDecimais,
                        ARREDONDAMENTO
                );

        exibirComparacao(
                "COMPARAÇÃO DO EXEMPLO 2",
                casasDecimais,
                truncamento,
                arredondamento
        );
    }

    /**
     * Exemplo 3:
     * propagação do erro em somas sucessivas.
     */
    public void testePropagacaoSomas() {

        double parcela = 0.56786;

        int repeticoes = 10;
        int casasDecimais = 4;

        System.out.println(
                "\n=== EXEMPLO 3 - PROPAGAÇÃO EM SOMAS ==="
        );

        ResultadoTeste truncamento =
                executarSomasSucessivas(
                        parcela,
                        repeticoes,
                        casasDecimais,
                        TRUNCAMENTO,
                        true
                );

        ResultadoTeste arredondamento =
                executarSomasSucessivas(
                        parcela,
                        repeticoes,
                        casasDecimais,
                        ARREDONDAMENTO,
                        true
                );

        exibirComparacao(
                "COMPARAÇÃO FINAL DO EXEMPLO 3",
                casasDecimais,
                truncamento,
                arredondamento
        );
    }

    /**
     * Executa a soma simples e aplica
     * a precisão ao resultado.
     */
    private ResultadoTeste executarSoma(
            double x,
            double y,
            int casasDecimais,
            int metodo
    ) {

        double resultadoExato =
                calculadora.somar(
                        x,
                        y
                );

        double resultadoAproximado =
                precisaoNumerica
                        .ajustarPrecisao(
                                resultadoExato,
                                casasDecimais,
                                metodo
                        );

        return criarResultado(
                metodo,
                resultadoExato,
                resultadoAproximado
        );
    }

    /**
     * Aproxima x e y antes de realizar
     * a subtração.
     */
    private ResultadoTeste executarSubtracao(
            double x,
            double y,
            int casasDecimais,
            int metodo
    ) {

        double resultadoExato =
                calculadora.subtrair(
                        x,
                        y
                );

        double xAproximado =
                precisaoNumerica
                        .ajustarPrecisao(
                                x,
                                casasDecimais,
                                metodo
                        );

        double yAproximado =
                precisaoNumerica
                        .ajustarPrecisao(
                                y,
                                casasDecimais,
                                metodo
                        );

        double resultadoAproximado =
                calculadora.subtrair(
                        xAproximado,
                        yAproximado
                );

        System.out.println(
                "\nMétodo: "
                + nomeMetodo(metodo)
        );

        System.out.println(
                "x aproximado: "
                + precisaoNumerica
                        .formatarResultado(
                                xAproximado,
                                casasDecimais
                        )
        );

        System.out.println(
                "y aproximado: "
                + precisaoNumerica
                        .formatarResultado(
                                yAproximado,
                                casasDecimais
                        )
        );

        System.out.println(
                "Resultado aproximado da subtração: "
                + precisaoNumerica
                        .formatarResultado(
                                resultadoAproximado,
                                casasDecimais
                        )
        );

        return criarResultado(
                metodo,
                resultadoExato,
                resultadoAproximado
        );
    }

    /**
     * Executa as somas sucessivas e aplica
     * a precisão após cada nova soma.
     */
    private ResultadoTeste executarSomasSucessivas(
            double parcela,
            int repeticoes,
            int casasDecimais,
            int metodo,
            boolean mostrarEtapas
    ) {

        double resultadoExato =
                parcela * repeticoes;

        double resultadoAproximado =
                parcela;

        if (mostrarEtapas) {

            System.out.println(
                    "\nMétodo: "
                    + nomeMetodo(metodo)
            );
        }

        for (int termo = 2;
                termo <= repeticoes;
                termo++) {

            double antesDoAjuste =
                    calculadora.somar(
                            resultadoAproximado,
                            parcela
                    );

            resultadoAproximado =
                    precisaoNumerica
                            .ajustarPrecisao(
                                    antesDoAjuste,
                                    casasDecimais,
                                    metodo
                            );

            if (mostrarEtapas) {

                System.out.println(
                        "Soma "
                        + (termo - 1)
                        + ": "
                        + precisaoNumerica
                                .formatarResultado(
                                        antesDoAjuste,
                                        casasDecimais
                                )
                        + " -> "
                        + precisaoNumerica
                                .formatarResultado(
                                        resultadoAproximado,
                                        casasDecimais
                                )
                );
            }
        }

        return criarResultado(
                metodo,
                resultadoExato,
                resultadoAproximado
        );
    }

    /**
     * Calcula os erros de um teste.
     */
    private ResultadoTeste criarResultado(
            int metodo,
            double resultadoExato,
            double resultadoAproximado
    ) {

        double erroAbsoluto =
                calculadoraErro
                        .erroAbsoluto(
                                resultadoExato,
                                resultadoAproximado
                        );

        double erroRelativo =
                calculadoraErro
                        .erroRelativo(
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

    /**
     * Retorna o nome do método.
     */
    private String nomeMetodo(
            int metodo
    ) {

        if (metodo == TRUNCAMENTO) {
            return "Truncamento";
        }

        if (metodo == ARREDONDAMENTO) {
            return "Arredondamento";
        }

        throw new IllegalArgumentException(
                "Método de precisão inválido."
        );
    }

    /**
     * Exibe a comparação dos resultados.
     */
    private void exibirComparacao(
            String titulo,
            int casasDecimais,
            ResultadoTeste truncamento,
            ResultadoTeste arredondamento
    ) {

        System.out.println(
                "\n=== "
                + titulo
                + " ==="
        );

        System.out.println(
                "Casas decimais: "
                + casasDecimais
        );

        System.out.printf(
                "%-15s %-26s %-22s %-22s %-25s%n",
                "Método",
                "Valor exato (sem ajuste)",
                "Valor aproximado",
                "Erro absoluto",
                "Erro relativo"
        );

        exibirLinha(
                truncamento,
                casasDecimais
        );

        exibirLinha(
                arredondamento,
                casasDecimais
        );
    }

    /**
     * Exibe uma linha da tabela.
     */
    private void exibirLinha(
            ResultadoTeste resultado,
            int casasDecimais
    ) {

        System.out.printf(
                "%-15s %-26s %-22s %-22s %-25s%n",
                resultado.metodo,

                formatarValorExato(
                        resultado.resultadoExato
                ),

                precisaoNumerica
                        .formatarResultado(
                                resultado.resultadoAproximado,
                                casasDecimais
                        ),

                precisaoNumerica
                        .formatarResultado(
                                resultado.erroAbsoluto,
                                5
                ),

                formatarErroRelativo(
                        resultado.erroRelativo,
                        casasDecimais
                )
        );
    }

    /**
     * Exibe o valor exato com mais casas para não esconder
     * a diferença em relação ao valor aproximado.
     */
    private String formatarValorExato(double valor) {
        return String.format(
                "%.10f",
                valor
        );
    }

    /**
     * Formata o erro relativo e a porcentagem.
     */
    private String formatarErroRelativo(
            double valor,
            int casasDecimais
    ) {

        if (Double.isNaN(valor)) {
            return "não definido";
        }

        return precisaoNumerica
                .formatarResultado(
                        valor,
                        casasDecimais
                )
                + " ("
                + String.format(
                        "%."
                        + casasDecimais
                        + "f",
                        valor * 100
                )
                + "%)";
    }

    /**
     * Armazena o resultado de cada teste.
     */
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

            this.metodo =
                    metodo;

            this.resultadoExato =
                    resultadoExato;

            this.resultadoAproximado =
                    resultadoAproximado;

            this.erroAbsoluto =
                    erroAbsoluto;

            this.erroRelativo =
                    erroRelativo;
        }
    }
}
