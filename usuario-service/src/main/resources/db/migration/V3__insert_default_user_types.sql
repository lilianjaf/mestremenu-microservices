INSERT INTO tipo_usuario (id, nome, tipo_nativo)
SELECT 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'cliente', 'CLIENTE'
WHERE NOT EXISTS (SELECT 1 FROM tipo_usuario WHERE nome = 'cliente');
