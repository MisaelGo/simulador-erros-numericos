package simuladorerrosnumericos;

import java.util.Scanner;

/**
 * Controla a interação com o usuário e integra
 * as demais classes do simulador.
 */
public class Menu {

    private final Scanner scanner;
    private final Calculadora calculadora;
    private final PrecisaoNumerica precisaoNumerica;
    private final CalculadoraErro calculadoraErro;

    /**
     * Inicializa as classes utilizadas pelo menu.
     */
    public Menu() {

        scanner =
                new Scanner(System.in);

        calculadora =
                new Calculadora();

        precisaoNumerica =
                new PrecisaoNumerica();

        calculadoraErro =
                new CalculadoraErro();
    }

    /**
     * Executa o fluxo principal do simulador.
     */
    public void iniciar() {

        try (scanner) {

            boolean continuar = true;

            while (continuar) {

                System.out.println(
                        "\n=== SIMULADOR DE PROPAGAÇÃO DE ERROS ==="
                );

                double x =
                        lerDouble(
                                "Digite o valor de x: "
                        );

                double y =
                        lerDouble(
                                "Digite o valor de y: "
                        );

                int operacao =
                        lerOperacao();

                /*
                 * Evita divisão exata por zero.
                 */
                if (operacao == 4) {

                    while (y == 0.0) {

                        System.out.println(
                                "Não é possível dividir por zero."
                        );

                        y =
                                lerDouble(
                                        "Digite novamente o valor de y: "
                                );
                    }
                }

                int casasDecimais =
                        lerCasasDecimais();

                int metodo =
                        lerMetodoPrecisao();

                /*
                 * Resultado de referência com
                 * os valores originais.
                 */
                double resultadoExato =
                        realizarOperacao(
                                x,
                                y,
                                operacao
                        );

                String resultadoNormalizado =
                        precisaoNumerica
                                .formatarNotacaoCientifica(
                                        resultadoExato,
                                        casasDecimais
                                );

                /*
                 * Aplica a precisão aos operandos
                 * antes da operação aproximada.
                 */
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

                double resultadoAproximado;
                double erroAbsoluto;
                double erroRelativo;

                /*
                 * O divisor pode se tornar zero
                 * depois da aproximação.
                 */
                if (operacao == 4
                        && yAproximado == 0.0) {

                    resultadoAproximado =
                            Double.NaN;

                    erroAbsoluto =
                            Double.NaN;

                    erroRelativo =
                            Double.NaN;

                } else {

                    /*
                     * Opera utilizando os valores
                     * já aproximados.
                     */
                    resultadoAproximado =
                            realizarOperacao(
                                    xAproximado,
                                    yAproximado,
                                    operacao
                            );

                    erroAbsoluto =
                            calculadoraErro
                                    .erroAbsoluto(
                                            resultadoExato,
                                            resultadoAproximado
                                    );

                    erroRelativo =
                            calculadoraErro
                                    .erroRelativo(
                                            resultadoExato,
                                            resultadoAproximado
                                    );
                }

                exibirResultado(
                        xAproximado,
                        yAproximado,
                        resultadoExato,
                        resultadoNormalizado,
                        resultadoAproximado,
                        erroAbsoluto,
                        erroRelativo,
                        casasDecimais,
                        metodo
                );

                continuar =
                        perguntarSeContinua();
            }

            System.out.println(
                    "\nPrograma encerrado."
            );
        }
    }

    /**
     * Lê um número real.
     */
    private double lerDouble(
            String mensagem
    ) {

        while (true) {

            System.out.print(
                    mensagem
            );

            /*
             * Permite ponto ou vírgula
             * como separador decimal.
             */
            String entrada =
                    scanner
                            .next()
                            .replace(',', '.');

            try {

                return Double.parseDouble(
                        entrada
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Entrada inválida. Digite um número válido."
                );
            }
        }
    }

    /**
     * Lê um número inteiro.
     */
    private int lerInteiro(
            String mensagem
    ) {

        while (true) {

            System.out.print(
                    mensagem
            );

            String entrada =
                    scanner.next();

            try {

                return Integer.parseInt(
                        entrada
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Entrada inválida. Digite um número inteiro."
                );
            }
        }
    }

    /**
     * Solicita a operação desejada.
     */
    private int lerOperacao() {

        while (true) {

            System.out.println(
                    "\nEscolha a operação:"
            );

            System.out.println(
                    "1 - Soma"
            );

            System.out.println(
                    "2 - Subtração"
            );

            System.out.println(
                    "3 - Multiplicação"
            );

            System.out.println(
                    "4 - Divisão"
            );

            int operacao =
                    lerInteiro(
                            "Opção: "
                    );

            if (operacao >= 1
                    && operacao <= 4) {

                return operacao;
            }

            System.out.println(
                    "Operação inválida. Escolha uma opção entre 1 e 4."
            );
        }
    }

    /**
     * Solicita a quantidade de casas decimais.
     */
    private int lerCasasDecimais() {

        while (true) {

            int casasDecimais =
                    lerInteiro(
                            "\nDigite a quantidade de casas decimais: "
                    );

            if (casasDecimais >= 0) {

                return casasDecimais;
            }

            System.out.println(
                    "Valor inválido. A quantidade de casas decimais não pode ser negativa."
            );
        }
    }

    /**
     * Solicita truncamento ou arredondamento.
     */
    private int lerMetodoPrecisao() {

        while (true) {

            System.out.println(
                    "\nEscolha o método de precisão:"
            );

            System.out.println(
                    "1 - Truncamento"
            );

            System.out.println(
                    "2 - Arredondamento"
            );

            int metodo =
                    lerInteiro(
                            "Opção: "
                    );

            if (metodo == 1
                    || metodo == 2) {

                return metodo;
            }

            System.out.println(
                    "Método inválido. Escolha 1 ou 2."
            );
        }
    }

    /**
     * Realiza a operação selecionada.
     */
    private double realizarOperacao(
            double x,
            double y,
            int operacao
    ) {

        return switch (operacao) {

            case 1 ->
                calculadora.somar(
                        x,
                        y
                );

            case 2 ->
                calculadora.subtrair(
                        x,
                        y
                );

            case 3 ->
                calculadora.multiplicar(
                        x,
                        y
                );

            case 4 ->
                calculadora.dividir(
                        x,
                        y
                );

            default ->
                throw new IllegalArgumentException(
                        "Operação inválida."
                );
        };
    }

    /**
     * Exibe os resultados da simulação.
     */
    private void exibirResultado(
            double xAproximado,
            double yAproximado,
            double resultadoExato,
            String resultadoNormalizado,
            double resultadoAproximado,
            double erroAbsoluto,
            double erroRelativo,
            int casasDecimais,
            int metodo
    ) {

        System.out.println(
                "\n=== RESULTADO ==="
        );

        System.out.println(
                "Casas decimais: "
                + casasDecimais
        );

        System.out.println(
                "Método escolhido: "
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
                "Resultado exato: "
                + precisaoNumerica
                        .formatarResultado(
                                resultadoExato,
                                casasDecimais
                        )
        );

        System.out.println(
                "Resultado normalizado: "
                + resultadoNormalizado
        );

        /*
         * Trata divisão indefinida após
         * a aproximação do divisor.
         */
        if (Double.isNaN(
                resultadoAproximado
        )) {

            System.out.println(
                    "Resultado aproximado: não definido"
            );

            System.out.println(
                    "Erro absoluto: não definido"
            );

            System.out.println(
                    "Erro relativo: não definido"
            );

            return;
        }

        System.out.println(
                "Resultado aproximado: "
                + precisaoNumerica
                        .formatarResultado(
                                resultadoAproximado,
                                casasDecimais
                        )
        );

        System.out.println(
                    "Erro absoluto: "
                    + precisaoNumerica
                            .formatarResultado(
                                    erroAbsoluto,
                                    5
                        )
        );

        if (Double.isNaN(
                erroRelativo
        )) {

            System.out.println(
                    "Erro relativo: não definido "
                    + "(resultado aproximado igual a zero)"
            );

        } else {

            System.out.println(
                    "Erro relativo: "
                    + precisaoNumerica
                            .formatarResultado(
                                    erroRelativo,
                                    casasDecimais
                            )
            );

            System.out.printf(
                    "Erro relativo em porcentagem: %."
                    + casasDecimais
                    + "f%%%n",
                    erroRelativo * 100
            );
        }
    }

    /**
     * Retorna o nome do método escolhido.
     */
    private String nomeMetodo(
            int metodo
    ) {

        return metodo == 1
                ? "Truncamento"
                : "Arredondamento";
    }

    /**
     * Pergunta se será feito outro cálculo.
     */
    private boolean perguntarSeContinua() {

        while (true) {

            System.out.println(
                    "\nDeseja realizar outro cálculo?"
            );

            System.out.println(
                    "1 - Sim"
            );

            System.out.println(
                    "2 - Não"
            );

            int resposta =
                    lerInteiro(
                            "Opção: "
                    );

            if (resposta == 1) {
                return true;
            }

            if (resposta == 2) {
                return false;
            }

            System.out.println(
                    "Opção inválida. Digite 1 para sim ou 2 para não."
            );
        }
    }
}