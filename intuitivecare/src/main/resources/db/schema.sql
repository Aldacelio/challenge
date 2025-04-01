CREATE TABLE IF NOT EXISTS operadoras (
    registro_ans VARCHAR(20) PRIMARY KEY,
    cnpj VARCHAR(20) NOT NULL,
    razao_social VARCHAR(255) NOT NULL,
    nome_fantasia VARCHAR(255),
    modalidade VARCHAR(100),
    logradouro VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    uf VARCHAR(2),
    cep VARCHAR(10),
    ddd VARCHAR(5),
    telefone VARCHAR(20),
    fax VARCHAR(20),
    endereco_eletronico VARCHAR(100),
    representante VARCHAR(100),
    cargo_representante VARCHAR(100),
    regiao_de_comercializacao VARCHAR(100),
    data_registro_ans DATE
);

CREATE TABLE IF NOT EXISTS demonstracoes_contabeis (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    reg_ans VARCHAR(20) NOT NULL,
    cd_conta_contabil VARCHAR(20) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    vl_saldo_inicial NUMERIC(15,2),
    vl_saldo_final NUMERIC(15,2)
);

CREATE INDEX IF NOT EXISTS idx_dc_data ON demonstracoes_contabeis(data);
CREATE INDEX IF NOT EXISTS idx_dc_descricao ON demonstracoes_contabeis(descricao);
CREATE INDEX IF NOT EXISTS idx_dc_reg_ans ON demonstracoes_contabeis(reg_ans);