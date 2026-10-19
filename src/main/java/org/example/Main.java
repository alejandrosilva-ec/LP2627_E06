package org.example;

/**
 * Classe principal da aplicação.
 *
 * <p>O método {@link #main()} apresenta uma mensagem de boas-vindas e imprime
 * os números de 1 a 5 na saída padrão.
 */
public class Main {
    /**
     * Ponto de entrada que apresenta uma mensagem de boas-vindas e imprime
     * os números de 1 a 5, um por linha, na saída padrão.
     */
    static void main() {
        // Dica: pressione <shortcut actionId="ShowIntentionActions"/> com o cursor no texto destacado
        // para ver as sugestões de correção do IntelliJ IDEA.
        IO.println(String.format("Hello and welcome!"));

        for (int i = 1; i <= 5; i++) {
            // Dica: pressione <shortcut actionId="Debug"/> para iniciar a depuração do código. Foi definido um ponto de interrupção
            // para si, mas pode adicionar outros pressionando <shortcut actionId="ToggleLineBreakpoint"/>.
            IO.println("i = " + i);
        }
    }
}
