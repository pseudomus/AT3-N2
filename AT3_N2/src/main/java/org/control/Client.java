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
                displayMenu();

                // Lê a escolha do usuário
                int choice = Integer.parseInt(scanner.nextLine());

                // Processa a escolha do usuário
                running = processChoice(choice, out, in, scanner);
            }
        } catch (Exception e) {
            // Trata exceções e imprime o stack trace
            e.printStackTrace();
        }
    }

    // Método para exibir o menu de opções
    private static void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Listar livros");
        System.out.println("2. Alugar livro");
        System.out.println("3. Devolver livro");
        System.out.println("4. Cadastrar livro");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    // Método para processar a escolha do usuário
    private static boolean processChoice(int choice, PrintWriter out, BufferedReader in, Scanner scanner) throws IOException {
        switch (choice) {
            case 1:
                // Envia comando de listagem de livros para o servidor
                out.println("listar");
                break;
            case 2:
                // Lista livros antes de solicitar o nome do livro para alugar
                listBooks(out, in);
                String rentBook = promptForInput("Digite o nome do livro para alugar: ", scanner);
                out.println("alugar#" + rentBook);
                break;
            case 3:
                // Lista livros antes de solicitar o nome do livro para devolver
                listBooks(out, in);
                String returnBook = promptForInput("Digite o nome do livro para devolver: ", scanner);
                out.println("devolver#" + returnBook);
                break;
            case 4:
                // Lista livros antes de solicitar os detalhes do novo livro
                listBooks(out, in);
                String bookDetails = promptForInput("Digite os detalhes do livro (autor,titulo,genero,exemplares): ", scanner);
                out.println("cadastrar#" + bookDetails);
                break;
            case 5:
                // Envia comando de sair para o servidor e retorna false para terminar o loop
                out.println("sair");
                return false;
            default:
                // Informa ao usuário que a opção é inválida e pede para tentar novamente
                System.out.println("Opção inválida. Tente novamente.");
                return true; // Pula a leitura da resposta do servidor se a opção for inválida
        }

        // Imprime a resposta do servidor antes de mostrar o menu novamente
        printResponse(in);
        return true; // Continua o loop
    }

    // Método auxiliar para solicitar entrada do usuário
    private static String promptForInput(String prompt, Scanner scanner) {
        System.out.print(prompt);
        return scanner.nextLine();
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

    // Método para imprimir a resposta do servidor com formatação específica para livros
    private static void printFormattedResponse(BufferedReader in) throws IOException {
        String responseLine;
        int bookCount = 1;
        while ((responseLine = in.readLine()) != null && !responseLine.isEmpty()) {
            // Assume que cada linha é um livro com campos separados por vírgula: autor,titulo,genero,exemplares
            String[] bookDetails = responseLine.split(",");
            if (bookDetails.length == 4) {
                System.out.println("Livro " + bookCount + ":");
                System.out.println("Autor: " + bookDetails[0].trim());
                System.out.println("Título: " + bookDetails[1].trim());
                System.out.println("Gênero: " + bookDetails[2].trim());
                System.out.println("Exemplares: " + bookDetails[3].trim());
                System.out.println("----------------------------");
                bookCount++;
            }
        }
    }

    // Método para listar livros antes de solicitar uma ação do usuário
    private static void listBooks(PrintWriter out, BufferedReader in) throws IOException {
        out.println("listar");
        System.out.println("Lista de Livros Disponíveis:");
        System.out.println("----------------------------");
        printFormattedResponse(in);
        System.out.println("----------------------------");
    }
}

