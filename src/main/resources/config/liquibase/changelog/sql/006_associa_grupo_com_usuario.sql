-- 
-- No SILQ antigo, os Grupos eram associados no banco com DadoGeral. Assim, ao alterar o DadoGeral os grupos eram perdidos.
-- Esta migração muda a associação tb_grupo -> tb_dado_geral para tb_grupo -> tb_usuario, preservando os grupos previamente criados.
--

UPDATE tb_grupo g
	SET co_usuario = d.co_usuario
	FROM tb_dado_geral d
	WHERE g.co_usuario = d.co_seq_dado_geral;

ALTER TABLE tb_grupo ADD FOREIGN KEY (co_usuario) REFERENCES tb_usuario (co_seq_usuario);

ALTER TABLE tb_grupo ALTER COLUMN co_usuario SET NOT NULL;