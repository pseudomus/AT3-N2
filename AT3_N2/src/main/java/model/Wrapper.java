package model;

import java.util.List;

// Classe Wrapper que encapsula uma lista de livros
public class Wrapper {
    // Atributo para armazenar a lista de livros
    private List<Livro> livros;

    // Construtor que inicializa a lista de livros
    public Wrapper(List<Livro> livros) {
        this.livros = livros;
    }

    // Método getter para obter a lista de livros
    public List<Livro> getLivros() {
        return livros;
    }

    // Método setter para definir a lista de livros
    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
}
