CREATE TABLE pessoa (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	ativo BOOLEAN NOT NULL,
	logradouro  VARCHAR(50),
	numero  VARCHAR(50),
	complemento  VARCHAR(50),
	bairro  VARCHAR(50),
	cep  VARCHAR(50),
	cidade  VARCHAR(50),
	estado  VARCHAR(50)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8; 
/* ENGINE=InnoDB garante a consistência do banco. Por ex: Ao se tentar excluir uma pessoa com lancamentos, não é permitido. */

/* Para não deixar a tabela vazia, inserimos algumas pessoas de partida: */
INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('Jacinto Leite Aquino Rego', true, 'Rua Bela', '0', null, 'São Cristóvão', '20930-380', 'Rio de Janeiro', 'RJ');
INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('H. Romeu Pinto', true, 'Rua Astra ', 10, null, 'Campo Grande', '23078-420', 'Rio de Janeiro', 'RJ');
INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('Ava Gina Berta', true, 'Avenida Nossa Senhora de Copacabana', '44', '302', 'Leme', '22010-122', 'Rio de Janeiro', 'RJ');
INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('Cuca Beludo', false, 'Avenida Nossa Senhora de Copacabana', '842', '1001', 'Copacabana', '22060-002', 'Rio de Janeiro', 'RJ');
INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('Botelho Pinto', true, 'Avenida Roberto Silveira', '200', null, null, '23970-000', 'Paraty', 'RJ');
INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('Mila Ambuza', false, 'Rua Teresa', '10', '101', 'Alto da Serra', '25625-022', 'Petrópolis', 'RJ');