SHOW DATABASES;
SHOW variables /*LIKE '%date%'*/;

USE algamoneyapi;

SHOW TABLES;

SHOW WARNINGS; # Usar sempre este cmd p/ exibir os altertas, erros e avisos da transação
SHOW ERRORS; # Usar sempre este cmd p/ exibir apenas os erros da transação

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
 WHERE data_vencimento >= '2017-06-10' limit 0, 3;

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

### Validando filtro pessoa (aula 7.6)
SELECT * FROM pessoa WHERE /*1 = 1*/ LOWER(nome) LIKE '%alves%' LIMIT 9, 3/*20*/;

### criando lista fixa de lançamentos p/ serem exibidos em tabela da aula 10.5 ###
SHOW COLUMNS FROM pessoa;
SHOW COLUMNS FROM lancamento;

SELECT CONCAT('{ tipo: "', l.tipo, '", descricao: "', l.descricao, '", dataVencimento: "', DATE_FORMAT(l.data_vencimento, '%d/%m/%Y'), 
	'", dataPagamento: "', IFNULL( DATE_FORMAT(l.data_pagamento, '%d/%m/%Y'), 'null' ), '", valor: ', l.valor, ', pessoa: "', 
    p.nome, '" },') lancamentosObjs
FROM lancamento l inner join pessoa p on l.codigo_pessoa = p.codigo;
