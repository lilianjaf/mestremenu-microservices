CREATE TABLE restaurante (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco_logradouro VARCHAR(255) NOT NULL,
    endereco_numero VARCHAR(255) NOT NULL,
    endereco_complemento VARCHAR(255),
    endereco_bairro VARCHAR(255) NOT NULL,
    endereco_cidade VARCHAR(255) NOT NULL,
    endereco_cep VARCHAR(255) NOT NULL,
    endereco_uf VARCHAR(2) NOT NULL,
    tipo_cozinha VARCHAR(255) NOT NULL,
    horario_funcionamento VARCHAR(255) NOT NULL,
    id_dono UUID NOT NULL,
    CONSTRAINT fk_restaurante_usuario FOREIGN KEY (id_dono) REFERENCES usuario(id)
);
