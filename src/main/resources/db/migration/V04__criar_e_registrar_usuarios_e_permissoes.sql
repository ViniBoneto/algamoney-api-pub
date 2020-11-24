/* Definições tabelas */

CREATE TABLE usuario (
/* Neste caso ñ há "auto increment", na primary key, pois não serão registrados novos usuários pela aplicação (ñ se precisa de coluna identidade auto incrementável). */
	codigo BIGINT PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(150) NOT NULL	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permissao (
/* Neste caso ñ há "auto increment", na primary key, pois não serão registradas novas permissões pela aplicação (ñ se precisa de coluna identidade auto incrementável). */
	codigo BIGINT PRIMARY KEY,
	descricao VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usuario_permissao (
	codigo_usuario BIGINT NOT NULL,
	codigo_permissao BIGINT NOT NULL,
	PRIMARY KEY(codigo_usuario, codigo_permissao),
	FOREIGN KEY(codigo_usuario) REFERENCES usuario(codigo),	
	FOREIGN KEY(codigo_permissao) REFERENCES permissao(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* Inserções registros */

/* Usuários. Senhas codificadas com algoritmo BCrypt */
INSERT INTO usuario(codigo, nome, email, senha) VALUES (1, 'Adminsitrador', 'admin@algamoney.com', '$2a$10$oeXGGnVLVxKznWfvH9LoNuLG88YIQziZej4nRtURlr.QZ2LDoMHZW' /* senha admin */);
INSERT INTO usuario(codigo, nome, email, senha)  VALUES (2, 'Vinicius Neto', 'vineto@algamoney.com', '$2a$10$cXeQWDZ8bSzElenhAAC4PeHS9uFXdSetdJfIcSBcBfisVQBSCui2O' /* senha vineto */);

/* Permissões */
INSERT INTO permissao(codigo, descricao) VALUES(1, 'ROLE_CADASTRAR_CATEGORIA');
INSERT INTO permissao(codigo, descricao) VALUES(2, 'ROLE_PESQUISAR_CATEGORIA');

INSERT INTO permissao(codigo, descricao) VALUES(3, 'ROLE_CADASTRAR_PESSOA');
INSERT INTO permissao(codigo, descricao) VALUES(4, 'ROLE_REMOVER_PESSOA');
INSERT INTO permissao(codigo, descricao) VALUES(5, 'ROLE_PESQUISAR_PESSOA');

INSERT INTO permissao(codigo, descricao) VALUES(6, 'ROLE_CADASTRAR_LANCAMENTO');
INSERT INTO permissao(codigo, descricao) VALUES(7, 'ROLE_REMOVER_LANCAMENTO');
INSERT INTO permissao(codigo, descricao) VALUES(8, 'ROLE_PESQUISAR_LANCAMENTO');

/* Permissões x usuários */

-- admin (todas)
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(1, 1);
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(1, 2);
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(1, 3);
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(1, 4);
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(1, 5);
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(1, 6);
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(1, 7);
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(1, 8);

-- Vinicius Neto (só pesquisas)
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(2, 2);
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(2, 5);
INSERT INTO usuario_permissao(codigo_usuario, codigo_permissao) VALUES(2, 8);
