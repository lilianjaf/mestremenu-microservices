INSERT INTO tipo_usuario (id, nome, tipo_nativo)
VALUES ('a0000000-0000-0000-0000-000000000001', 'DONO', 'DONO');

INSERT INTO tipo_usuario (id, nome, tipo_nativo)
VALUES ('a0000000-0000-0000-0000-000000000002', 'CLIENTE', 'CLIENTE');

INSERT INTO usuario (id, nome, email, login, senha, tipo_usuario_id,
                     endereco_logradouro, endereco_numero, endereco_complemento,
                     endereco_bairro, endereco_cidade, endereco_cep, endereco_uf,
                     data_ultima_alteracao, ativo)
VALUES ('8c9c7e0c-84d4-4a4e-862d-0b70c3c54d3d', 'Admin Dono', 'admin@test.com', 'admin',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'a0000000-0000-0000-0000-000000000001',
        'Rua Admin', '1', null, 'Centro', 'Cidade', '12345678', 'SP',
        CURRENT_TIMESTAMP, true);

INSERT INTO usuario (id, nome, email, login, senha, tipo_usuario_id,
                     endereco_logradouro, endereco_numero, endereco_complemento,
                     endereco_bairro, endereco_cidade, endereco_cep, endereco_uf,
                     data_ultima_alteracao, ativo)
VALUES ('b0000000-0000-0000-0000-000000000002', 'Outro Usuario', 'outro@test.com', 'outro.usuario',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'a0000000-0000-0000-0000-000000000002',
        'Rua Outro', '2', null, 'Centro', 'Cidade', '12345678', 'SP',
        CURRENT_TIMESTAMP, true);

INSERT INTO restaurante (id, nome, endereco_logradouro, endereco_numero, endereco_complemento,
                         endereco_bairro, endereco_cidade, endereco_cep, endereco_uf,
                         tipo_cozinha, horario_funcionamento, id_dono, ativo)
VALUES ('00000000-0000-0000-0000-000000000001', 'Restaurante do Admin',
        'Rua Principal', '100', null, 'Centro', 'Cidade', '12345000', 'SP',
        'Italiana', '08:00-22:00', '8c9c7e0c-84d4-4a4e-862d-0b70c3c54d3d', true);
