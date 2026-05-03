CREATE TABLE tipo_usuario (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    tipo_nativo VARCHAR(255) NOT NULL
);

CREATE TABLE usuario (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario_id UUID NOT NULL,
    endereco_logradouro VARCHAR(255) NOT NULL,
    endereco_numero VARCHAR(255) NOT NULL,
    endereco_complemento VARCHAR(255),
    endereco_bairro VARCHAR(255) NOT NULL,
    endereco_cidade VARCHAR(255) NOT NULL,
    endereco_cep VARCHAR(255) NOT NULL,
    endereco_uf VARCHAR(2) NOT NULL,
    data_ultima_alteracao TIMESTAMP NOT NULL,
    ativo BOOLEAN NOT NULL,
    CONSTRAINT fk_usuario_tipo_usuario FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
);
