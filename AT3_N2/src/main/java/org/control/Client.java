package org.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12344;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            boolean running = true;

            while (running) {
                System.out.println("Menu:");
                System.out.println("1. Listar livros");
                System.out.println("2. Alugar livro");
                System.out.println("3. Devolver livro");
                System.out.println("4. Cadastrar livro");
                System.out.println("5. Sair");
                System.out.print("Escolha uma opção: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        out.println("listar");
                        printResponse(in);
                        break;
                    case 2:
                        boolean bookRented = false;
                        while (!bookRented) {
                            System.out.print("Digite o nome do livro para alugar: ");
                            String rentBook = scanner.nextLine();
                            out.println("alugar#" + rentBook);
                            bookRented = processRentReturnResponse(in);
                        }
                        break;
                    case 3:
                        boolean bookReturned = false;
                        while (!bookReturned) {
                            System.out.print("Digite o nome do livro para devolver: ");
                            String returnBook = scanner.nextLine();
                            out.println("devolver#" + returnBook);
                            bookReturned = processRentReturnResponse(in);
                        }
                        break;
                    case 4:
                        System.out.print("Digite os detalhes do livro (autor,titulo,genero,exemplares): ");
                        String bookDetails = scanner.nextLine();
                        out.println("cadastrar#" + bookDetails);
                        printResponse(in);
                        break;
                    case 5:
                        out.println("sair");
                        running = false;
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printResponse(BufferedReader in) throws IOException {
        String responseLine;
        while ((responseLine = in.readLine()) != null && !responseLine.isEmpty()) {
            System.out.println(responseLine);
        }
    }

    private static boolean processRentReturnResponse(BufferedReader in) throws IOException {
        String responseLine;
        boolean success = false;
        while ((responseLine = in.readLine()) != null && !responseLine.isEmpty()) {
            System.out.println(responseLine);
            if (responseLine.contains("sucesso") || responseLine.contains("renting") || responseLine.contains("returning")) {
                success = true;
            } else if (responseLine.contains("não encontrado") || responseLine.contains("não disponível")) {
                System.out.println("Livro não encontrado ou não disponível. Tente novamente.");
            }
        }
        return success;
    }
}
