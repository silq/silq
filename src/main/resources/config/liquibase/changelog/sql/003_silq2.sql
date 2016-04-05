ALTER TABLE tb_usuario ADD COLUMN reset_key VARCHAR(20);

CREATE TABLE rl_autoridade_usuario (
    no_autoridade varchar(50) NOT NULL,
    co_usuario numeric(19,0) NOT NULL
);

-- Já existente:
ALTER TABLE tb_dado_geral ADD COLUMN xml BYTEA;
ALTER TABLE tb_dado_geral ADD COLUMN data_atualizacao_curriculo TIMESTAMP;
ALTER TABLE tb_dado_geral ADD COLUMN data_atualizacao_usuario TIMESTAMP;
ALTER TABLE tb_dado_geral ADD COLUMN id_curriculo VARCHAR(255);

CREATE TABLE tb_pesquisador (
    co_seq_pesquisador bigint NOT NULL,
    ds_area_atuacao character varying(255),
    xml bytea,
    dt_atualizacao_curriculo timestamp without time zone,
    dt_atualizacao_usuario timestamp without time zone,
    id_curriculo bigint,
    nome_pesquisador character varying(255),
    co_grupo bigint
);

CREATE SEQUENCE sq_pesquisador
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE tb_grupo (
    co_seq_grupo bigint NOT NULL,
    no_area character varying(255),
    no_grupo character varying(255),
    no_instituicao character varying(255),
    co_usuario bigint
);

CREATE SEQUENCE sq_grupo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;