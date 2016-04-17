-- 
-- Adiciona a coluna 'ano' a 'tb_qualis_evento' e 'tb_qualis_periodico', adicionando o valor '2012' aos registros atuais
--

ALTER TABLE tb_qualis_evento ADD COLUMN nu_ano INTEGER;

ALTER TABLE tb_qualis_periodico ADD COLUMN nu_ano INTEGER;

UPDATE tb_qualis_evento SET nu_ano = 2012;

UPDATE tb_qualis_periodico SET nu_ano = 2012;
