package model;

import java.util.List;

// Classe Wrapper que encapsula uma lista de livros
public class Wrapper {
    // Atributo para armazenar a lista de livros
    private List<Livro> books;

    // Construtor que inicializa a lista de livros
    public Wrapper(List<Livro> books) {
        this.books = books;
    }

    // Método getter para obter a lista de livros
    public List<Livro> getBooks() {
        return books;
    }

    // Método setter para definir a lista de livros
    public void setBooks(List<Livro> books) {
        this.books = books;
    }
}
