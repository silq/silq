--
-- Alterações referentes ao refactoring do Banco de Dados original para suportar nova estrutura e inclusão dos Qualis 2013, 2014, 2015.
-- 		* Remoção das tabelas não utilizadas: rl_evento_usuario, rl_periodico_usuario, tb_periodico, tb_evento e de suas respectivas sequences
--    * Alteração do nome das tabelas Qualis: tb_qualis_geral -> tb_qualis_periodico; tb_qualis_cco -> tb_qualis_evento e de suas sequences
--		* Inclusão da coluna `no_area_avaliacao` em tb_qualis_evento (antiga tb_qualis_cco) incluindo valor `CIÊNCIA DA COMPUTAÇÃO`
--      (já que a antiga `tb_qualis_cco` só continha eventos de computação). 
--

-- Remoção tabelas inúteis
DROP TABLE IF EXISTS rl_evento_usuario;
DROP SEQUENCE sq_evento_usuario;

DROP TABLE IF EXISTS rl_periodico_usuario;
DROP SEQUENCE sq_periodico_usuario;

DROP TABLE IF EXISTS tb_periodico;
DROP SEQUENCE sq_periodico;

DROP TABLE IF EXISTS tb_evento;
DROP SEQUENCE sq_evento;

-- Alteração nomenclatura
ALTER TABLE tb_qualis_geral RENAME TO tb_qualis_periodico;
ALTER SEQUENCE sq_qualis_geral RENAME TO sq_qualis_periodico;

ALTER TABLE tb_qualis_cco RENAME TO tb_qualis_evento;
ALTER SEQUENCE sq_qualis_cco RENAME TO sq_qualis_evento;

-- Adição coluna área de avaliação em tb_qualis_evento
ALTER TABLE tb_qualis_evento ADD COLUMN no_area_avaliacao character varying(50);
UPDATE tb_qualis_evento SET no_area_avaliacao = 'CIÊNCIA DA COMPUTAÇÃO';

-- Remove a coluna 'nu_ano' de tb_usuario
ALTER TABLE tb_usuario DROP COLUMN nu_ano;

