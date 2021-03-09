SHOW DATABASES;
SHOW variables /*LIKE '%date%'*/;

USE algamoneyapi;

SHOW TABLES;

SELECT * FROM flyway_schema_history;

SELECT * FROM categoria;

SELECT @@autocommit;
SELECT @@error_count;
SELECT @@GLOBAL.log_error_services;

/*START TRANSACTION;*/
DELETE FROM algamoneyapi.categoria;
/*COMMIT;*/

INSERT INTO categoria(nome) VALUES ('Lazer');
INSERT INTO categoria(nome) VALUES ('Alimentação');
INSERT INTO categoria(nome) VALUES ('Supermercado');
INSERT INTO categoria(nome) VALUES ('Farmácia');
INSERT INTO categoria(nome) VALUES ('Outros');

SELECT * FROM pessoa;

DROP TABLE lancamento; /* Necessário p/ corrigir merd@ (execução ñ intencional script V03) */

INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('Major Tom', true, 'E. Street SW', '300', 'Suite 5R30', null, '358-0001', 'Washington', 'DC');
INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES ('Patinhas McPato', true, 'Elm Street', '1000', null, null, '123-4567', 'Patópolis', null);

SELECT * FROM lancamento
/* WHERE data_vencimento >= '2017-06-10' */;

SELECT codigo, descricao, data_vencimento FROM lancamento ORDER BY data_vencimento DESC;

SELECT count(*) as 'total lançamentos' FROM lancamento
WHERE data_vencimento >= '2017-06-10';

/* Consertando o erro da migração V04__criar_e_registrar_usuarios_e_permissoes
DELETE FROM flyway_schema_history WHERE installed_rank = 4; 
DROP TABLE usuario_permissao;
DROP TABLE usuario;
DROP TABLE permissao; 
*/

SELECT * FROM usuario;

SELECT * FROM permissao;

SELECT * FROM usuario_permissao;

### Criando insercoes em pessoas p/ amb prod bater c/ amb dev ###
SHOW COLUMNS FROM pessoa;

SET @SQLins= "INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado) VALUES (";
#SELECT @SQLins + CAST(codigo AS CHAR(10)) + ", '" + nome + "', " + CAST(ativo AS CHAR(1)) + ", '" + logradouro + "', '" + numero + "', '" + complemento + "', '" + bairro + "', '" + cep + "', '" + cidade + "', '" + estado  + "')" SQlIns FROM pessoa;

SELECT CONCAT(@SQLins, "'", nome, "', ", CAST(ativo AS CHAR(1)), ", '", IFNULL(logradouro, "NULL"), "', '", IFNULL(numero, "NULL"), "', '", IFNULL(complemento, "NULL"), "', '", IFNULL(bairro, "NULL"), "', '", IFNULL(cep, "NULL"), "', '", IFNULL(cidade, "NULL"), "', '", IFNULL(estado, "NULL"), "');") SQlIns FROM pessoa;

