# Teste de Web Scraping

## Objetivo

Baixar automaticamente os Anexos I e II em PDF do [portal da ANS](https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos) e compactá-los em um arquivo ZIP.

## Pré-requisitos

- Java 23
- Maven 3.9.9
- Conexão com internet (para acesso ao site da ANS)

## Como Executar

1. **Clone o repositório**:

   ```bash
   git clone https://github.com/Aldacelio/challenge.git
   cd challenge/intuitive-care
   ```

2. **Compile e execute**:

    ```bash
    mvn clean compile exec:java -Dexec.mainClass="com.br.intuitivecare.webscraping.Scraper"
    ```

3. **Saida esperada**:

- Pasta downloads com os PDFs baixados.
- Arquivo anexos.zip dentro de downloads.