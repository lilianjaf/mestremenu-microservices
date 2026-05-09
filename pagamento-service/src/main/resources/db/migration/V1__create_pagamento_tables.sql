CREATE TABLE IF NOT EXISTS pagamento (
    id             UUID           NOT NULL PRIMARY KEY,
    pedido_id      UUID           NOT NULL UNIQUE,
    valor_total    NUMERIC(10, 2) NOT NULL,
    status         VARCHAR(30)    NOT NULL,
    criado_em      TIMESTAMP      NOT NULL,
    processado_em  TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_pagamento_status    ON pagamento (status);
CREATE INDEX IF NOT EXISTS idx_pagamento_pedido_id ON pagamento (pedido_id);

CREATE TABLE IF NOT EXISTS pgto_outbox_event (
    id           UUID         NOT NULL PRIMARY KEY,
    aggregate_id UUID         NOT NULL,
    event_type   VARCHAR(100) NOT NULL,
    payload      TEXT         NOT NULL,
    criado_em    TIMESTAMP    NOT NULL,
    processado   BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_pgto_outbox_processado ON pgto_outbox_event (processado) WHERE processado = FALSE;
