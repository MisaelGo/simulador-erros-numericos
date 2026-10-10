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
     * Inicializa as dependências usadas pelo menu.
     */
    public Menu() {
        scanner = new Scanner(System.in);
        calculadora = new Calculadora();
        precisaoNumerica = new PrecisaoNumerica();
        calculadoraErro = new CalculadoraErro();
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

                // Lê os dois operandos.
                double x =
                        lerDouble(
                                "Digite o valor de x: "
                        );

                double y =
                        lerDouble(
                                "Digite o valor de y: "
                        );

                // Lê as opções da simulação.
                int operacao =
                        lerOperacao();

                int casasDecimais =
                        lerCasasDecimais();

                int metodo =
                        lerMetodoPrecisao();

                // Calcula o resultado sem limitar a precisão.
                double resultadoExato =
                        realizarOperacaoComTratamento(
                                x,
                                y,
                                operacao
                        );

                // Gera a forma normalizada do resultado.
                String resultadoNormalizado =
                        precisaoNumerica
                                .formatarNotacaoCientifica(
                                        resultadoExato
                                );

                /*
                 * No fluxo comum, a precisão é aplicada
                 * depois da operação.
                 */
                double resultadoAproximado =
                        precisaoNumerica
                                .ajustarPrecisao(
                                        resultadoExato,
                                        casasDecimais,
                                        metodo
                                );

                // Calcula os erros do resultado aproximado.
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

                // Mostra todas as informações da operação.
                exibirResultado(
                        resultadoExato,
                        resultadoNormalizado,
                        resultadoAproximado,
                        erroAbsoluto,
                        erroRelativo,
                        casasDecimais,
                        metodo
                );

                // Permite realizar novos cálculos sem reiniciar.
                continuar =
                        perguntarSeContinua();
            }

            System.out.println(
                    "\nPrograma encerrado."
            );
        }
    }

    /**
     * Lê um número real e repete somente este campo
     * caso a entrada seja inválida.
     */
    private double lerDouble(
            String mensagem
    ) {

        while (true) {

            System.out.print(mensagem);

            /*
             * Permite usar tanto vírgula quanto ponto
             * como separador decimal.
             */
            String entrada =
                    scanner.next()
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
     * Lê um número inteiro e repete somente este campo
     * caso a entrada seja inválida.
     */
    private int lerInteiro(
            String mensagem
    ) {

        while (true) {

            System.out.print(mensagem);

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
     * Solicita uma das quatro operações disponíveis.
     */
    private int lerOperacao() {

        while (true) {

            System.out.println(
                    "\nEscolha a operação:"
            );

            System.out.println("1 - Soma");
            System.out.println("2 - Subtração");
            System.out.println("3 - Multiplicação");
            System.out.println("4 - Divisão");

            int operacao =
                    lerInteiro("Opção: ");

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
     * Lê a quantidade de casas decimais desejada.
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
     * Solicita o método de ajuste da precisão.
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
                    lerInteiro("Opção: ");

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
     * Executa a operação e trata divisão por zero.
     */
    private double realizarOperacaoComTratamento(
            double x,
            double y,
            int operacao
    ) {

        while (true) {

            try {

                return realizarOperacao(
                        x,
                        y,
                        operacao
                );

            } catch (ArithmeticException e) {

                System.out.println(
                        e.getMessage()
                );

                /*
                 * Em uma divisão inválida, solicita
                 * somente um novo valor para y.
                 */
                if (operacao == 4) {

                    y = lerDouble(
                            "Digite novamente o valor de y: "
                    );
                }
            }
        }
    }

    /**
     * Encaminha a operação escolhida para Calculadora.
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
     * Exibe os resultados e os erros calculados.
     */
    private void exibirResultado(
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
                "Resultado exato: "
                + resultadoExato
        );

        System.out.println(
                "Resultado normalizado: "
                + resultadoNormalizado
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
                "Resultado aproximado: "
                + resultadoAproximado
        );

        System.out.println(
                "Erro absoluto: "
                + erroAbsoluto
        );

        // Evita exibir um valor inválido quando o divisor é zero.
        if (Double.isNaN(erroRelativo)) {

            System.out.println(
                    "Erro relativo: não definido "
                    + "(resultado aproximado igual a zero)"
            );

        } else {

            System.out.println(
                    "Erro relativo: "
                    + erroRelativo
            );

            System.out.printf(
                    "Erro relativo em porcentagem: %.4f%%%n",
                    erroRelativo * 100
            );
        }
    }

    /**
     * Retorna o nome do método de precisão selecionado.
     */
    private String nomeMetodo(
            int metodo
    ) {

        return metodo == 1
                ? "Truncamento"
                : "Arredondamento";
    }

    /**
     * Pergunta se o usuário deseja realizar outro cálculo.
     */
    private boolean perguntarSeContinua() {

        while (true) {

            System.out.println(
                    "\nDeseja realizar outro cálculo?"
            );

            System.out.println("1 - Sim");
            System.out.println("2 - Não");

            int resposta =
                    lerInteiro("Opção: ");

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
