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

## Testes Unitários

O projeto conta com testes automatizados que verificam:

- Download dos anexos
- Criação do arquivo ZIP
- Criação do diretório de downloads

### Como Executar os Testes

Execute o comando:

```bash
mvn test
```

### O que é Testado

- `testDownloadAnexos`: Verifica se os anexos são baixados corretamente
- `testCreateZip`: Verifica se o arquivo ZIP é criado com sucesso
- `testDownloadDirCreation`: Verifica se o diretório de downloads é criado

**Observação**: Os testes fazem download real dos arquivos apenas uma vez e limpam todos os arquivos após a execução.

# Teste de Transformação de dados

## Objetivo

Extrair tabelas do PDF baixado (Anexos I) e convertê-las para CSV, realizando as seguintes operações:

- Leitura do arquivo PDF
- Extração das tabelas
- Conversão para formato CSV
- Limpeza e formatação dos dados

## Pré-requisitos

- Java 23
- Maven 3.9.9
- PDFs dos Anexos I e II (gerados pelo teste de Web Scraping)

## Como Executar

1. **Certifique-se que o PDF esta baixado**:

   ```bash
   cd challenge/intuitive-care
   mvn clean compile exec:java -Dexec.mainClass="com.br.intuitivecare.webscraping.Scraper"
   ```

2. **Execute o processamento do PDF**:

   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.br.intuitivecare.pdfdataprocessing.PDFProcessor"
   ```

3. **Saída esperada**:

- Arquivos CSV correspondentes à tabela extraída na pasta 'csvDir'

## Testes Unitários

O projeto inclui testes automatizados que verificam:

- Extração de tabela do PDF
- Conversão para CSV
- Limpeza e formatação dos dados
- Criação do diretório de saída

### Como Executar os Testes

Execute o comando:

```bash
mvn test
```

### O que é Testado

- `testPDFExtraction`: Verifica se a tabela é extraída corretamente do PDF
- `testCSVConversion`: Verifica se a conversão para CSV é realizada com sucesso
- `testDataCleaning`: Verifica se a limpeza e formatação dos dados está correta
- `testOutputDirCreation`: Verifica se o diretório de saída é criado adequadamente

**Observação**: Os testes necessitam do arquivo PDF previamente baixado e limpam os arquivos CSV gerados após a execução.