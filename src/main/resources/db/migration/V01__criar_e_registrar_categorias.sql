CREATE TABLE categoria (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/* ENGINE=InnoDB garante a concistencia do banco. Por ex: Ao se tentar excluir uma categoria com lancamentos, nao eh permitido. */

/* Para não deixar o banco vazio, inserimos algumas categorias de partida: */
INSERT INTO categoria(nome) VALUES ('Lazer');
INSERT INTO categoria(nome) VALUES ('Alimentação');
INSERT INTO categoria(nome) VALUES ('Supermercado');
INSERT INTO categoria(nome) VALUES ('Farmácia');
INSERT INTO categoria(nome) VALUES ('Outros');
