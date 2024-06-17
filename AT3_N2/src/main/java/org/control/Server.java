package org.control;

import model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// Classe Server que gerencia o servidor da biblioteca
public class Server {
    // Caminho para o arquivo JSON que armazena os livros
    private static final String ARQUIVO_JSON = "src/main/resources/livros.json";
    // Lista estática de livros compartilhada entre as threads
    static List<Livro> livros;

    // Método principal que inicia o servidor
    public static void main(String[] args) {
        // Carrega os livros do arquivo JSON
        carregarLivros();

        // Porta na qual o servidor irá escutar
        final int PORTA = 12344;

        try {
            // Cria um socket do servidor que escuta na porta especificada
            ServerSocket servidorSocket = new ServerSocket(PORTA);
            System.out.println("Servidor pronto para receber conexões...");

            // Loop principal que aceita conexões de clientes
            while (true) {
                // Aceita a conexão do cliente
                Socket clienteSocket = servidorSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket);

                // Cria uma nova thread para lidar com o cliente conectado
                Thread clienteThread = new Thread(new Handler(clienteSocket));
                clienteThread.start();
            }
        } catch (IOException e) {
            // Trata exceções e imprime o stack trace
            e.printStackTrace();
        }
    }

    // Método sincronizado para carregar os livros do arquivo JSON
    static synchronized void carregarLivros() {
        // Cria um ObjectMapper para manipular o JSON
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(ARQUIVO_JSON);
        if (file.exists()) {
            try {
                // Lê a árvore JSON do arquivo
                JsonNode rootNode = mapper.readTree(file);
                // Obtém o nó que contém a lista de livros
                JsonNode livrosNode = rootNode.path("livros");
                // Define o tipo da coleção como List<Livro>
                CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Livro.class);
                // Converte o nó JSON para uma lista de objetos Livro
                livros = mapper.convertValue(livrosNode, listType);
            } catch (IOException e) {
                // Trata exceções e imprime o stack trace
                e.printStackTrace();
                livros = new ArrayList<>();
            }
        } else {
            // Caso o arquivo JSON não exista, cria um novo arquivo e inicializa a lista de livros
            System.out.println("Arquivo JSON não encontrado, criando novo arquivo...");
            livros = new ArrayList<>();
            salvarLivros();
        }
    }

    // Método sincronizado para salvar os livros no arquivo JSON
    static synchronized void salvarLivros() {
        // Cria um ObjectMapper para manipular o JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Escreve a lista de livros no arquivo JSON encapsulada em um Wrapper
            mapper.writeValue(new File(ARQUIVO_JSON), new Wrapper(livros));
        } catch (IOException e) {
            // Trata exceções e imprime o stack trace
            e.printStackTrace();
        }
    }
}
