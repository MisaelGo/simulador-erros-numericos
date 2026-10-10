package simuladorerrosnumericos;

/**
 * Executa os três exemplos usados para validar
 * o simulador.
 */
public class Testes {

    // Códigos usados para identificar os métodos.
    private static final int TRUNCAMENTO = 1;
    private static final int ARREDONDAMENTO = 2;

    private final Calculadora calculadora;
    private final PrecisaoNumerica precisaoNumerica;
    private final CalculadoraErro calculadoraErro;

    /**
     * Inicializa as classes utilizadas nos testes.
     */
    public Testes() {
        calculadora = new Calculadora();
        precisaoNumerica = new PrecisaoNumerica();
        calculadoraErro = new CalculadoraErro();
    }

    /**
     * Executa todos os exemplos em sequência.
     */
    public static void main(String[] args) {

        Testes testes = new Testes();

        testes.testeSomaSimples();
        testes.testeCancelamentoSubtrativo();
        testes.testePropagacaoSomas();
    }

    /**
     * Exemplo 1: soma simples.
     *
     * Soma dois números e aplica truncamento
     * e arredondamento ao resultado.
     */
    public void testeSomaSimples() {

        // Dois valores com cinco casas decimais.
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
     * Exemplo 2: cancelamento subtrativo.
     *
     * Primeiro aproxima x e y e depois realiza
     * a subtração.
     */
    public void testeCancelamentoSubtrativo() {

        // Valores fornecidos pelo professor.
        double x = 0.76545;
        double y = 0.76541;
        int casasDecimais = 4;

        System.out.println(
                "\n=== EXEMPLO 2 - CANCELAMENTO SUBTRATIVO ==="
        );

        System.out.println(
                "x original: " + x
        );

        System.out.println(
                "y original: " + y
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
     * Exemplo 3: propagação do erro em somas.
     *
     * Soma 0.56786 dez vezes e ajusta o resultado
     * após cada operação.
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
     * Executa a soma simples e ajusta o resultado.
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
     * Executa a subtração após aproximar
     * separadamente os dois operandos.
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

        // Aproxima os operandos antes da subtração.
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
                + xAproximado
        );

        System.out.println(
                "y aproximado: "
                + yAproximado
        );

        System.out.println(
                "Resultado aproximado da subtração: "
                + resultadoAproximado
        );

        return criarResultado(
                metodo,
                resultadoExato,
                resultadoAproximado
        );
    }

    /**
     * Executa as somas sucessivas e aplica o ajuste
     * após cada nova soma.
     */
    private ResultadoTeste executarSomasSucessivas(
            double parcela,
            int repeticoes,
            int casasDecimais,
            int metodo,
            boolean mostrarEtapas
    ) {

        // Valor de referência sem limitar a precisão.
        double resultadoExato =
                parcela * repeticoes;

        /*
         * O exemplo começa em:
         * parcela + parcela.
         */
        double resultadoAproximado =
                parcela;

        if (mostrarEtapas) {

            System.out.println(
                    "\nMétodo: "
                    + nomeMetodo(metodo)
            );
        }

        // Repete a soma até completar dez parcelas.
        for (int termo = 2;
                termo <= repeticoes;
                termo++) {

            double antesDoAjuste =
                    calculadora.somar(
                            resultadoAproximado,
                            parcela
                    );

            // Limita novamente o acumulado após cada soma.
            resultadoAproximado =
                    precisaoNumerica
                            .ajustarPrecisao(
                                    antesDoAjuste,
                                    casasDecimais,
                                    metodo
                            );

            if (mostrarEtapas) {

                System.out.printf(
                        "Soma %d: %.10f -> %.10f%n",
                        termo - 1,
                        antesDoAjuste,
                        resultadoAproximado
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
     * Calcula os erros e agrupa os dados de um teste.
     */
    private ResultadoTeste criarResultado(
            int metodo,
            double resultadoExato,
            double resultadoAproximado
    ) {

        double erroAbsoluto =
                calculadoraErro.erroAbsoluto(
                        resultadoExato,
                        resultadoAproximado
                );

        double erroRelativo =
                calculadoraErro.erroRelativo(
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
     * Converte o código do método para texto.
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
     * Exibe a comparação entre truncamento
     * e arredondamento.
     */
    private void exibirComparacao(
            String titulo,
            int casasDecimais,
            ResultadoTeste truncamento,
            ResultadoTeste arredondamento
    ) {

        System.out.println(
                "\n=== " + titulo + " ==="
        );

        System.out.println(
                "Casas decimais: "
                + casasDecimais
        );

        System.out.printf(
                "%-15s %18s %18s %18s %18s%n",
                "Método",
                "Valor exato",
                "Valor aproximado",
                "Erro absoluto",
                "Erro relativo"
        );

        exibirLinha(truncamento);
        exibirLinha(arredondamento);
    }

    /**
     * Exibe uma linha da tabela de resultados.
     */
    private void exibirLinha(
            ResultadoTeste resultado
    ) {

        System.out.printf(
                "%-15s %18s %18s %18s %18s%n",
                resultado.metodo,
                formatar(
                        resultado.resultadoExato
                ),
                formatar(
                        resultado.resultadoAproximado
                ),
                formatar(
                        resultado.erroAbsoluto
                ),
                formatarErroRelativo(
                        resultado.erroRelativo
                )
        );
    }

    /**
     * Formata valores em notação científica.
     */
    private String formatar(
            double valor
    ) {

        return String.format(
                "%.10e",
                valor
        );
    }

    /**
     * Formata o erro relativo em valor decimal
     * e porcentagem.
     */
    private String formatarErroRelativo(
            double valor
    ) {

        if (Double.isNaN(valor)) {
            return "não definido";
        }

        return String.format(
                "%.6f (%.2f%%)",
                valor,
                valor * 100
        );
    }

    /**
     * Estrutura interna usada para armazenar
     * o resultado de cada teste.
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
            this.metodo = metodo;
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
