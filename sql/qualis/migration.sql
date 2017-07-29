-- Não é necessário executar este arquivo, trata-se apenas de um registro histórico dos comandos utilizados para importação dos registros a partir das tabelas em XLS.

\COPY tb_qualis_periodico(co_issn, no_titulo, no_area_avaliacao, no_estrato) FROM 2010_75786_registros.xls DELIMITER E'\t' CSV HEADER ENCODING 'latin1';
UPDATE tb_qualis_periodico SET nu_ano = 2010 WHERE nu_ano IS NULL;

\COPY tb_qualis_periodico(co_issn, no_titulo, no_area_avaliacao, no_estrato) FROM 2011_66171_registros.xls DELIMITER E'\t' CSV HEADER ENCODING 'latin1';
UPDATE tb_qualis_periodico SET nu_ano = 2011 WHERE nu_ano IS NULL;

\COPY tb_qualis_periodico(co_issn, no_titulo, no_area_avaliacao, no_estrato) FROM 2012_108272_registros.xls DELIMITER E'\t' CSV HEADER ENCODING 'latin1';
UPDATE tb_qualis_periodico SET nu_ano = 2012 WHERE nu_ano IS NULL;

\COPY tb_qualis_periodico(co_issn, no_titulo, no_area_avaliacao, no_estrato) FROM 2013_44437_registros.xls DELIMITER E'\t' CSV HEADER ENCODING 'latin1';
UPDATE tb_qualis_periodico SET nu_ano = 2013 WHERE nu_ano IS NULL;

\COPY tb_qualis_periodico(co_issn, no_titulo, no_area_avaliacao, no_estrato) FROM 2014_44538_registros.xls DELIMITER E'\t' CSV HEADER ENCODING 'latin1';
UPDATE tb_qualis_periodico SET nu_ano = 2014 WHERE nu_ano IS NULL;

\COPY tb_qualis_periodico(co_issn, no_titulo, no_area_avaliacao, no_estrato) FROM 2015_101865_registros.xls DELIMITER E'\t' CSV HEADER ENCODING 'latin1';
UPDATE tb_qualis_periodico SET nu_ano = 2015 WHERE nu_ano IS NULL;

-- Removendo espaços em branco das colunas:
-- Exemplo: "ADMINISTRAÇÃO ..         " -> "ADMINISTRAÇÃO .."
UPDATE tb_qualis_periodico SET no_titulo = trim(no_titulo), no_estrato = trim(no_estrato), no_area_avaliacao = trim(no_area_avaliacao);

--------------------------------------------------------------------------------
-- Qualis Conferência

-- 2013-2015

\COPY tb_qualis_evento(ds_sigla, no_titulo, nu_indice_h, no_estrato) FROM QualisConferencias2013-2015.csv DELIMITER E'\t' CSV HEADER ENCODING 'utf8';
UPDATE tb_qualis_evento SET nu_ano = 2015 WHERE nu_ano IS NULL;
UPDATE tb_qualis_evento SET no_area_avaliacao = 'CIÊNCIA DA COMPUTAÇÃO' WHERE no_area_avaliacao IS NULL;

-- Cópia dos registros para anos 2013 e 2014:
INSERT INTO tb_qualis_evento
SELECT nextval('sq_qualis_evento'), ds_sigla, no_titulo, nu_indice_h, no_estrato, no_area_avaliacao, 2013
FROM tb_qualis_evento
WHERE nu_ano = 2015;

INSERT INTO tb_qualis_evento
SELECT nextval('sq_qualis_evento'), ds_sigla, no_titulo, nu_indice_h, no_estrato, no_area_avaliacao, 2014
FROM tb_qualis_evento
WHERE nu_ano = 2015;

-- 2016
\COPY tb_qualis_evento(ds_sigla, no_titulo, no_estrato) FROM QualisConferencias2016.csv DELIMITER E'\t' CSV HEADER ENCODING 'utf8';
UPDATE tb_qualis_evento SET nu_ano = 2016 WHERE nu_ano IS NULL;
UPDATE tb_qualis_evento SET no_area_avaliacao = 'CIÊNCIA DA COMPUTAÇÃO' WHERE no_area_avaliacao IS NULL;
