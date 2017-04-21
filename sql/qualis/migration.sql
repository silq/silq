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
