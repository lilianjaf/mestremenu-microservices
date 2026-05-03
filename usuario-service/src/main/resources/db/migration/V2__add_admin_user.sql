INSERT INTO tipo_usuario (id, nome, tipo_nativo)
VALUES ('799981d3-3b1f-4b68-b3d9-6938d6174154', 'dono', 'DONO');

INSERT INTO usuario (
    id, nome, email, login, senha, tipo_usuario_id,
    endereco_logradouro, endereco_numero, endereco_complemento, endereco_bairro,
    endereco_cidade, endereco_cep, endereco_uf,
    data_ultima_alteracao, ativo
)
VALUES (
    '8c9c7e0c-84d4-4a4e-862d-0b70c3c54d3d', 'Admin', 'admin@mestremenu.com.br', 'admin',
    '$2a$10$3tOrF7F0GRIK9dcTwMeSWONigMJgp28aifO4tEOcKpDnOsO3n7g7u', -- senha: admin
    '799981d3-3b1f-4b68-b3d9-6938d6174154',
    'Rua Admin', '1', 'Sala 1', 'Bairro Admin',
    'Cidade Admin', '00000000', 'UF',
    NOW(), true
);
