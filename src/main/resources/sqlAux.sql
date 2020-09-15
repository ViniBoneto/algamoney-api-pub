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