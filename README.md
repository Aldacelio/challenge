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
mvn test -Dtest=DownloadFilesTest
```

### O que é Testado

- `testDownloadAnexos`: Verifica se os anexos são baixados corretamente
- `testCreateZip`: Verifica se o arquivo ZIP é criado com sucesso
- `testDownloadDirCreation`: Verifica se o diretório de downloads é criado

**Observação**: Os testes fazem download real dos arquivos apenas uma vez e limpam todos os arquivos após a execução.

# Teste de Transformação de dados

## Objetivo

Extrair tabela do PDF baixado (Anexos I) e convertê-la para CSV, realizando as seguintes operações:

- Leitura do arquivo PDF
- Extração das tabelas
- Conversão para formato CSV
- Limpeza e formatação dos dados

## Pré-requisitos

- Java 23
- Maven 3.9.9
- PDF do Anexo I (gerado pelo teste de Web Scraping)

## Como Executar

1. **Certifique-se que o PDF esta baixado**:

   ```bash
   cd challenge/intuitive-care
   mvn clean compile exec:java -Dexec.mainClass="com.br.intuitivecare.webscraping.Scraper"
   ```

2. **Execute o processamento do PDF**:

   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.br.intuitivecare.pdfdataprocessing.PdfDataProcessing"
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
mvn test -Dtest=PdfDataProcessingTest
```

### O que é Testado

- `testPDFExtraction`: Verifica se a tabela é extraída corretamente do PDF
- `testCSVConversion`: Verifica se a conversão para CSV é realizada com sucesso
- `testDataCleaning`: Verifica se a limpeza e formatação dos dados está correta
- `testOutputDirCreation`: Verifica se o diretório de saída é criado adequadamente

**Observação**: Os testes necessitam do arquivo PDF previamente baixado e limpam os arquivos CSV gerados após a execução.

# Teste de Banco de dados

## Objetivo

Realizar análise de dados das demonstrações contábeis das operadoras de planos de saúde, com as seguintes funcionalidades:

- Criação de tabelas no banco de dados PostgreSQL
- Importação de dados das operadoras e demonstrações contábeis
- Análise das 10 operadoras que mais tiveram despesas com "EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS" no último trimestre e último ano

## Pré-requisitos

- Java 23
- Maven 3.9.9
- PostgreSQL 15
- Banco de dados configurado com as credenciais corretas

## Como Executar

1. **Configure o banco de dados**:

   Certifique-se que o PostgreSQL está rodando e as credenciais estão configuradas corretamente.

2. **Execute a aplicação**:

   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.br.intuitivecare.databaseanalysis.DatabaseAnalysis"
   ```

3. **Funcionalidades disponíveis**:

   - Criar tabelas: Cria as estruturas necessárias no banco de dados
   - Importar dados: Importa os dados dos CSVs para o banco
   - Top 10 maiores despesas: Exibe relatório das operadoras com maiores despesas

## Observações

1. **Dados versionados**

   - Sei que não é uma boa prática subir arquivos como os que subi porém foi para facilitar na hora da avaliação
   - Tempo médio da importação de dados: 5 minutos
   - Implementei uma limpeza de tabelas para que caso queira executar o teste mais de uma vez, não tenha duplicação de dados