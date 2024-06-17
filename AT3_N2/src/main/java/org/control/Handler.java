package org.control;

import model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// Classe Handler que lida com a conexão de cada cliente
public class Handler implements Runnable {
    // Socket do cliente conectado
    private Socket clienteSocket;

    // Construtor que inicializa o Handler com o socket do cliente
    public Handler(Socket clienteSocket) {
        this.clienteSocket = clienteSocket;
    }

    // Método run que é executado quando o Handler é iniciado
    @Override
    public void run() {
        // Bloco try-with-resources para garantir o fechamento dos recursos
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
             PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true)) {

            // Variável para armazenar a requisição do cliente
            String requisicaoCliente;
            // Loop para processar as requisições do cliente
            while ((requisicaoCliente = entrada.readLine()) != null) {
                // Processa a requisição do cliente e gera uma resposta
                String resposta = processarRequisicao(requisicaoCliente);
                int respostaLength = resposta.length();
                // Envia a resposta para o cliente, prefixada com o comprimento
                saida.println(respostaLength + "#" + resposta);
            }
        } catch (IOException e) {
            // Trata exceções e imprime o stack trace
            e.printStackTrace();
        } finally {
            // Fecha o socket do cliente no final
            try {
                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para processar a requisição do cliente
    private String processarRequisicao(String requisicao) {
        // Divide a requisição em partes usando o delimitador '#'
        String[] partes = requisicao.split("#");
        String operacao = partes[0];

        // Executa a operação solicitada pelo cliente
        switch (operacao) {
            case "listar":
                return listarLivros();
            case "alugar":
                return alugarLivro(partes[1]);
            case "devolver":
                return devolverLivro(partes[1]);
            case "cadastrar":
                return cadastrarLivro(partes[1]);
            case "sair":
                return "Saindo...";
            default:
                return "Operação inválida.";
        }
    }

    // Método para listar os livros disponíveis
    private String listarLivros() {
        StringBuilder resposta = new StringBuilder();
        // Sincroniza o acesso à lista de livros
        synchronized (Server.livros) {
            for (Livro livro : Server.livros) {
                resposta.append(livro.toString()).append("\n");
            }
        }
        return resposta.toString();
    }

    // Método para alugar um livro
    private String alugarLivro(String nomeLivro) {
        // Sincroniza o acesso à lista de livros
        synchronized (Server.livros) {
            for (Livro livro : Server.livros) {
                if (livro.getTitulo().equals(nomeLivro)) {
                    if (livro.getExemplares() > 0) {
                        livro.setExemplares(livro.getExemplares() - 1);
                        Server.salvarLivros();
                        return "Livro alugado com sucesso.";
                    } else {
                        return "Não há exemplares disponíveis para este livro.";
                    }
                }
            }
        }
        return "Livro não encontrado.";
    }

    // Método para devolver um livro
    private String devolverLivro(String nomeLivro) {
        // Sincroniza o acesso à lista de livros
        synchronized (Server.livros) {
            for (Livro livro : Server.livros) {
                if (livro.getTitulo().equals(nomeLivro)) {
                    livro.setExemplares(livro.getExemplares() + 1);
                    Server.salvarLivros();
                    return "Livro devolvido com sucesso.";
                }
            }
        }
        return "Livro não encontrado.";
    }

    // Método para cadastrar um novo livro
    private String cadastrarLivro(String livroJson) {
        // Divide os atributos do livro usando o delimitador ','
        String[] atributos = livroJson.split(",");
        if (atributos.length != 4) {
            return "Formato de cadastro inválido. Use: autor,titulo,genero,exemplares";
        }
        String autor = atributos[0].trim();
        String titulo = atributos[1].trim();
        String genero = atributos[2].trim();
        int exemplares;
        try {
            exemplares = Integer.parseInt(atributos[3].trim());
        } catch (NumberFormatException e) {
            return "Número de exemplares inválido.";
        }
        // Cria um novo objeto Livro com os atributos fornecidos
        Livro novoLivro = new Livro(autor, titulo, genero, exemplares);
        // Sincroniza o acesso à lista de livros e adiciona o novo livro
        synchronized (Server.livros) {
            Server.livros.add(novoLivro);
            Server.salvarLivros();
        }
        return "Livro cadastrado com sucesso.";
    }
}
