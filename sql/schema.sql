--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: pg_trgm; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA public;


--
-- Name: EXTENSION pg_trgm; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pg_trgm IS 'text similarity measurement and index searching based on trigrams';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: rl_autoridade_usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rl_autoridade_usuario (
    no_autoridade character varying(50) NOT NULL,
    co_usuario numeric(19,0) NOT NULL
);


ALTER TABLE rl_autoridade_usuario OWNER TO postgres;

--
-- Name: rl_grupo_pesquisador; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rl_grupo_pesquisador (
    co_grupo bigint NOT NULL,
    co_curriculum numeric(19,0) NOT NULL
);


ALTER TABLE rl_grupo_pesquisador OWNER TO postgres;

--
-- Name: sq_curriculum_lattes; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_curriculum_lattes
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_curriculum_lattes OWNER TO postgres;

--
-- Name: sq_grupo; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_grupo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_grupo OWNER TO postgres;

--
-- Name: sq_pesquisador; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_pesquisador
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_pesquisador OWNER TO postgres;

--
-- Name: sq_qualis_evento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_qualis_evento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_qualis_evento OWNER TO postgres;

--
-- Name: sq_qualis_periodico; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_qualis_periodico
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_qualis_periodico OWNER TO postgres;

--
-- Name: sq_qualis_periodico_antigo; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_qualis_periodico_antigo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_qualis_periodico_antigo OWNER TO postgres;

--
-- Name: sq_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sq_usuario OWNER TO postgres;

--
-- Name: tb_curriculum_lattes; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_curriculum_lattes (
    co_seq_curriculum numeric(19,0) DEFAULT nextval('sq_curriculum_lattes'::regclass) NOT NULL,
    no_completo character varying(255),
    no_especialidade character varying(255),
    no_sub_area_conhecimento character varying(255),
    no_area_conhecimento character varying(255),
    no_grande_area_conhecimento character varying(255),
    data_atualizacao_curriculo timestamp without time zone NOT NULL,
    data_atualizacao_usuario timestamp without time zone,
    id_lattes character varying(255) NOT NULL,
    lattes_xml xml NOT NULL
);


ALTER TABLE tb_curriculum_lattes OWNER TO postgres;

--
-- Name: tb_grupo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_grupo (
    co_seq_grupo bigint DEFAULT nextval('sq_grupo'::regclass) NOT NULL,
    no_area character varying(255),
    no_grupo character varying(255),
    no_instituicao character varying(255),
    co_usuario bigint NOT NULL
);


ALTER TABLE tb_grupo OWNER TO postgres;

--
-- Name: tb_pesquisador; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_pesquisador (
    co_seq_pesquisador bigint DEFAULT nextval('sq_pesquisador'::regclass) NOT NULL,
    ds_area_atuacao character varying(255),
    xml bytea,
    dt_atualizacao_curriculo timestamp without time zone,
    dt_atualizacao_usuario timestamp without time zone,
    id_curriculo bigint,
    nome_pesquisador character varying(255),
    co_grupo bigint
);


ALTER TABLE tb_pesquisador OWNER TO postgres;

--
-- Name: tb_qualis_evento; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_qualis_evento (
    co_seq_qualis_cco numeric(19,0) DEFAULT nextval('sq_qualis_evento'::regclass) NOT NULL,
    ds_sigla character varying(20),
    no_titulo character varying(255),
    nu_indice_h numeric(3,0),
    no_estrato character varying(2),
    no_area_avaliacao character varying(50),
    nu_ano integer
);


ALTER TABLE tb_qualis_evento OWNER TO postgres;

--
-- Name: tb_qualis_periodico; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_qualis_periodico (
    co_seq_periodico numeric(19,0) DEFAULT nextval('sq_qualis_periodico'::regclass) NOT NULL,
    co_issn character varying(9),
    no_titulo character varying(255),
    no_estrato character varying(2),
    no_area_avaliacao character varying(50),
    nu_ano integer
);


ALTER TABLE tb_qualis_periodico OWNER TO postgres;

--
-- Name: tb_usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_usuario (
    no_usuario character varying(255),
    ds_senha character varying(255),
    co_seq_usuario numeric(19,0) DEFAULT nextval('sq_usuario'::regclass) NOT NULL,
    ds_email character varying(255),
    reset_key character varying(20),
    co_curriculum numeric(19,0),
    register_key character varying(20),
    st_ativo boolean DEFAULT false
);


ALTER TABLE tb_usuario OWNER TO postgres;

--
-- Name: pk_dado_geral; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tb_curriculum_lattes
    ADD CONSTRAINT pk_dado_geral PRIMARY KEY (co_seq_curriculum);


--
-- Name: pk_grupo_pesquisador; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rl_grupo_pesquisador
    ADD CONSTRAINT pk_grupo_pesquisador PRIMARY KEY (co_grupo, co_curriculum);


--
-- Name: pk_qualis_cco; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tb_qualis_evento
    ADD CONSTRAINT pk_qualis_cco PRIMARY KEY (co_seq_qualis_cco);


--
-- Name: pk_usuario; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tb_usuario
    ADD CONSTRAINT pk_usuario PRIMARY KEY (co_seq_usuario);


--
-- Name: rl_autoridade_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rl_autoridade_usuario
    ADD CONSTRAINT rl_autoridade_usuario_pkey PRIMARY KEY (no_autoridade, co_usuario);


--
-- Name: tb_grupo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tb_grupo
    ADD CONSTRAINT tb_grupo_pkey PRIMARY KEY (co_seq_grupo);


--
-- Name: tb_pesquisador_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tb_pesquisador
    ADD CONSTRAINT tb_pesquisador_pkey PRIMARY KEY (co_seq_pesquisador);


--
-- Name: tb_qualis_periodico_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tb_qualis_periodico
    ADD CONSTRAINT tb_qualis_periodico_pkey PRIMARY KEY (co_seq_periodico);


--
-- Name: in_curriculum_lattes; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX in_curriculum_lattes ON tb_curriculum_lattes USING btree (id_lattes, data_atualizacao_curriculo);


--
-- Name: in_email_usuario; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX in_email_usuario ON tb_usuario USING btree (ds_email);


--
-- Name: in_trgm_qualis_avaliacao_avaliacao; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX in_trgm_qualis_avaliacao_avaliacao ON tb_qualis_evento USING gin (no_area_avaliacao gin_trgm_ops);


--
-- Name: in_trn_qualis_cco_titulo; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX in_trn_qualis_cco_titulo ON tb_qualis_evento USING gin (no_titulo gin_trgm_ops);


--
-- Name: tb_qualis_periodico_co_issn_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tb_qualis_periodico_co_issn_idx ON tb_qualis_periodico USING btree (co_issn);


--
-- Name: tb_qualis_periodico_co_issn_no_area_avaliacao_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tb_qualis_periodico_co_issn_no_area_avaliacao_idx ON tb_qualis_periodico USING btree (co_issn, no_area_avaliacao);


--
-- Name: tb_qualis_periodico_no_area_avaliacao_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tb_qualis_periodico_no_area_avaliacao_idx ON tb_qualis_periodico USING btree (no_area_avaliacao);


--
-- Name: fk_curriculum_co_curriculum_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_usuario
    ADD CONSTRAINT fk_curriculum_co_curriculum_fkey FOREIGN KEY (co_curriculum) REFERENCES tb_curriculum_lattes(co_seq_curriculum);


--
-- Name: fk_rl_grupo_pesquisador_curriculum_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rl_grupo_pesquisador
    ADD CONSTRAINT fk_rl_grupo_pesquisador_curriculum_fkey FOREIGN KEY (co_curriculum) REFERENCES tb_curriculum_lattes(co_seq_curriculum);


--
-- Name: fk_rl_grupo_pesquisador_grupo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rl_grupo_pesquisador
    ADD CONSTRAINT fk_rl_grupo_pesquisador_grupo_fkey FOREIGN KEY (co_grupo) REFERENCES tb_grupo(co_seq_grupo);


--
-- Name: tb_grupo_co_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_grupo
    ADD CONSTRAINT tb_grupo_co_usuario_fkey FOREIGN KEY (co_usuario) REFERENCES tb_usuario(co_seq_usuario);


--
-- Name: tb_pesquisador_co_grupo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tb_pesquisador
    ADD CONSTRAINT tb_pesquisador_co_grupo_fkey FOREIGN KEY (co_grupo) REFERENCES tb_grupo(co_seq_grupo);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

