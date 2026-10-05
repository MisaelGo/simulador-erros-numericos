package simuladorerrosnumericos;

import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final Calculadora calculadora;

    public Menu() {
        scanner = new Scanner(System.in);
        calculadora = new Calculadora();
    }

    public void iniciar() {

        try (scanner) {

            boolean continuar = true;

            while (continuar) {

                System.out.println("\n=== SIMULADOR DE PROPAGAÇÃO DE ERROS ===");

                double x = lerDouble("Digite o valor de x: ");
                double y = lerDouble("Digite o valor de y: ");

                int operacao = lerOperacao();

                int digitos = lerDigitosSignificativos();

                int metodo = lerMetodoPrecisao();

                double resultado = realizarOperacaoComTratamento(
                        x,
                        y,
                        operacao
                );

                System.out.println("\n=== RESULTADO ===");
                System.out.println("Resultado exato: " + resultado);
                System.out.println(
                        "Dígitos significativos: " + digitos
                );

                if (metodo == 1) {
                    System.out.println(
                            "Método escolhido: Truncamento"
                    );
                } else {
                    System.out.println(
                            "Método escolhido: Arredondamento"
                    );
                }

                continuar = perguntarSeContinua();
            }

            System.out.println("\nPrograma encerrado.");
        }
    }

    private double lerDouble(String mensagem) {

        while (true) {

            try {
                System.out.print(mensagem);
                return scanner.nextDouble();

            } catch (InputMismatchException e) {
                System.out.println(
                        "Entrada inválida. Digite um número válido."
                );
                scanner.nextLine();
            }
        }
    }

    private int lerInteiro(String mensagem) {

        while (true) {

            try {
                System.out.print(mensagem);
                return scanner.nextInt();

            } catch (InputMismatchException e) {
                System.out.println(
                        "Entrada inválida. Digite um número inteiro."
                );
                scanner.nextLine();
            }
        }
    }

    private int lerOperacao() {

        while (true) {

            System.out.println("\nEscolha a operação:");
            System.out.println("1 - Soma");
            System.out.println("2 - Subtração");
            System.out.println("3 - Multiplicação");
            System.out.println("4 - Divisão");

            int operacao = lerInteiro("Opção: ");

            if (operacao >= 1 && operacao <= 4) {
                return operacao;
            }

            System.out.println(
                    "Operação inválida. Escolha uma opção entre 1 e 4."
            );
        }
    }

    private int lerDigitosSignificativos() {

        while (true) {

            int digitos = lerInteiro(
                    "\nDigite a quantidade de dígitos significativos: "
            );

            if (digitos > 0) {
                return digitos;
            }

            System.out.println(
                    "Valor inválido. Digite uma quantidade maior que zero."
            );
        }
    }

    private int lerMetodoPrecisao() {

        while (true) {

            System.out.println("\nEscolha o método de precisão:");
            System.out.println("1 - Truncamento");
            System.out.println("2 - Arredondamento");

            int metodo = lerInteiro("Opção: ");

            if (metodo == 1 || metodo == 2) {
                return metodo;
            }

            System.out.println(
                    "Método inválido. Escolha 1 ou 2."
            );
        }
    }

    private double realizarOperacaoComTratamento(
            double x,
            double y,
            int operacao
    ) {

        while (true) {

            try {
                return realizarOperacao(x, y, operacao);

            } catch (ArithmeticException e) {

                System.out.println(e.getMessage());

                if (operacao == 4) {
                    y = lerDouble(
                            "Digite novamente o valor de y: "
                    );
                }
            }
        }
    }

    private double realizarOperacao(
            double x,
            double y,
            int operacao
    ) {

        return switch (operacao) {

            case 1 -> calculadora.somar(x, y);

            case 2 -> calculadora.subtrair(x, y);

            case 3 -> calculadora.multiplicar(x, y);

            case 4 -> calculadora.dividir(x, y);

            default -> throw new IllegalArgumentException(
                    "Operação inválida."
            );
        };
    }

    private boolean perguntarSeContinua() {

        while (true) {

            System.out.println("\nDeseja realizar outro cálculo?");
            System.out.println("1 - Sim");
            System.out.println("2 - Não");

            int resposta = lerInteiro("Opção: ");

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