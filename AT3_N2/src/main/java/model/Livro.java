package model;

// Classe Livro que representa um livro na biblioteca
public class Livro {
    // Atributo para armazenar o autor do livro
    private String autor;
    // Atributo para armazenar o título do livro
    private String titulo;
    // Atributo para armazenar o gênero do livro
    private String genero;
    // Atributo para armazenar o número de exemplares disponíveis do livro
    private int exemplares;

    // Construtor padrão necessário para a biblioteca Jackson (para serialização/deserialização JSON)
    public Livro() {}

    // Construtor que inicializa todos os atributos do livro
    public Livro(String autor, String titulo, String genero, int exemplares) {
        this.autor = autor;
        this.titulo = titulo;
        this.genero = genero;
        this.exemplares = exemplares;
    }

    // Método getter para o atributo autor
    public String getAutor() {
        return autor;
    }

    // Método setter para o atributo autor
    public void setAutor(String autor) {
        this.autor = autor;
    }

    // Método getter para o atributo título
    public String getTitulo() {
        return titulo;
    }

    // Método setter para o atributo título
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Método getter para o atributo gênero
    public String getGenero() {
        return genero;
    }

    // Método setter para o atributo gênero
    public void setGenero(String genero) {
        this.genero = genero;
    }

    // Método getter para o atributo número de exemplares
    public int getExemplares() {
        return exemplares;
    }

    // Método setter para o atributo número de exemplares
    public void setExemplares(int exemplares) {
        this.exemplares = exemplares;
    }

    // Método toString para retornar uma representação em string do objeto Livro
    @Override
    public String toString() {
        return "Livro{" +
                "autor='" + autor + '\'' +
                ", titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                ", exemplares=" + exemplares +
                '}';
    }
}
