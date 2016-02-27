--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2014-10-08 21:58:56

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 187 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2065 (class 0 OID 0)
-- Dependencies: 187
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 188 (class 3079 OID 32803)
-- Name: pg_trgm; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA public;


--
-- TOC entry 2066 (class 0 OID 0)
-- Dependencies: 188
-- Name: EXTENSION pg_trgm; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pg_trgm IS 'text similarity measurement and index searching based on trigrams';


SET search_path = public, pg_catalog;

--
-- TOC entry 178 (class 1259 OID 32988)
-- Name: sq_evento_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_evento_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_evento_usuario OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 186 (class 1259 OID 33079)
-- Name: rl_evento_usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rl_evento_usuario (
    co_seq_evento_usuario numeric(19,0) DEFAULT nextval('sq_evento_usuario'::regclass) NOT NULL,
    co_evento numeric(19,0) NOT NULL,
    co_usuario numeric(19,0) NOT NULL
);


ALTER TABLE public.rl_evento_usuario OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 32990)
-- Name: sq_periodico_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_periodico_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_periodico_usuario OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 33010)
-- Name: rl_periodico_usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rl_periodico_usuario (
    co_seq_periodico_usuario numeric(19,0) DEFAULT nextval('sq_periodico_usuario'::regclass) NOT NULL,
    co_periodico numeric(19,0) NOT NULL,
    co_usuario numeric(19,0) NOT NULL
);


ALTER TABLE public.rl_periodico_usuario OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 33053)
-- Name: sq_dado_geral; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_dado_geral
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_dado_geral OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 32899)
-- Name: sq_evento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_evento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_evento OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 32897)
-- Name: sq_periodico; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_periodico
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_periodico OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 16402)
-- Name: sq_qualis_cco; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_qualis_cco
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_qualis_cco OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 16394)
-- Name: sq_qualis_geral; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_qualis_geral
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_qualis_geral OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 32877)
-- Name: sq_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sq_usuario OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 33055)
-- Name: tb_dado_geral; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_dado_geral (
    co_seq_dado_geral numeric(19,0) DEFAULT nextval('sq_dado_geral'::regclass) NOT NULL,
    no_completo character varying(255),
    no_especialidade character varying(255),
    no_sub_area_conhecimento character varying(255),
    no_area_conhecimento character varying(255),
    no_grande_area_conhecimento character varying(255),
    co_usuario numeric(19,0)
);


ALTER TABLE public.tb_dado_geral OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 33070)
-- Name: tb_evento; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_evento (
    co_seq_evento numeric(19,0) DEFAULT nextval('sq_evento'::regclass) NOT NULL,
    nu_ano numeric(4,0),
    no_trabalho character varying(255),
    no_evento character varying(255)
);


ALTER TABLE public.tb_evento OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 33001)
-- Name: tb_periodico; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_periodico (
    co_seq_periodico numeric(19,0) DEFAULT nextval('sq_periodico'::regclass) NOT NULL,
    nu_ano numeric(4,0),
    no_artigo character varying(255),
    no_periodico character varying(255),
    co_issn character varying(8),
    no_area_conhecimento_um character varying(255),
    no_grande_area_conhecimento_um character varying(255),
    no_area_conhecimento_dois character varying(255),
    no_grande_area_conhecimento_dois character varying(255),
    no_area_conhecimento_tres character varying(255),
    no_grande_area_conhecimento_tres character varying(255)
);


ALTER TABLE public.tb_periodico OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 16404)
-- Name: tb_qualis_cco; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_qualis_cco (
    co_seq_qualis_cco numeric(19,0) DEFAULT nextval('sq_qualis_cco'::regclass) NOT NULL,
    ds_sigla character varying(20),
    no_titulo character varying(255),
    nu_indice_h numeric(3,0),
    no_estrato character varying(2)
);


ALTER TABLE public.tb_qualis_cco OWNER TO postgres;

--
-- TOC entry 171 (class 1259 OID 16396)
-- Name: tb_qualis_geral; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_qualis_geral (
    co_seq_qualis_geral numeric(19,0) DEFAULT nextval('sq_qualis_geral'::regclass) NOT NULL,
    co_issn character varying(9),
    no_titulo character varying(255),
    no_estrato character varying(2),
    no_area_avaliacao character varying(50),
    st_qualis character varying(15)
);


ALTER TABLE public.tb_qualis_geral OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 32888)
-- Name: tb_usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tb_usuario (
    no_usuario character varying(255),
    ds_senha character varying(255),
    co_seq_usuario numeric(19,0) DEFAULT nextval('sq_usuario'::regclass) NOT NULL,
    ds_email character varying(255),
    no_sexo character varying(10),
    nu_ano numeric(4,0)
);


ALTER TABLE public.tb_usuario OWNER TO postgres;

--
-- TOC entry 2057 (class 0 OID 33079)
-- Dependencies: 186
-- Data for Name: rl_evento_usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2052 (class 0 OID 33010)
-- Dependencies: 181
-- Data for Name: rl_periodico_usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2067 (class 0 OID 0)
-- Dependencies: 183
-- Name: sq_dado_geral; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sq_dado_geral', 8, true);


--
-- TOC entry 2068 (class 0 OID 0)
-- Dependencies: 177
-- Name: sq_evento; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sq_evento', 57, true);


--
-- TOC entry 2069 (class 0 OID 0)
-- Dependencies: 178
-- Name: sq_evento_usuario; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sq_evento_usuario', 57, true);


--
-- TOC entry 2070 (class 0 OID 0)
-- Dependencies: 176
-- Name: sq_periodico; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sq_periodico', 30, true);


--
-- TOC entry 2071 (class 0 OID 0)
-- Dependencies: 179
-- Name: sq_periodico_usuario; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sq_periodico_usuario', 30, true);


--
-- TOC entry 2072 (class 0 OID 0)
-- Dependencies: 172
-- Name: sq_qualis_cco; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sq_qualis_cco', 1703, true);


--
-- TOC entry 2073 (class 0 OID 0)
-- Dependencies: 170
-- Name: sq_qualis_geral; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sq_qualis_geral', 107446, true);


--
-- TOC entry 2074 (class 0 OID 0)
-- Dependencies: 174
-- Name: sq_usuario; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sq_usuario', 1, true);