CREATE TABLE cardapio (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    id_restaurante UUID NOT NULL,
    CONSTRAINT fk_cardapio_restaurante FOREIGN KEY (id_restaurante) REFERENCES restaurante(id)
);

CREATE TABLE item_cardapio (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    disponibilidade_restaurante BOOLEAN NOT NULL,
    caminho_foto VARCHAR(500) NOT NULL,
    cardapio_id UUID NOT NULL,
    CONSTRAINT fk_item_cardapio_cardapio FOREIGN KEY (cardapio_id) REFERENCES cardapio(id)
);
