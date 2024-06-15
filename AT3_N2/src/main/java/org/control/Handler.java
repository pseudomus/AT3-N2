package org.control;
import model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler implements Runnable {
    private Socket clienteSocket;

    public Handler(Socket clienteSocket) {
        this.clienteSocket = clienteSocket;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
             PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true)) {

            String requisicaoCliente;
            while ((requisicaoCliente = entrada.readLine()) != null) {
                String resposta = processarRequisicao(requisicaoCliente);
                saida.println(resposta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processarRequisicao(String requisicao) {
        String[] partes = requisicao.split("#");
        String operacao = partes[0];

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

    private String listarLivros() {
        StringBuilder resposta = new StringBuilder();
        synchronized (Server.livros) {
            for (Livro livro : Server.livros) {
                resposta.append(livro.toString()).append("\n");
            }
        }
        return resposta.toString();
    }

    private String alugarLivro(String nomeLivro) {
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

    private String devolverLivro(String nomeLivro) {
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

    private String cadastrarLivro(String livroJson) {
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
        Livro novoLivro = new Livro(autor, titulo, genero, exemplares);
        synchronized (Server.livros) {
            Server.livros.add(novoLivro);
            Server.salvarLivros();
        }
        return "Livro cadastrado com sucesso.";
    }
}
