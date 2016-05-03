-- Autoridades
ALTER TABLE rl_autoridade_usuario ADD PRIMARY KEY (no_autoridade, co_usuario);

-- Grupos
ALTER TABLE tb_grupo ADD PRIMARY KEY (co_seq_grupo);
ALTER TABLE tb_grupo ALTER COLUMN co_seq_grupo SET DEFAULT nextval('sq_grupo'::regclass);

-- Pesquisador
ALTER TABLE tb_pesquisador ADD PRIMARY KEY (co_seq_pesquisador);
ALTER TABLE tb_pesquisador ALTER COLUMN co_seq_pesquisador SET DEFAULT nextval('sq_pesquisador'::regclass);
ALTER TABLE tb_pesquisador ADD FOREIGN KEY (co_grupo) REFERENCES tb_grupo;
