# Biblioteca Socket Server

### Descrição do Projeto

Este projeto implementa um servidor de biblioteca utilizando sockets em Java 17. O servidor é responsável por gerenciar um registro/cadastro de livros, permitindo operações como listagem de livros, aluguel e devolução de livros, além de cadastro de novos livros. Os dados dos livros são armazenados em um arquivo JSON, que serve como "base de dados" da biblioteca.

# Estrutura do Projeto

#### Classes

##### Livro

Representa um livro na biblioteca e possui os seguintes atributos:

- Autor
- Nome
- Gênero
- Número de exemplares

##### Biblioteca

Gerencia a lista de livros e as operações relacionadas:

- Listar livros
- Alugar livro
- Devolver livro
- Cadastrar novo livro
- Salvar dados no arquivo JSON
- Carregar dados do arquivo JSON

##### Servidor

Responsável por receber e tratar requisições dos clientes via sockets.

##### Cliente

Envia requisições ao servidor e recebe respostas via sockets.

# Funcionalidades

#### Listagem de Livros

Exibe a lista completa de livros disponíveis na biblioteca.

#### Aluguel de Livros

Permite alugar um exemplar de um livro, diminuindo o número de exemplares disponíveis.

#### Devolução de Livros

Permite devolver um exemplar de um livro, aumentando o número de exemplares disponíveis.

#### Cadastro de Livros

Permite adicionar novos livros ao registro da biblioteca.

# Requisitos

Java 17

Biblioteca JSON para Java 
