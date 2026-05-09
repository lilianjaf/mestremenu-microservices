CREATE TABLE IF NOT EXISTS pedido (
    id            UUID        NOT NULL PRIMARY KEY,
    cliente_id    UUID        NOT NULL,
    restaurante_id UUID       NOT NULL,
    valor_total   NUMERIC(10, 2) NOT NULL,
    status        VARCHAR(30) NOT NULL,
    data_criacao  TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS item_pedido (
    id        UUID           NOT NULL PRIMARY KEY,
    pedido_id UUID           NOT NULL REFERENCES pedido(id),
    descricao VARCHAR(255)   NOT NULL,
    quantidade INT            NOT NULL,
    preco     NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS outbox_event (
    id           UUID      NOT NULL PRIMARY KEY,
    aggregate_id UUID      NOT NULL,
    event_type   VARCHAR(100) NOT NULL,
    payload      TEXT      NOT NULL,
    criado_em    TIMESTAMP NOT NULL,
    processado   BOOLEAN   NOT NULL DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_outbox_processado ON outbox_event (processado) WHERE processado = FALSE;
CREATE INDEX IF NOT EXISTS idx_pedido_cliente ON pedido (cliente_id);
