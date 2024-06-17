package org.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Classe Client que se comunica com o servidor da biblioteca via sockets
public class Client {
    // Endereço do servidor
    private static final String SERVER_ADDRESS = "localhost";
    // Porta do servidor
    private static final int SERVER_PORT = 12344;

    // Método main que executa o cliente
    public static void main(String[] args) {
        // Bloco try-with-resources para garantir o fechamento dos recursos
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Variável para controlar o loop principal
            boolean running = true;

            // Loop principal do cliente
            while (running) {
                // Exibe o menu de opções para o usuário
                System.out.println("Menu:");
                System.out.println("1. Listar livros");
                System.out.println("2. Alugar livro");
                System.out.println("3. Devolver livro");
                System.out.println("4. Cadastrar livro");
                System.out.println("5. Sair");
                System.out.print("Escolha uma opção: ");
                int choice = Integer.parseInt(scanner.nextLine());

                // Processa a escolha do usuário
                switch (choice) {
                    case 1:
                        // Envia comando de listagem de livros para o servidor
                        out.println("listar");
                        break;
                    case 2:
                        // Solicita o nome do livro para alugar
                        System.out.print("Digite o nome do livro para alugar: ");
                        String rentBook = scanner.nextLine();
                        // Envia comando de aluguel de livro para o servidor
                        out.println("alugar#" + rentBook);
                        break;
                    case 3:
                        // Solicita o nome do livro para devolver
                        System.out.print("Digite o nome do livro para devolver: ");
                        String returnBook = scanner.nextLine();
                        // Envia comando de devolução de livro para o servidor
                        out.println("devolver#" + returnBook);
                        break;
                    case 4:
                        // Solicita os detalhes do livro para cadastrar
                        System.out.print("Digite os detalhes do livro (autor,titulo,genero,exemplares): ");
                        String bookDetails = scanner.nextLine();
                        // Envia comando de cadastro de livro para o servidor
                        out.println("cadastrar#" + bookDetails);
                        break;
                    case 5:
                        // Envia comando de sair para o servidor e termina o loop
                        out.println("sair");
                        running = false;
                        break;
                    default:
                        // Informa ao usuário que a opção é inválida e pede para tentar novamente
                        System.out.println("Opção inválida. Tente novamente.");
                        continue; // Pula a leitura da resposta do servidor se a opção for inválida
                }

                // Imprime a resposta do servidor antes de mostrar o menu novamente
                printResponse(in);
            }
        } catch (Exception e) {
            // Trata exceções e imprime o stack trace
            e.printStackTrace();
        }
    }

    // Método para imprimir a resposta do servidor
    private static void printResponse(BufferedReader in) throws IOException {
        // Lê a primeira linha da resposta
        String responseLine = in.readLine();
        if (responseLine != null) {
            // Divide a resposta em duas partes: comprimento e conteúdo
            String[] parts = responseLine.split("#", 2);
            int length = Integer.parseInt(parts[0]);
            String response = parts[1];

            // Continua lendo até que a resposta completa seja recebida
            while (response.length() < length) {
                response += in.readLine() + "\n";
            }
            // Imprime a resposta, removendo espaços em branco no final
            System.out.println(response.trim());
        }
    }
}
