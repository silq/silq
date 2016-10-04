--
-- Arquivo utilizado para os testes de avaliação do algoritmo conforme
-- descritos na monografia do Carlos Bonetti
-- Para os testes foram considerados apenas os feedbacks com co_usuario = 20
-- Os feedbacks de teste são marcados com st_validation = true, e os de treino
-- com st_validation = false.
--

--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

--
-- Data for Name: tb_feedback; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY tb_feedback (co_seq_feedback, co_tipo, ds_query, co_evento, co_periodico, co_usuario, dt_feedback, nu_ano, st_validation) FROM stdin;
765	E	4th International Information and Telecommunication Technologies Symposium	670	\N	20	2016-10-04 11:07:56.556	2005	f
768	E	The 18th International Symposium on High Performance Computing Systems and Applications	659	\N	20	2016-10-04 11:08:19.054	2004	f
769	E	Escola Regional de Redes de Computadores	\N	\N	20	2016-10-04 11:08:24.966	2004	f
771	E	XII ESCOLA REGIONAL DE INFORMATICA SBC - PARANA	\N	\N	20	2016-10-04 11:08:34.23	2004	f
84	E	CBMS´2000 - 13th IEEE Conference on Computer-Based Medical Systems	201	\N	20	2016-09-19 22:17:21.006	2000	f
88	E	MRM 2000 - Second International Congress on MR-Mammography	\N	\N	20	2016-09-19 22:17:59.54	2000	f
80	E	Congresso Brasileiro de Radiologia 2001	\N	\N	20	2016-09-19 22:16:22.116	2001	f
90	E	XXIX Congresso Brasileiro de Radiologia	\N	\N	20	2016-09-19 22:18:20.655	2000	f
105	E	4th German-Brazilian Workshop on Information Technology	\N	\N	20	2016-09-19 22:20:12.579	1997	f
135	E	19th IEEE International Conference on Electronics, Circuits, and Systems	756	\N	20	2016-09-19 23:40:54.824	2012	f
127	E	First Workshop on Cyber-Physical System Architectures and Design Methodologies	\N	\N	20	2016-09-19 23:36:32.827	2014	f
198	E	Simpósio Brasileiro sobre Fatores Humanos em Sistemas Computacionais	896	\N	20	2016-09-20 09:18:38.803	2013	f
202	E	Simpósio de Informática do Planalto Médio (SIPM)	\N	\N	20	2016-09-20 09:20:38.228	2008	f
215	E	XVI Mostra de Iniciação Científica	\N	\N	20	2016-09-20 09:23:40.268	2006	f
235	E	2014 6th International Conference on New Technologies, Mobility and Security (NTMS)	\N	\N	20	2016-09-20 10:34:17.907	2014	f
239	E	ICWMC - The Ninth International Conference on Wireless and Mobile Communications	868	\N	20	2016-09-20 10:35:03.221	2013	f
256	E	WSeg 2003 - III Workshop em Segurança de Sistemas Computacionais	\N	\N	20	2016-09-20 10:40:36.285	2003	f
305	E	33rd Annual IEEE Software Engineering Workshop 2009 (SEW-33)	1428	\N	20	2016-09-20 11:04:22.221	2009	f
308	E	SIBIGRAPI 2007 - The Brazilian Symposium on Computer Graphics and Image Processing	\N	\N	20	2016-09-20 11:05:07.094	2007	f
309	E	III Simpósio de Instrumentação e Imagens Médicas	\N	\N	20	2016-09-20 11:05:21.215	2007	f
315	E	ACM International Workshop on Mobility Management and Wireless Access (MOBIWAC) 04	1151	\N	20	2016-09-20 11:05:56.987	2004	f
320	E	ISCIS'99 - XIV. International Symposium on Computer and Information Sciences	959	\N	20	2016-09-20 11:06:36.539	1999	f
357	E	IEEE International Conference on Communications	709	\N	20	2016-09-21 10:34:58.747	2006	f
369	E	The Eighth International Conference on Networks - ICN 2009	813	\N	20	2016-09-21 10:37:37.019	2009	f
353	E	SBRC - WGRS - XII Workshop de Gerência e Operação de Redes e Serviços	1644	\N	20	2016-09-21 10:34:45.94	2007	f
378	E	Congresso da SBC-2005 - SEMISH-2005	1420	\N	20	2016-09-21 10:40:36.174	2005	f
402	E	SPICE Conference	\N	\N	20	2016-09-21 10:53:33.911	2012	f
410	E	13th IFIP WG 5.5 Working Conference on Virtual Enterprises, PRO-VE 2012	1291	\N	20	2016-09-21 10:55:09.535	2012	f
434	E	XII Conferencia de la Asociación Española para la Inteligencia Artificial (CAEPIA)	\N	\N	20	2016-09-21 11:11:02.429	2007	f
438	E	2016 IEEE International Conference on Services Computing (SCC)	1388	\N	20	2016-09-21 11:17:56.182	2016	f
443	E	International Conference on Semantic Web and Web Services	1010	\N	20	2016-09-21 11:20:08.239	2012	f
455	E	Simpósio Brasileiro de Sistemas Multimídia e Web	1632	\N	20	2016-09-21 11:20:56.569	2010	f
452	E	Symposium on Applied Computing	1340	\N	20	2016-09-21 11:20:59.016	2009	f
457	E	IEEE International Conference on Advanced Information Networking and Applications	63	\N	20	2016-09-21 11:21:03.215	2008	f
472	E	XIV impósio Brasileiro em Segurança da Informação e de Sistemas Computacionais	1382	\N	20	2016-09-21 11:24:39.129	2014	f
484	E	VIII Simpósio Brasileiro em Segurança da Informação e de Sistemes Computacionais	1382	\N	20	2016-09-21 11:29:06.925	2008	f
500	E	2013 26th Symposium on Integrated Circuits and Systems Design (SBCCI)	1364	\N	20	2016-09-24 18:33:16.249	2013	f
537	E	2014 26th International Symposium on Computer Architecture and High Performance Computing (SBACPAD)	1361	\N	20	2016-09-27 10:20:22.936	2014	f
541	E	2013 14th European Conference on Radiation and Its Effects on Components and Systems (RADECS)	1306	\N	20	2016-09-27 10:20:52.954	2013	f
542	E	2012 IEEE 18th International Conference on Parallel and Distributed Systems (ICPADS)	828	\N	20	2016-09-27 10:20:56.527	2012	f
550	E	IEEE Computer Society Annual Symposium on VLSI	1006	\N	20	2016-09-27 10:25:41.559	2016	f
749	E	The Fifth International Symposium on Parallel and Distributed Processing and Applications (ISPA07)	989	\N	20	2016-10-04 11:04:53.058	2007	f
750	E	The 8th IEEE/ACM International Conference on Grid Computing (Grid 2007)	624	\N	20	2016-10-04 11:04:58.555	2007	f
751	E	5 Conged- Congresso de Tecnologias para a Gestão de Dados e Metadados do Cone Sul	\N	\N	20	2016-10-04 11:05:09.288	2007	f
754	E	III Congresso Sul Catarinense de Computação	\N	\N	20	2016-10-04 11:06:05.105	2007	f
761	E	International Symposium on High Performance Computing Systems an Apllications	659	\N	20	2016-10-04 11:07:18.645	2005	f
762	E	XXXII Seminario Integrado de Software e Hardware	1420	\N	20	2016-10-04 11:07:28.183	2005	f
770	E	2a. Escola Regional de Redes de Computadores	\N	\N	20	2016-10-04 11:08:30.359	2004	f
290	E	Design, Automation and Test in Europe	354	\N	20	2016-09-20 10:53:04.582	2004	f
6	E	GEOINFO	605	\N	6	2016-09-06 15:09:47.3	2012	f
510	E	28th IEEE International Conference on Advanced Information Networking and Applications	63	\N	20	2016-09-24 18:48:29.645	2014	f
536	E	2014 IEEE International Symposium on Defect and Fault Tolerance in VLSI and Nanotechnology Systems (DFT)	374	\N	20	2016-09-27 10:20:19.775	2014	f
103	E	SEKE98 - 10th INternational Conference on Software Engineering and Knowledge Engineering, 1998	1415	\N	20	2016-09-19 22:19:40.798	1998	f
113	E	ISIKNH´94 - International Symposium on Integrating Knowledge and Neural Heuristics, Pensacola, Florida	\N	\N	20	2016-09-19 22:22:02.969	1994	f
120	E	2015 International Symposium on Rapid System Prototyping (RSP)	1327	\N	20	2016-09-19 23:35:23.562	2015	f
122	E	2015 Euromicro Conference on Digital System Design (DSD)	399	\N	20	2016-09-19 23:35:34.254	2015	f
143	E	Brazilian Symposium on Multimedia and the Web	1632	\N	20	2016-09-19 23:41:43.553	2011	f
207	E	Brazilian Symposium on Multimedia and the Web (WebMedia - Qualis Nacional A)	1632	\N	20	2016-09-20 09:21:28.227	2007	f
124	E	2014 Brazilian Symposium on Computing Systems Engineering (SBESC)	1368	\N	20	2016-09-19 23:44:59.848	2014	f
157	E	International Embedded System Symposium	885	\N	20	2016-09-20 00:05:48.378	2009	f
158	E	Evaluation of PHY Reconfiguration Latency in SDR Gateway for WSN	\N	\N	20	2016-09-20 00:09:50.356	2009	f
171	E	XXXIII Latin American Conference on Informatics	\N	\N	20	2016-09-20 00:15:26.266	2007	f
172	E	4th International IEEE Conference on Industrial Informatics	919	\N	20	2016-09-20 00:15:36.657	2006	f
174	E	11th IEEE International Conference on Emerging Technologies and Factory Automation	508	\N	20	2016-09-20 00:15:43.262	2006	f
185	E	Dagstuhl Seminar 238 - High Level Parallel Programming: Applicability, Analysis and Performance	\N	\N	20	2016-09-20 00:18:00.659	1999	f
191	E	Simpósio Brasileiro de Sistemas Multimídia e Web (WebMedia)	1632	\N	20	2016-09-20 09:14:12.756	2016	f
192	E	XII Workshop de Trabalhos de Iniciação Científica (WTIC)	\N	\N	20	2016-09-20 09:14:34.067	2015	f
195	E	Escola Regional de Banco de Dados	\N	\N	20	2016-09-20 09:20:22.302	2009	f
263	E	SBC - Kit Recém Doutor	\N	\N	20	2016-09-20 10:41:54.172	2001	f
264	E	SBRC - Simpósio Brasileiro de Redes de Computadores	1377	\N	20	2016-09-20 10:41:59.069	2000	f
270	E	VII Semana da Pesquisa da UFSC	\N	\N	20	2016-09-20 10:43:11.606	1999	f
278	E	2011 Design, Automation &amp; Test in Europe	354	\N	20	2016-09-20 10:50:04.509	2011	f
286	E	EDAA/DATE PhD Forum at Design, Automotive and Test in Europe (DATE08)	354	\N	20	2016-09-20 10:51:20.224	2008	f
287	E	IP 08 (IP-Based System Design)	\N	\N	20	2016-09-20 10:51:31.182	2008	f
295	E	11th IEEE Asian Test Symposium	\N	\N	20	2016-09-20 10:53:55.399	2002	f
363	E	ICN 2015 - The Fourteenth International Conference on Networks	813	\N	20	2016-09-21 10:36:15.545	2015	f
422	E	I2TS - 8th International Information and Telecommunication Technologies Symposium	670	\N	20	2016-09-21 11:07:24.534	2009	f
427	E	WESAAC 2014Workshop-Escola de Sistemas de Agentes, seus Ambientes e aplicações	1640	\N	20	2016-09-21 11:09:56.716	2014	f
429	E	Fifth Latin American Conference on Learning Objects	1063	\N	20	2016-09-21 11:10:21.273	2010	f
449	E	International Conference on Networks	822	\N	20	2016-09-21 11:20:13.478	2011	f
450	E	European Interactive TV Conference	526	\N	20	2016-09-21 11:20:15.46	2011	f
481	E	X Simpósio Brasileiro em Segurança da Informação e de Sistemes Computacionais (SBSEG 2010)	1382	\N	20	2016-09-21 11:26:24.185	2010	f
487	E	2016 IEEE 7th Latin American Symposium on Circuits & Systems (LASCAS)	1070	\N	20	2016-09-24 18:32:10.068	2016	f
489	E	2016 IEEE Computer Society Annual Symposium on VLSI (ISVLSI)	1006	\N	20	2016-09-24 18:32:18.821	2016	f
490	E	2016 IEEE INTERNATIONAL CONFERENCE ON IMAGE PROCESSING	785	\N	20	2016-09-24 18:32:22.107	2016	f
493	E	2015 IEEE/ACM International Conference on ComputerAided Design (ICCAD)	711	\N	20	2016-09-24 18:32:38.054	2015	f
514	E	2014 Brazilian Symposium on Computer Networks and Distributed Systems (SBRC)	1377	\N	20	2016-09-24 18:49:01.152	2014	f
519	E	XIV SBC Workshop de Teste e Tolerância a Falhas	1694	\N	20	2016-09-24 18:49:31.374	2013	f
531	E	2015 23rd Euromicro International Conference on Parallel, Distributed and NetworkBased Processing (PDP)	1259	\N	20	2016-09-27 10:18:45.279	2015	f
540	E	the 3rd Workshop	\N	\N	20	2016-09-27 10:20:42.397	2013	f
543	E	International Conference on Parallel Processing	830	\N	20	2016-09-27 10:20:58.987	2012	f
581	E	Ontobras- Seminário de Pesquisa de Ontologia no Brasil	\N	\N	20	2016-09-27 10:29:47.731	2013	f
613	E	26th International Conference on Software Engineering & Knowledge Engineering (SEKE 2014)	1415	\N	20	2016-09-27 22:05:45.166	2014	f
620	E	SEKE 2013 - 25th International Conference on Software Engineering & Knowledge Engineering	1415	\N	20	2016-09-27 22:06:13.49	2013	f
530	E	Escola Regional de Alto Desempenho	488	\N	20	2016-09-27 10:20:37.796	2013	f
674	E	WESAAC - Workshop-Escola de Sistemas de Agentes, seus Ambientes e Aplicações	1640	\N	20	2016-09-27 22:12:27.07	2012	f
20	E	EURAM 2016 - 16th Conference of the European Academy of Management	\N	\N	20	2016-09-19 22:03:37.432	2016	f
24	E	Computer on the Beach 2015	\N	\N	20	2016-09-19 22:05:27.229	2015	f
25	E	SBBD 2015 - XXX Simpósio Brasileiro de Bancos de Dados	\N	\N	20	2016-09-19 22:05:56.797	2015	f
38	E	CBIS 2012 - XIII Congresso Brasileiro de Informática em Saúde	199	\N	20	2016-09-19 22:08:53.735	2012	f
40	E	Computer-Based Medical Systems (CBMS), 2011 24th International Symposium on	201	\N	20	2016-09-19 22:09:18.656	2011	f
41	E	20th IEEE International Workshops on Enabling Technologies: Infrastructure for Collaborative Enterprises (WETICE), 2011	1641	\N	20	2016-09-19 22:09:24.253	2011	f
51	E	SBAC-PAD 2009 - 21st International Symposium on Computer Architecture and High Performance Computing	1361	\N	20	2016-09-19 22:11:26.529	2009	f
60	E	CBMS 2007 - 20th IEEE International Symposium on Computer-Based Medical Systems, 2007	201	\N	20	2016-09-19 22:13:01.337	2007	f
57	E	SIBGRAPI 2008 - XXI Brazilian Symposium on Computer Graphics and Image Processing	\N	\N	20	2016-09-19 22:12:44.814	2008	f
66	E	CBIS 2006 - X Congresso Brasileiro de Informática em Saúde	199	\N	20	2016-09-19 22:14:27.228	2006	f
65	E	X Congresso Brasileiro de Informática em Saúde	199	\N	20	2016-09-19 22:14:20.89	2006	f
675	E	International Conference on Systems	824	\N	20	2016-09-27 22:12:54.217	2014	f
130	E	2013 IEEE 19th International Conference on Embedded and RealTime Computing Systems and Applications (RTCSA)	1334	\N	20	2016-09-19 23:37:03.03	2013	f
43	E	WEBMEDIA 2011 Simpósio Brasileiro de Sistemas Multimídia e Web	1632	\N	20	2016-09-19 23:42:49.828	2011	f
146	E	V Brazilian Symposium on Computing Systems Engineering	1368	\N	20	2016-09-19 23:44:27.303	2015	f
150	E	IEEE International Conference on Electronics, Circuits, and Systems,	756	\N	20	2016-09-19 23:58:38.046	2011	f
160	E	17th IFAC World Congress	\N	\N	20	2016-09-20 00:10:07.117	2008	f
164	E	Congresso Latinoamericano de Distribucion Electrica (CLADE'2008)	\N	\N	20	2016-09-20 00:13:56.497	2008	f
165	E	7th International Workshop on Ambient Intelligence and Embedded Systems	\N	\N	20	2016-09-20 00:14:39.097	2008	f
167	E	12th IEEE Conference on Emerging Technologies and Factory Automation	508	\N	20	2016-09-20 00:14:45.355	2007	f
169	E	IV Workshop de Sistemas Operacionais	\N	\N	20	2016-09-20 00:14:56.782	2007	f
176	E	The 3rd ACS/IEEE International Conference on Computer Systems and Applications	54	\N	20	2016-09-20 00:15:54.89	2005	f
179	E	10th IEEE International Conference on Emerging Technologies and Factory Automation	508	\N	20	2016-09-20 00:16:37.256	2005	f
180	E	16th Symposium on Computer Architecture and High Performance Computing	1361	\N	20	2016-09-20 00:16:57.297	2004	f
181	E	First IEEE/ACM International Symposium on Cluster Computing and the Grid	210	\N	20	2016-09-20 00:17:20.2	2001	f
189	E	Conferência Latino-americana de Informática	271	\N	20	2016-09-20 00:18:53.695	1994	f
196	E	International Conference on Web Engineering	864	\N	20	2016-09-20 09:15:05.811	2014	f
201	E	VI Escola Regional de Banco de Dados	\N	\N	20	2016-09-20 09:20:20.24	2010	f
273	E	2015 IEEE International Conference on Industrial Technology (ICIT)	792	\N	20	2016-09-20 10:46:42.921	2015	f
247	E	Telecommunications 2005 - Service Assurance with Partial and Intermittent Resources - SAPIR 2005	1353	\N	20	2016-09-20 10:37:05.097	2005	f
246	E	V Simpósio Brasileiro em Segurança da Informação e de Sistemas Computacionais	1382	\N	20	2016-09-20 10:37:08.327	2005	f
253	E	LANOMS 2003 - The 3rd IEEE Latin American Network Operations and Management Symposium	1068	\N	20	2016-09-20 10:40:17.298	2003	f
257	E	SBRC 2002 - Simpósio Brasileiro de Redes de Computadores	1377	\N	20	2016-09-20 10:41:01.586	2002	f
292	E	10 Congresso de Iniciação Ciêntífica, Prêmio Jovem Pesquisador- 3º Encontro da pós-graduação	\N	\N	20	2016-09-20 10:53:15.807	2002	f
303	E	Latin-American Symposium on Dependable Computing	1064	\N	20	2016-09-20 11:04:07.474	2011	f
304	E	14th IEEE International Conference on Emerging Technologies and Factory Automation	508	\N	20	2016-09-20 11:04:18.319	2009	f
322	E	American Geophysical Union, 1999 Fall Meeting	\N	\N	20	2016-09-20 11:06:56.851	1999	f
323	E	7th IEEE Asian Test Symposium - ATS 98	\N	\N	20	2016-09-20 11:07:01.592	1998	f
332	E	AICT 2015 - The Eleventh Advanced International Conference on Telecommunications	55	\N	20	2016-09-21 10:31:13.954	2015	f
333	E	Thirteenth International Conference on Networks	822	\N	20	2016-09-21 10:31:20.632	2014	f
420	E	FEES - Fórum de Educação em Engenharia de Software	\N	\N	20	2016-09-21 11:07:13.945	2009	f
423	E	EuroSPI	848	\N	20	2016-09-21 11:07:56.363	2008	f
439	E	2015 IEEE 8th International Conference on Cloud Computing (CLOUD)	276	\N	20	2016-09-21 11:18:00.686	2015	f
441	E	21st Brazilian Symposium on Multimedia and the Web	1632	\N	20	2016-09-21 11:18:04.771	2015	f
447	E	2013 IEEE 16th International Conference on Computational Science and Engineering (CSE)	328	\N	20	2016-09-21 11:20:00.221	2013	f
453	E	Workshop on Interactive Digital TV in Emergent Countries	\N	\N	20	2016-09-21 11:20:42.616	2010	f
456	E	Simpósio Brasileiro em Computação Ubíqua e Pervasiva	1366	\N	20	2016-09-21 11:21:00.503	2009	f
458	E	ACM Symposium on Applied Computing	1340	\N	20	2016-09-21 11:21:05.739	2008	f
470	E	Workshop de Trabalhos de Iniciação Científica e de Graduação (WTICG)/Simpósio Brasileiro em Segurança da Informação e de Sistemas Computacionais (SBSeg),	1382	\N	20	2016-09-21 11:24:29.72	2015	f
474	E	9TH WORKSHOP ON SOFTWARE TECHNOLOGIES FOR FUTURE EMBEDDED AND UBIQUITOUS SYSTEMS (SEUS 2013)	\N	\N	20	2016-09-21 11:24:54.567	2013	f
475	E	Workshop de Gestão de Identidades Digitais	\N	\N	20	2016-09-21 11:25:02.716	2013	f
592	E	2014 IEEE International Conference on Fuzzy Systems (FUZZIEEE)	593	\N	20	2016-09-27 10:42:24.637	2014	f
597	E	Workshop on UnConventional High Performance Computing (UCHPC)	\N	\N	20	2016-09-27 13:04:54.456	2016	f
606	E	xACS/IEEE International Conference on Computer Systems and Applications AICCSA 2016	54	\N	20	2016-09-27 22:04:52.654	2016	f
611	E	SBSI 2015 (XI Simpósio Brasileiro de Sistemas de Informação)	1383	\N	20	2016-09-27 22:05:39.409	2015	f
618	E	XIII Simpósio Brasileiro de Qualidade de Software (SBQS 2014)	1376	\N	20	2016-09-27 22:06:06.173	2014	f
622	E	24th International Conference on Software Engineering & Knowledge Engineering (SEKE'2012)	1415	\N	20	2016-09-27 22:06:18.656	2012	f
623	E	Winter Simulation Conference 2015	1684	\N	20	2016-09-27 22:06:41.528	2015	f
629	E	XXVI SBIE - Simpósio Brasileiro de Informática na Educação	1371	\N	20	2016-09-27 22:07:31.576	2015	f
634	E	XI SBAI - Simpósio Brasileiro de Automação Inteligente	1362	\N	20	2016-09-27 22:07:46.929	2013	f
687	E	19th International Conference on Collaboration and Technology (CRIWG 2013)	\N	\N	20	2016-09-27 22:15:28.016	2013	f
712	E	46th ACM/IEEE Design Automation Conference	345	\N	20	2016-10-04 10:58:10.978	2009	f
718	E	50th IEEE International Midwest Symposium on Circuits and Systems/5th IEEE International Northeast Workshop on Circuits and Systems	1180	\N	20	2016-10-04 10:59:02.896	2007	f
763	E	Enia - V Encontro Nacional de Inteligencia Artificial	477	\N	20	2016-10-04 11:07:40.355	2005	f
726	E	9th ACM/IEEE International Symposium on System Synthesis	\N	\N	20	2016-10-04 11:00:17.852	1996	f
640	E	Escola Regional de Bancos de Dados	\N	\N	20	2016-09-27 22:08:41.098	2014	f
641	E	Brazilian Symposium on Geoinformatics	\N	\N	20	2016-09-27 22:08:51.367	2014	f
663	E	14th Mexican International Conference on Artificial Intelligence	1121	\N	20	2016-09-27 22:11:21.84	2015	f
660	E	2012 9th IEEE International Workshop on Factory Communication Systems (WFCS 2012)	\N	\N	20	2016-09-27 22:11:03.021	2012	f
736	E	2009 IEEE Symposium on Computers and Communications (ISCC)	958	\N	20	2016-10-04 11:02:43.742	2009	f
740	E	The 21th IEEE International Symposium on Computer-Based Medical Systems	201	\N	20	2016-10-04 11:03:44.808	2008	f
741	E	2008 IEEE 11th Internantional Conference on Computational Science and Engineering - CSE	328	\N	20	2016-10-04 11:03:53.77	2008	f
744	E	13th IEEEE International Conference on Emerging Technologies and Factory Automation	508	\N	20	2016-10-04 11:04:23.457	2008	f
104	E	SBIA´97 - Simpósio Brasileiro de Inteligência Artificial, Brasilia	1370	\N	20	2016-09-19 22:20:46.576	1997	t
109	E	Proceedings des Aachener Workshops Bildverarbeitung für die Medizin	\N	\N	20	2016-09-19 22:21:17.306	1996	t
114	E	Proceedings of the Workshop on Neural Networks Applications and Tools, Liverpool	\N	\N	20	2016-09-19 22:22:08.913	1993	t
126	E	4th Brazilian Symposium on Computing Systems Engineering	1368	\N	20	2016-09-19 23:45:07.327	2014	t
125	E	2014 12th IEEE International Conference on Industrial Informatics (INDIN)	919	\N	20	2016-09-19 23:36:12.235	2014	t
155	E	17th IEEE International Conference on Telecommunications	851	\N	20	2016-09-20 00:01:34.818	2010	t
156	E	IADIS International Conference Applied Computing	12	\N	20	2016-09-20 00:05:08.736	2010	t
209	E	VII Simpósio de Informática do Planalto Médio	\N	\N	20	2016-09-20 09:23:14.831	2007	t
216	E	I Escola Regional de Banco de Dados - ERBD	\N	\N	20	2016-09-20 09:23:43.866	2005	t
244	E	IEEE Globecom 2007 General Symposium	\N	\N	20	2016-09-20 10:36:15.495	2007	t
297	E	2015 IEEE 23rd Annual International Symposium on FieldProgrammable Custom Computing Machines (FCCM)	543	\N	20	2016-09-20 11:02:41.537	2015	t
299	E	2014 6th IEEE LatinAmerican Conference on Communications (LATINCOM)	1074	\N	20	2016-09-20 11:03:14.357	2014	t
302	E	XVIII Simpósio Brasileiro de Sistemas Multimídia e Web	1632	\N	20	2016-09-20 11:03:58.189	2012	t
312	E	II HP Brazil Tech Symposium	\N	\N	20	2016-09-20 11:05:38.186	2005	t
313	E	Conferencia Latino-Americana de Informatica	271	\N	20	2016-09-20 11:05:43.408	2005	t
365	E	X Workshop em Clouds e Aplicações - WCGA	1615	\N	20	2016-09-21 10:37:03.066	2012	t
366	E	The Tenth International Conference on Networks - ICN 2011	813	\N	20	2016-09-21 10:37:13.973	2011	t
391	E	27th International Conference on Software Engineering and Knowledge Engineering	1415	\N	20	2016-09-21 10:51:34.317	2015	t
403	E	EuroSPI - Conference on European System, Software and Service Process Improvement & Innovation	530	\N	20	2016-09-21 10:53:36.759	2012	t
404	E	18th Americas Conference on Information Systems Seattle	84	\N	20	2016-09-21 10:53:39.746	2012	t
467	E	International Conference on Availability, Reliability and Security	116	\N	20	2016-09-21 11:24:38.581	2014	t
425	E	SBES 2008 - FEES - Fórum de Educação em Engenharia de Software	1367	\N	20	2016-09-21 11:08:33.828	2008	t
428	E	4th Metadata and Semantics Research Conference	1171	\N	20	2016-09-21 11:10:16.299	2010	t
432	E	Portuguese Conference on Artificial Intelligence - EPIA	484	\N	20	2016-09-21 11:10:42.135	2009	t
448	E	Simpósio Brasileiro de Computação Ubíqua e Pervasiva	1366	\N	20	2016-09-21 11:20:12.892	2012	t
503	E	VIII SOUTHERN CONFERENCE ON PROGRAMMABLE LOGIC (SPL2012)	1477	\N	20	2016-09-24 18:33:24.478	2012	t
547	E	2013 IEEE Symposium on Computers and Communications (ISCC)	958	\N	20	2016-09-27 10:24:37.055	2013	t
557	E	2012 19th IEEE International Conference on Electronics, Circuits and Systems (ICECS 2012)	756	\N	20	2016-09-27 10:26:14.907	2012	t
594	E	the 28th Annual ACM Symposium	\N	\N	20	2016-09-27 10:42:37.524	2013	t
757	E	20th Annual International Symposium on High Performance Computing Systems and Applications (HPCS 2006),	659	\N	20	2016-10-04 11:06:35.151	2006	t
766	E	Escola Regional de Alto-Desempenho do RGS (ERAD)	488	\N	20	2016-10-04 11:08:01.188	2004	t
288	E	10. Methoden und Beschreibungsprachen zur Modellierung und Verifikation von Schaltungen und Systemen - MBMV 07	\N	\N	20	2016-09-20 10:51:33.008	2007	t
289	E	<![CDATA[IEEE Computer Society Annual Symposium on VLSI (ISVLSI '07)]]>	1006	\N	20	2016-09-20 10:52:36.086	2007	t
506	E	XXXVI Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos	1377	\N	20	2016-09-24 18:47:32.133	2016	t
512	E	9th IARIA International Conference on Systems	824	\N	20	2016-09-24 18:48:39.198	2014	t
513	E	XXXII Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos	1377	\N	20	2016-09-24 18:48:40.443	2014	t
520	E	III Brazilian Symposium on Computing Systems Engineering	1368	\N	20	2016-09-24 18:49:52.585	2013	t
35	E	WEBMEDIA 2014 - XX Simpósio Brasileiro de Sistemas Multimídia e Web	1632	\N	20	2016-09-19 23:42:32.217	2014	t
149	E	IEEE International Conference on Dependable, Autonomic and Secure Computing	351	\N	20	2016-09-19 23:58:35.184	2011	t
151	E	2011 IEEE Globecom Workshops	\N	\N	20	2016-09-19 23:58:47.599	2011	t
153	E	Symposium on Electronic Materials at the Interface with Biology	\N	\N	20	2016-09-20 00:01:25.811	2010	t
162	E	First ACM Workshop on Hot Topics in Software Upgrades (HotSWUp)	\N	\N	20	2016-09-20 00:12:49.624	2008	t
170	E	Conferência Brasileira sobre Qualidade da Energia Elétrica	\N	\N	20	2016-09-20 00:15:08.184	2007	t
183	E	SNOW: a Parallel Programming Environment for Clusters of Worksstations	\N	\N	20	2016-09-20 00:17:40.231	2000	t
267	E	XXV Conferencia Latinoamericana de Informatica - CLEI99	271	\N	20	2016-09-20 10:43:02.34	1999	t
284	E	IV Simpósio Brasil-Alemanha	\N	\N	20	2016-09-20 10:51:00.058	2009	t
296	E	XI Jornadas sobre Sistemas Reconfiguráveis	\N	\N	20	2016-09-20 11:01:39.982	2015	t
347	E	Second International Conference on Emerging Security Information, Systems and Technologies, 2008. SECURWARE '08.	1412	\N	20	2016-09-21 10:33:55.243	2008	t
329	E	ICN 2016 - The Fifteenth International Conference on Networks	813	\N	20	2016-09-21 10:36:05.604	2016	t
415	E	SPICE - International SPICE Conference on Process Improvement and Capability dEtermination in Software, Systems Engineering and Service Management	1475	\N	20	2016-09-21 11:06:46.615	2010	t
418	E	PRO-VE - 11th IFIP Working Conference on VIRTUAL ENTERPRISES	1291	\N	20	2016-09-21 11:07:03.294	2010	t
478	E	Fifth International Conference on eHealth, Telemedicine, and Social Medicine	507	\N	20	2016-09-21 11:25:58.502	2012	t
482	E	IX Simpósio Brasileiro em Segurança da Informação e de Sistemes Computacionais (SBSEG 09)	1382	\N	20	2016-09-21 11:27:25.475	2009	t
491	E	SYMPOSIUM ON INTEGRATED CIRCUITS AND SYSTEMS DESIGN, 29 (SBCCI2016)	1364	\N	20	2016-09-24 18:32:26.468	2016	t
498	E	4th LATIN AMERICAN SYMPOSIUM ON CIRCUITS AND SYSTEMS (LASCAS2013)	1070	\N	20	2016-09-24 18:33:14.119	2013	t
502	E	3rd LATIN AMERICAN SYMPOSIUM ON CIRCUITS AND SYSTEMS (LASCAS2012)	1070	\N	20	2016-09-24 18:33:22.626	2012	t
518	E	8th International Conference on Dependability and Complex Systems	370	\N	20	2016-09-24 18:49:28.764	2013	t
473	E	28th Symposium On Applied Computing	1340	\N	20	2016-09-21 11:24:42.688	2013	t
582	E	Congresso Internacional Conhecimento e Inovação, CKI,	\N	\N	20	2016-09-27 10:30:03.961	2013	t
586	E	XII Escola Regional de Alto Desempenho do Estado do Rio Grande do Sul - 2012 - Erechim, RS	488	\N	20	2016-09-27 10:31:04.598	2012	t
588	E	Winter Simulation Conference - WSC '15	1684	\N	20	2016-09-27 10:42:05.936	2015	t
598	E	Simpósio em Sistemas Computacionais de Alto Desempenho (WSCAD)	1685	\N	20	2016-09-27 13:05:39.949	2014	t
621	E	SBSI 2012 - VIII Simpósio Brasileiro de Sistemas de Informação	1383	\N	20	2016-09-27 22:06:17.462	2012	t
673	E	IBERAMIA - 13th Ibero-American Conference on Artificial Intelligenc	680	\N	20	2016-09-27 22:12:25.065	2012	t
678	E	Workshop de Computação Aplicada em Governo Eletrônico	1616	\N	20	2016-09-27 22:13:08.853	2013	t
53	E	ISCC'08 - IEEE Symposium on Computers and Communications	958	\N	20	2016-09-19 22:11:35.627	2008	t
144	E	37th Annual Conference of the IEEE Industrial Electronics Society	879	\N	20	2016-09-19 23:42:12.232	2011	t
175	E	9th ACM/IEEE International Symposium on Modeling, Analysis and Simulation of Wireless and Mobile Systems	1169	\N	20	2016-09-20 00:15:47.009	2006	t
177	E	20th Annual ACM Symposium on Applied Computing	1340	\N	20	2016-09-20 00:15:56.638	2005	t
188	E	9th International Symposium for Advanced Studies in Systems Research and Cybernetics	\N	\N	20	2016-09-20 00:18:25.811	1997	t
190	E	International Conference on Internet and Web Applications and Services	795	\N	20	2016-09-20 09:13:53.026	2016	t
77	E	SCPDI 2002 - II Simpósio Catarinense de Processamento Digital de Imagens	\N	\N	20	2016-09-19 22:16:11	2002	t
274	E	Cadence CDNLive EMEA 2015	\N	\N	20	2016-09-20 10:46:46.949	2015	t
298	E	2015 IEEE Computer Society Annual Symposium on VLSI (ISVLSI)	1006	\N	20	2016-09-20 11:02:46.701	2015	t
301	E	Radiation Effects on Components and Systems	1306	\N	20	2016-09-20 11:03:25.228	2013	t
318	E	SBCCI - XIV SYMPOSIUM ON INTEGRATED CIRCUITS AND SYSTEMS	1364	\N	20	2016-09-20 11:06:22.253	2001	t
319	E	LATW'00 - 1st IEEE Latin-American Test Workshop	1077	\N	20	2016-09-20 11:06:33.983	2000	t
321	E	MAPLD'99 - 2nd Annual Military and Aerospace Applications of Programmable Devices and Technologies Conference	\N	\N	20	2016-09-20 11:06:42.448	1999	t
625	E	Winter Simulation Conference	1684	\N	20	2016-09-27 22:06:51.325	2014	f
325	E	IFAC - Symposium on Fault Detection, Supervision and Safety for Technical Process: SAFEPROCESS'97	1345	\N	20	2016-09-20 11:07:18.916	1997	t
334	E	IEEE/IFIP Network Operations and Management Symposium, NOMS 2014,	1206	\N	20	2016-09-21 10:31:31.151	2014	t
358	E	XXIV Simpósio Brasileiro de Redes de Computadores - IV Workshop on Computational Grids and Applications	1377	\N	20	2016-09-21 10:35:07.232	2006	t
419	E	EES - Fórum de Educação em Engenharia de Software	\N	\N	20	2016-09-21 11:07:09.642	2009	t
421	E	EuroSPI - International Conference on European System & Software Process Improvement and Innovation	530	\N	20	2016-09-21 11:07:19.569	2009	t
433	E	The 7th IEEE International Conference on Advanced Learning Technologies	697	\N	20	2016-09-21 11:10:53.151	2007	t
460	E	Simpósio Brasileiro de Componentes, Arquiteturas e Reutilização de Software	1363	\N	20	2016-09-21 11:21:11.842	2008	t
476	E	2012 IEEE 11th International Conference on Trust, Security and Privacy in Computing and Communications (TrustCom)	\N	\N	20	2016-09-21 11:25:33.84	2012	t
480	E	Workshop de Trabalhos de Iniciação Científica e de Graduação	\N	\N	20	2016-09-21 11:28:49.839	2009	t
483	E	7th symposium on Identity and trust on the Internet	\N	\N	20	2016-09-21 11:29:01.339	2008	t
585	E	International Conference on Parallel and Distributed Processing Techniques and Applications	1260	\N	20	2016-09-27 10:31:00.149	2012	t
602	E	Conférence de Recherche en Informatique (CRI)	\N	\N	20	2016-09-27 13:06:10.83	2013	t
614	E	XVII Ibero-American Conference on Software Engineering	241	\N	20	2016-09-27 22:05:50.7	2014	t
479	E	X CertForum	\N	\N	20	2016-09-21 11:26:02.098	2012	t
630	E	Simpósio sobre Avaliação de Instituições de Educação Superior	\N	\N	20	2016-09-27 22:07:34.331	2015	t
705	E	Simposio Brasileiro de Sistemas de Informacao	1383	\N	20	2016-09-27 22:23:14.478	2013	t
599	E	Euromicro International Conference on Parallel, Distributed, and Network-Based Processing (PDP)	1259	\N	20	2016-09-27 13:05:25.16	2014	t
603	E	International Conference on Computers and Their Applications - CATA 2016	192	\N	20	2016-09-27 22:04:35.681	2016	t
605	E	International Conference on Natural Computation, Fuzzy Systems and Knowledge Discovery (ICNC-FSKD)	585	\N	20	2016-09-27 22:04:43.122	2016	t
607	E	ICIRA 2014 - International Conference on Intelligent Robotics and Applications	787	\N	20	2016-09-27 22:04:56.059	2014	t
42	E	23rd International Conference on Software Engineering & Knowledge Engineering (SEKE'2011)	1415	\N	20	2016-09-19 22:09:28.777	2011	t
716	E	13th Iberchip Workshop (IWS 2007)	681	\N	20	2016-10-04 10:58:43.324	2007	t
722	E	3rd International IEEE Northeast Workshop on Circuits and Systems	1194	\N	20	2016-10-04 10:59:42.388	2005	t
724	E	ACM/IEEE Design, Automation and Test in Europe Conference	354	\N	20	2016-10-04 10:59:58.6	1999	t
636	E	XII Simpósio Brasileiro de Sistemas de Informação	1383	\N	20	2016-09-27 22:08:17.246	2016	t
652	E	42nd Annual Conference of IEEE Industrial Electronics Society (IECON)	879	\N	20	2016-09-27 22:10:12.345	2016	t
657	E	III Simpósio Brasileiro de Engenharia de Sistemas Computacionais (SBESC)	1368	\N	20	2016-09-27 22:10:44.628	2013	t
738	E	2009 21st International Symposium on Computer Architecture and High Performance Computing (SBACPAD)	1361	\N	20	2016-10-04 11:03:33.907	2009	t
742	E	IX Simposio em Sistemas Computacionais - WSCAD	1685	\N	20	2016-10-04 11:03:58.887	2008	t
661	E	Simpósio Brasileiro de Informática na Educação	1371	\N	20	2016-09-27 22:11:18.857	2015	t
454	E	Workshop de TV Digital Interativa	\N	\N	20	2016-09-21 11:20:45.571	2010	t
688	E	International Conference on Internet and Web Applications and Services (ICIW)	795	\N	20	2016-09-27 22:15:44.364	2016	f
692	E	XIII Internacional Conference on WWW/Internet (ICWI 2014)	866	\N	20	2016-09-27 22:16:34.035	2014	t
694	E	International Conference on Information Integration and Web-based Applications & Services	795	\N	20	2016-09-27 22:16:48.939	2013	t
703	E	13th International Symposium on Advances in Spatial and Temporal Databases (SSTD)	1495	\N	20	2016-09-27 22:23:02.356	2013	t
730	E	2011 IEEE/IFIP 9th International Conference on Embedded and Ubiquitous Computing (EUC)	512	\N	20	2016-10-04 11:01:34.533	2011	t
739	E	X Simpósio em Sistemas Computacionais	1685	\N	20	2016-10-04 11:03:40.385	2009	t
747	E	IEEE Symposium on Computers and Communications (ISCC'07)	958	\N	20	2016-10-04 11:04:43.108	2007	t
683	E	International Conference on Security and Cryptography	26	\N	20	2016-09-27 22:14:15.5	2012	t
745	E	CLEI 2008 \t XXXIV Conferencia Latinoamericana de Informática	271	\N	20	2016-10-04 11:04:31.606	2008	t
756	E	6a Escola Regional de Alto Desempenho	488	\N	20	2016-10-04 11:06:28.334	2006	t
758	E	International Symposium on High Performance Computing Systems and Applications (HPCS 2006),	659	\N	20	2016-10-04 11:06:39.344	2006	t
759	E	XXXIII Seminário Integrado de Software e Hardware	1420	\N	20	2016-10-04 11:06:43.874	2006	f
767	E	Escola Regional de Alto desemepnho RGS (ERAD)	488	\N	20	2016-10-04 11:08:07.739	2004	f
676	E	Simpósio Brasileiro em Segurança da Informação e de Sistemas Computacionais	1382	\N	20	2016-09-27 22:14:13.385	2012	f
685	E	The 25th International Conference on Software Engineering & Knowledge Engineering	1415	\N	20	2016-09-27 22:14:31.231	2013	f
695	E	International Workshop on the Web and Databases (WebDB 2011)/2012 ACM SIGMOD/PODS Conference	1629	\N	20	2016-09-27 22:16:56.287	2012	f
700	E	XXV Simpósio Brasileiro de Informática na Educação	1371	\N	20	2016-09-27 22:18:17.994	2014	f
746	E	21st International Symposium on High Performance Computing Systems and Applications (HPCS'07)	659	\N	20	2016-10-04 11:04:38.084	2007	f
764	E	4th International Information and Telecommunication Technologies Symposium - I2TS	670	\N	20	2016-10-04 11:07:53.958	2005	f
760	E	International Conference on Computational Science	728	\N	20	2016-10-04 11:06:46.475	2005	f
772	E	III SIMPÓSIO DE INFORMÁTICA DA REGIÃO CENTRO/RS	\N	\N	20	2016-10-04 11:08:39.77	2004	f
187	E	Meeting of the Operating System Group of the German Computer Science Society	\N	\N	20	2016-09-20 00:18:12.252	1998	f
773	E	III Simpósio de Informática da Região Centro/RS	\N	\N	20	2016-10-04 11:08:54.858	2004	t
197	E	Workshop de Teses e Dissertações em Banco de Dados	\N	\N	20	2016-09-20 09:15:09.951	2014	f
310	E	VLSI-SoC	\N	\N	20	2016-09-20 11:05:27.503	2007	f
777	E	ABUS`88 - Associação dos Usuários Sisgraph- Intergraph	\N	\N	20	2016-10-04 11:12:00.857	1988	f
778	E	DECUS - Digital Users Group	\N	\N	20	2016-10-04 11:12:06.154	1988	f
775	E	SBAC- PAD 13th Symposium on Computer Architecture and High-Performance	1361	\N	20	2016-10-04 11:11:06.889	2001	t
780	E	the 21st Brazilian Symposium	\N	\N	20	2016-10-04 11:12:37.232	2015	t
781	E	ICANN2010 - International Conference on Artificial Neural Networks	698	\N	20	2016-10-04 11:12:44.333	2011	t
782	E	Neural Networks (IJCNN), The 2011 International Joint Conference on	904	\N	20	2016-10-04 11:12:46.963	2011	t
587	E	Computer On the Beach	\N	\N	20	2016-09-27 10:31:14.977	2012	t
559	E	XVI Escola Regional de Alto Desempenho	488	\N	20	2016-09-27 10:26:38.05	2016	f
589	E	2015 International Joint Conference on Neural Networks (IJCNN)	904	\N	20	2016-09-27 10:42:13.277	2015	t
593	E	2014 IEEE Congress on Evolutionary Computation (CEC)	222	\N	20	2016-09-27 10:42:31.744	2014	t
615	E	Computer on the Beach 2014	\N	\N	20	2016-09-27 22:05:54.539	2014	t
596	E	Escola Regional de Alto Desempenho do Estado do Rio Grande do Sul (ERAD/RS)	488	\N	20	2016-09-27 13:05:27.702	2014	t
616	E	II Workshop de Transparência em Sistemas (WTrans 2014)	\N	\N	20	2016-09-27 22:06:02.71	2014	t
774	E	The 16th Annual International Symposium on High Performance Computing Systems and Applications (HPCS?2002)	659	\N	20	2016-10-04 11:10:42.747	2002	f
776	E	IV Workshop em Internet, Linux e Aplicações	\N	\N	20	2016-10-04 11:11:13.493	2001	f
779	E	Conferência da SUCESU	\N	\N	20	2016-10-04 11:12:09.228	1990	f
560	E	21st IEEE Symposium on Computers and Communications (ISCC)	958	\N	20	2016-09-27 10:26:41.654	2016	f
566	E	WSCAD: Workshop de Iniciação Científica (WIC)	\N	\N	20	2016-09-27 10:27:20.379	2016	f
584	E	XIII Simpósio Brasileiro em Sistemas Computacionais (WSCAD 2012)	1685	\N	20	2016-09-27 10:30:56.037	2012	f
600	E	High Performance Computing Conference (HiPC)	641	\N	20	2016-09-27 13:05:31.342	2014	f
601	E	Workshop on Irregular Applications: Architectures & Algorithms (IA^3) - Supercomputing Conference (SC)	\N	\N	20	2016-09-27 13:06:05.047	2013	f
608	E	SBESC 2013 - Sistemas de Tempo Real	\N	\N	20	2016-09-27 22:05:16.377	2013	f
612	E	The 27th International Conference on Software Engineering and Knowledge Engineering	1415	\N	20	2016-09-27 22:05:43.603	2015	f
617	E	2014 IEEE International Conference on Information Reuse and Integration (IRI)	946	\N	20	2016-09-27 22:06:04.756	2014	f
626	E	Winter Simulation Conference 2013	1684	\N	20	2016-09-27 22:06:52.31	2013	f
668	E	12th International Conference on Intelligent Tutoring Systems, ITS 2014	794	\N	20	2016-09-27 22:12:03.156	2014	f
679	E	International Conference on Emerging Security Information, Systems and Technologies	1412	\N	20	2016-09-27 22:13:12.532	2013	f
681	E	2013 12th IEEE International Conference on Trust, Security and Privacy in Computing and Communications (TrustCom)	\N	\N	20	2016-09-27 22:14:04.536	2013	f
604	E	Escola Regional de Alto Desempenho (ERAD) do Estado do Rio Grande do Sul	488	\N	20	2016-09-27 22:04:38.594	2016	f
707	E	SBES - Simpósio Brasileiro de Engenharia de Software	1367	\N	20	2016-10-04 10:52:32.721	1996	f
708	E	22º Workshop sobre Educação em Computação/CSBC	\N	\N	20	2016-10-04 10:54:18.75	2014	f
709	E	24th Symposium on Integrated Circuits and Systems Design (SBCCI 2011)	1364	\N	20	2016-10-04 10:57:56.415	2011	f
710	E	18th IEEE International Conference on Electronics, Circuits, and Systems (ICECS)	756	\N	20	2016-10-04 10:58:02.432	2011	f
713	E	22nd Symposium on Integrated Circuits and Systems Design	1364	\N	20	2016-10-04 10:58:14.009	2009	f
714	E	8th IEEE Latin American Test Workshop	1077	\N	20	2016-10-04 10:58:17.988	2007	f
715	E	IEEE Computer Society Annual Symposium on VLSI (ISVLSI 2007)	1006	\N	20	2016-10-04 10:58:34.514	2007	f
717	E	6th IEEE/ACM International Symposium on Low Power Electronics and Design	977	\N	20	2016-10-04 10:58:49.721	2007	f
755	E	19th International Symposium on Computer Architecture	956	\N	20	2016-10-04 11:06:18.575	2007	f
720	E	Microelectronics Student Forum (SFM 2006)	1429	\N	20	2016-10-04 10:59:15.206	2006	f
723	E	SAMOS V: Embedded Computer Systems: Architectures, Modeling, and Simulation Workshop	1350	\N	20	2016-10-04 10:59:52.551	2005	f
727	E	SEKE - Software Engineering Knowledge Engineering	1415	\N	20	2016-10-04 11:01:23.352	2011	f
735	E	IEEE Symposium on Computers and Communications (ISCC'09)	958	\N	20	2016-10-04 11:02:42.057	2009	f
743	E	WeBmedia 2008	1632	\N	20	2016-10-04 11:04:08.814	2008	f
748	E	16th IEEE International Workshops on Enabling Technologies: Infrastructures for Collaborative Enterprises	1641	\N	20	2016-10-04 11:04:50.08	2007	f
711	E	18th IEEE International Conference on Electronics, Circuits, and Systems (ICECS	756	\N	20	2016-10-04 10:58:05.334	2011	t
719	E	XII Workshop IBERCHIP	681	\N	20	2016-10-04 10:59:08.227	2006	t
721	E	XI Workshop Iberchip	681	\N	20	2016-10-04 10:59:18.497	2005	t
725	E	ACM/IEEE 36th Design Automation Conference	345	\N	20	2016-10-04 11:00:01.13	1999	t
728	E	Seke - Software Engineering Knowledge Engineering	1415	\N	20	2016-10-04 11:01:28.096	2011	t
729	E	2011 IEEE Symposium on Computers and Communications (ISCC)	958	\N	20	2016-10-04 11:01:31.858	2011	t
731	E	2011 20th IEEE International Workshop On Enabling Technologies: Infrastructure For Collaborative Enterprises (WETICE)	1641	\N	20	2016-10-04 11:01:35.596	2011	t
732	E	The 2010 International Conference on Grid Computing and Applications	596	\N	20	2016-10-04 11:01:44.726	2010	t
733	E	WCGA 2010 - VIII Workshop de Computação em Cloud, Grids e Aplicações	1615	\N	20	2016-10-04 11:02:14.914	2010	t
734	E	Simposio Brasileiro de Redes de Computadores e Sistemas Distribuidos (SBRC)	1377	\N	20	2016-10-04 11:02:28.402	2009	t
752	E	V Workshop de Computação em Grade e Aplicações (WCGA2007)	1615	\N	20	2016-10-04 11:05:13.593	2007	t
94	E	INTERNATIONAL WORKSHOP ON CYBERNETIC VISION	\N	\N	20	2016-09-19 22:18:48.925	1999	t
102	E	Bildverarbeitung fuer die Medizin 1998	\N	\N	20	2016-09-19 22:19:36.962	1998	t
219	E	I Workshop de Teses e Dissertações em Banco de Dados - (WTDBD)	\N	\N	20	2016-09-20 09:24:02.51	2002	t
269	E	IEEE LANOMS'99 Latin American Network Operations and Management Symposium	1068	\N	20	2016-09-20 10:43:08.199	1999	t
272	E	The Proceedings of the 1996 Summer Computer Simulation Conference	1393	\N	20	2016-09-20 10:43:18.174	1996	t
93	E	XXIX CONGRESSO BRASILEIRO DE RADIOLOGIA	\N	\N	20	2016-09-19 22:18:38.826	2000	t
218	E	I Sessão de Demos - SBBD	\N	\N	20	2016-09-20 09:23:54.961	2004	t
199	E	Simpósio Brasileiro de Banco de Dados	\N	\N	20	2016-09-20 09:24:54.03	2000	t
226	E	Sessão de Demos - XXV SBBD	\N	\N	20	2016-09-20 09:26:18.491	2010	t
260	E	IEEE/IFIP International Symposium on Integrated Network Management	907	\N	20	2016-09-20 10:41:34.653	2001	t
277	E	2012 13th Latin American Test Workshop LATW	1077	\N	20	2016-09-20 10:49:56.111	2012	t
282	E	2010 Design, Automation &amp; Test in Europe Conference &amp; Exhibition (DATE 2010)	354	\N	20	2016-09-20 10:50:23.363	2010	t
230	E	2016 IEEE Symposium on Computers and Communication (ISCC)	958	\N	20	2016-09-20 10:32:54.565	2016	f
268	E	ICON 99 - The IEEE International Conference on Networks	822	\N	20	2016-09-20 10:43:04.803	1999	f
271	E	VII Semana de Pesquisa da UFSC 1999	\N	\N	20	2016-09-20 10:43:13.759	1999	f
275	E	2014 15th Latin American Test Workshop LATW	1077	\N	20	2016-09-20 10:46:59.962	2014	f
276	E	2013 14th Latin American Test Workshop LATW	1077	\N	20	2016-09-20 10:47:15.905	2013	f
279	E	2011 12th Latin American Test Workshop LATW	1077	\N	20	2016-09-20 10:50:08.205	2011	f
280	E	14. Methoden und Beschreibungssprachen zur Modellierung und Verifikation von Schaltungen und Systemen (MBMV)	\N	\N	20	2016-09-20 10:50:10.783	2011	f
283	E	2009 Design, Automation & Test in Europe Conference & Exhibition (DATE'09)	354	\N	20	2016-09-20 10:50:55.946	2009	f
285	E	2008 Design, Automation and Test in Europe	354	\N	20	2016-09-20 10:51:04.198	2008	f
134	E	2013 III Brazilian Symposium on Computing Systems Engineering (SBESC)	\N	\N	20	2016-09-19 23:39:49.578	2013	f
97	E	23rd International Epilepsy Congress	\N	\N	20	2016-09-19 22:19:17.621	1999	f
98	E	GMDS99 - Jahrestagung der Gesellschaft fuer Medizinische Datenverarbeitung und Statistik	\N	\N	20	2016-09-19 22:19:24.666	1999	f
99	E	III Simposio de Cirurgia Endovascular da SBACV	\N	\N	20	2016-09-19 22:19:28.064	1999	f
224	E	International Seminar On Document Management	\N	\N	20	2016-09-20 09:25:32.162	1998	f
258	E	IFIP TC11 17th International Conference on Information Security (SEC2002)	1405	\N	20	2016-09-20 10:41:22.269	2002	f
259	E	IX Simpósio Brasileiro de Computação Tolerante a Falhas (SCTF)	\N	\N	20	2016-09-20 10:41:33.109	2001	f
265	E	CLEI - Conferência Latinoamericana de Informática	271	\N	20	2016-09-20 10:42:01.204	2000	f
281	E	Embedded World Conference	\N	\N	20	2016-09-20 10:50:19.366	2011	f
337	E	The Seventh International Conference on Emerging Security Information, Systems and Technologies - SECURWARE 2013	1412	\N	20	2016-09-21 10:32:01.404	2013	t
349	E	E-MediSys (E-Medical Systems) 2008	\N	\N	20	2016-09-21 10:34:10.227	2008	t
468	E	Simpósio Brasileiro de Sistemas de Informação	1383	\N	20	2016-09-21 11:24:19.944	2016	t
341	E	The Sixth International Conference on Networking and Services - ICNS 2010	817	\N	20	2016-09-21 10:32:58.301	2010	f
344	E	Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos (SBRC 2009). VI Workshop de Computação em Grade e Aplicações (WCGA2009)	1377	\N	20	2016-09-21 10:33:18.056	2009	f
345	E	8th International Information and TelecommunicationTechnologiees Symposium	670	\N	20	2016-09-21 10:33:45.96	2009	f
509	E	3rd ACM Workshop on Distributed Cloud Computing	\N	\N	20	2016-09-24 18:48:15.58	2015	t
348	E	IFIP The Second International Conference on New Technologies, Mobility and Security.	\N	\N	20	2016-09-21 10:34:05.705	2008	f
405	E	ICSOFT ? 7th International Conference on Software Paradigm Trends	\N	\N	20	2016-09-21 10:53:47.14	2012	f
463	E	International Conference on Networking	813	\N	20	2016-09-21 11:21:22.817	2006	f
522	E	15th IEEE International Symposium on Object/Component/ServiceOriented RealTime Distributed Computing (ISORC)	988	\N	20	2016-09-24 18:50:16.983	2012	t
507	E	XXXIV Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos	1377	\N	20	2016-09-24 18:47:31.562	2016	f
355	E	58ª Reunião Anual da SBPC	\N	\N	20	2016-09-21 10:34:53.538	2006	t
464	E	International Conference on Advanced Information Networking and Applications	63	\N	20	2016-09-21 11:21:24.136	2006	t
508	E	XXXIII Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos	1377	\N	20	2016-09-24 18:47:33.907	2015	f
466	E	Workshop em Algoritmos e Aplicações de Mineração de Dados	1591	\N	20	2016-09-21 11:21:25.724	2006	t
521	E	13th International Conference on Computational Science	728	\N	20	2016-09-24 18:50:14.528	2013	f
523	E	XXXVIII Conferencia Latinoamericana de Informática	271	\N	20	2016-09-24 18:50:17.828	2012	f
293	E	VIII Workshop Iberchip	681	\N	20	2016-09-20 10:53:17.713	2002	f
406	E	XIII Workshop Internacional de Software Livre/FISL ? Fórum Internacional Software Livre	\N	\N	20	2016-09-21 10:53:55.641	2012	f
407	E	IHC - Simpósio Brasileiro de Fatores Humanos em Sistemas Computacionais	896	\N	20	2016-09-21 10:53:58.886	2012	f
461	E	Workshop de Desenvolvimento Distribuído de Software	\N	\N	20	2016-09-21 11:21:21.17	2007	f
469	E	The Ninth International Conference on Emerging Security Information, Systems and Technologies	1412	\N	20	2016-09-21 11:24:26.126	2015	f
504	E	19th IEEE INTERNATIONAL CONFERENCE ON ELECTRONICS, CIRCUITS AND SYSTEMS (ICECS2012)	756	\N	20	2016-09-24 18:33:32.861	2012	f
505	E	2016 IEEE 30th International Conference on Advanced Information Networking and Applications (AINA)	63	\N	20	2016-09-24 18:47:29.658	2016	f
689	E	XI Escola Regional de Banco de Dados (ERBD 2015)	\N	\N	20	2016-09-27 22:15:50.695	2015	f
580	E	2013 Eighth International Conference on P2P, Parallel, Grid, Cloud and Internet Computing (3PGCIC)	3	\N	20	2016-09-27 10:29:42.543	2013	t
690	E	XVII International Conference on Information Integration and Web-based Applications & Services (iiWAS 2015)	899	\N	20	2016-09-27 22:16:07.662	2015	t
696	E	East-European Conference on Advances in Databases and Information Systems (ADBIS)	32	\N	20	2016-09-27 22:17:04.245	2012	f
697	E	Congresso Internacional ABED de Educação a Distância	237	\N	20	2016-09-27 22:17:31.48	2015	f
698	E	ongresso Internacional de Métodos e Gestão em Avaliação Educacional,	\N	\N	20	2016-09-27 22:17:40.591	2015	t
704	E	GEOINFO	605	\N	20	2016-09-27 22:23:09.507	2013	t
48	E	IEEE CBMS 2009, the 22 nd IEEE International Symposium on Computer-Based Medical Systems	201	\N	20	2016-09-19 22:10:20.078	2009	t
701	E	International Conference on Software Engineering and Knowledge Engineering	1415	\N	20	2016-09-27 22:22:46.715	2015	f
706	E	29th International Symposium on Computer-Based Medical Systems (CBMS 2016)	201	\N	20	2016-10-04 10:45:02.749	2016	f
49	E	XX Simpósio Brasileiro de Informática na Educação	1371	\N	20	2016-09-19 22:11:17.122	2009	f
138	E	IEEE Sensors	\N	\N	20	2016-09-20 00:05:11.268	2010	t
570	E	2014 IEEE 27th International Symposium on ComputerBased Medical Systems (CBMS)	201	\N	20	2016-09-27 10:28:34.147	2014	t
576	E	The 25th International Conference on Software Engineering and Knowledge Engineering	1415	\N	20	2016-09-27 10:29:21.483	2013	t
529	E	WIM 2016 - XVI Workshop de Informática Médica	1650	\N	9	2016-09-26 18:28:04.975	2016	f
524	E	29th International Symposium on Computer-Based Medical Systems (CBMS 2016)	201	\N	9	2016-09-26 18:27:19.348	2016	f
525	E	7th International Conference on Intelligent Text Processing and Computational Linguistics	244	\N	9	2016-09-26 18:27:25.851	2016	f
526	E	20th International Conference on Information Visualisation	1026	\N	9	2016-09-26 18:27:35.851	2016	f
571	E	2014 IEEE 23rd International Workshops on Enabling Technologies: Infrastructures for Collaborative Enterprise (WETICE)	1641	\N	20	2016-09-27 10:28:37.733	2014	f
572	E	2014 IEEE Symposium on Computers and Communication (ISCC)	958	\N	20	2016-09-27 10:28:58.09	2014	f
579	E	2013 IEEE 9th International Conference on Wireless and Mobile Computing, Networking and Communications (WiMob)	1651	\N	20	2016-09-27 10:29:39.537	2013	t
702	E	IEEE International Conference on Intelligent Systems (IEEE IS)	880	\N	20	2016-09-27 22:22:57.07	2014	t
39	E	COT 2012 - III Computer on the Beach	\N	\N	20	2016-09-19 22:09:08.875	2012	t
583	E	2012 International Conference on Computing, Networking and Communications (ICNC)	815	\N	20	2016-09-27 10:30:41.543	2012	f
444	E	Congresso Linked Open Data Brasil	\N	\N	20	2016-09-21 11:18:51.297	2014	f
691	E	VI International Conference on Advances in Databases, Knowledge, and Data Applications	358	\N	20	2016-09-27 22:16:25.064	2014	f
693	E	European Conference on Information Systems	432	\N	20	2016-09-27 22:16:37.548	2013	f
699	E	C	\N	\N	20	2016-09-27 22:17:48.834	2015	f
52	E	XXI IEEE CBMS - 21th IEEE International Symposium on Computer-Based Medical Systems	201	\N	20	2016-09-19 22:11:33.753	2008	f
55	E	CBIS'2008 - XI Congresso Brasileiro de Informática em Saúde	199	\N	20	2016-09-19 22:12:19.47	2008	t
81	E	Workshop de Medicina na Saúde - Congresso Brasileiro de Computação	\N	\N	20	2016-09-19 22:16:27.057	2001	t
86	E	13. IEEE Symposium on Computer Based Medical Systems	201	\N	20	2016-09-19 22:17:32.667	2000	t
132	E	2013 IEEE Wireless Communications and Networking Conference (WCNC)	1617	\N	20	2016-09-19 23:37:38.102	2013	t
70	E	Proceedings. 17th IEEE Symposium on ComputerBased Medical Systems	201	\N	20	2016-09-19 22:15:01.866	2004	t
64	E	WIM 2006 - VI Workshop de Informática Médica	1650	\N	20	2016-09-19 22:13:45.843	2006	f
71	E	16th IEEE Symposium on ComputerBased Medical Systems. CBMS 2003	201	\N	20	2016-09-19 22:15:27.396	2003	f
72	E	SCPDI 2003 - III Simpósio Catarinense de Processamento Digital de Imagens	\N	\N	20	2016-09-19 22:15:41.082	2003	f
79	E	SEKE 2001 - INTERNATIONAL CONFERENCE ON SOFTWARE ENGINEERING AND KNOWLEDGE ENGINEERING	1415	\N	20	2016-09-19 22:16:17.239	2001	t
87	E	XVII CONGRESSO BRASILEIRO DE ENGENHARIA BIOMÉDICA	\N	\N	20	2016-09-19 22:17:43.517	2000	t
89	E	OD2000 - Objetos Distribuídos	\N	\N	20	2016-09-19 22:18:08.127	2000	t
133	E	2013 IEEE 16th International Symposium on Object/Component/ServiceOriented RealTime Distributed Computing (ISORC)	988	\N	20	2016-09-19 23:39:30.406	2013	t
92	E	TelMed 2000	\N	\N	20	2016-09-19 22:18:32.166	2000	t
95	E	CBMS99 - 12th IEEE Symposium on Computer-Based Medical Systems	201	\N	20	2016-09-19 22:18:53.901	1999	f
106	E	AIM-96 - Symposium on Artificial Intelligence in Medicine. Stanford University	\N	\N	20	2016-09-19 22:21:07.657	1996	f
107	E	AATD´96 - II Workshop Automatische Analyse von Tomografphie-Daten, München	\N	\N	20	2016-09-19 22:21:12.37	1996	f
111	E	ECMI94 - European Conference on Mathematics in Industry	\N	\N	20	2016-09-19 22:21:47.293	1994	f
112	E	8 th International Symposium on Diagnostic Quantitative Pathology, Amsterdam	\N	\N	20	2016-09-19 22:21:54.155	1994	f
121	E	2015 IEEE 20th Conference on Emerging Technologies &amp; Factory Automation (ETFA)	508	\N	20	2016-09-19 23:35:28.804	2015	f
69	E	26. Congresso Brasileiro de Endocrinologia e Metabologia	\N	\N	20	2016-09-19 22:14:55.006	2005	f
63	E	19th IEEE SYMPOSIUM ON COMPUTER BASED MEDICAL SYSTEMS - CBMS2006	201	\N	20	2016-09-19 22:13:30.982	2006	f
67	E	WM2005: Professional Knowledge Management - Experiences and Visions	\N	\N	20	2016-09-19 22:14:39.041	2005	f
74	E	XV International Conference on Computer-Based Medical Systems	862	\N	20	2016-09-19 22:15:49.955	2002	f
91	E	Workshop 2000 of the German-Brazilian Cooperation Programme on Information Technology	\N	\N	20	2016-09-19 22:18:28.396	2000	f
96	E	CARS99 - Computer Assisted Radiology and Surgery - 13th International Congress and Exhibition	\N	\N	20	2016-09-19 22:19:11.821	1999	f
100	E	CARS 99 - Computer Assisted Radiology and Surgery - 13th International Congress and Exhibition	\N	\N	20	2016-09-19 22:19:32.747	1999	f
101	E	Telemed99 - Jahrestagung fuer Telemedizin	\N	\N	20	2016-09-19 22:19:35.207	1999	f
142	E	2011 IEEE International Conference on Systems, Man, and Cybernetics	1453	\N	20	2016-09-19 23:41:29.236	2011	f
152	E	VIII Congresso Brasileiro de Agroinformática	\N	\N	20	2016-09-19 23:59:35.407	2011	f
159	E	6th International IEEE Conference on Industrial Informatics	919	\N	20	2016-09-20 00:10:03.12	2008	f
147	E	2015 Brazilian Symposium on Computing Systems Engineering (SBESC)	1368	\N	20	2016-09-19 23:44:42.749	2015	t
163	E	V Workspot - International Workshop On Power Transformers	\N	\N	20	2016-09-20 00:12:58.1	2008	f
145	E	Brazilian Symposium on Computing System Engineering	1368	\N	20	2016-09-19 23:59:43.859	2011	t
154	E	15th IEEE International Conference on Emerging Techonologies and Factory Automation	508	\N	20	2016-09-20 00:01:31.44	2010	t
166	E	14th Brazilian Symposium on Multimedia and the Web	1632	\N	20	2016-09-20 00:14:41.876	2008	t
168	E	12th IEEE International Conference on Emerging Technologies and Factory Automation	508	\N	20	2016-09-20 00:14:46.771	2007	f
136	E	Second Brazilian Symposium on Computing System Engineering	\N	\N	20	2016-09-19 23:41:14.148	2012	f
139	E	New England Workshop for Software Defined Radio	\N	\N	20	2016-09-19 23:41:21.715	2012	f
140	E	10th International Conference on Networks	822	\N	20	2016-09-19 23:41:25.053	2011	f
173	E	International Conference on Emerging Technologies and Factory Automation	508	\N	20	2016-09-20 00:15:41.753	2006	t
182	E	Proceedings of the 9th International Conference on Architectural Support for Programming Languages and Operating Systems	130	\N	20	2016-09-20 00:17:29.004	2000	f
178	E	9th international symposium on Hihg-Performance Systemas and Applications	659	\N	20	2016-09-20 00:16:05.774	2005	t
193	E	Workshop on Thesis and Dissertations in Databases	\N	\N	20	2016-09-20 09:14:44.745	2015	t
194	E	International Conference on Computer and Information Technology	264	\N	20	2016-09-20 09:14:47.161	2014	t
184	E	11th Symposium on Computer Architecture and High Performance Computing	1361	\N	20	2016-09-20 00:17:51.346	1999	f
186	E	First Euromicro Workshop on Network Computing	\N	\N	20	2016-09-20 00:18:09.715	1998	f
141	E	18th International Conference on Telecommunications	851	\N	20	2016-09-19 23:41:26.701	2011	f
148	E	IADIS Applied Computing 2011	12	\N	20	2016-09-19 23:58:08.078	2011	f
203	E	XVIII Mostra de Iniciação Científica	\N	\N	20	2016-09-20 09:20:42.971	2008	t
227	E	Workshop de Teses e Dissertações em Banco de Dados (WTDBD)	\N	\N	20	2016-09-20 09:26:28.698	2010	t
238	E	SECURWARE 2013 - The Seventh International Conference on Emerging Security Information, Systems and Technologies	1412	\N	20	2016-09-20 10:35:01.081	2013	f
228	E	(Best Demo Award) - Sessão de Demos - XXIV SBBD	\N	\N	20	2016-09-20 09:26:33.565	2009	t
241	E	SEMISH - XXXVIII Seminário Integrado de Software e Hardware	1420	\N	20	2016-09-20 10:35:23.636	2011	f
242	E	The Second International Conference on Emerging Security Information, Systems and Technologies - SECURWARE 2008	1412	\N	20	2016-09-20 10:35:55.587	2008	f
248	E	I WORKSHOP de Peer-to-Peer - WP2P (SBRC 2005)	1677	\N	20	2016-09-20 10:37:11.43	2005	f
252	E	GRES´03 Gestion de Reseaux et de Services	\N	\N	20	2016-09-20 10:40:07.857	2003	f
251	E	II ERRC - Escola Regional de Redes de Computadores	\N	\N	20	2016-09-20 10:37:38.202	2004	t
255	E	ERRC 2003 - I Escola Regional de Redes de Computadores	\N	\N	20	2016-09-20 10:40:30.442	2003	t
266	E	17o Simpósio Brasileiro de Redes de Computadores - SBRC99	1377	\N	20	2016-09-20 10:42:59.119	1999	f
291	E	IX Workshop IBERCHIP	681	\N	20	2016-09-20 10:53:07.311	2003	f
294	E	VII-Brazilian Symposium on Neural Networks - IEEE Computer Society	\N	\N	20	2016-09-20 10:53:27.032	2002	t
300	E	2014 International Telecommunications Symposium (ITS)	1023	\N	20	2016-09-20 11:03:17.755	2014	t
204	E	XXIII Simpósio Brasileiro de Banco de Dados	\N	\N	20	2016-09-20 09:21:17.116	2008	t
211	E	XVII Mostra de Iniciação Científica	\N	\N	20	2016-09-20 09:23:29.499	2007	t
206	E	Conference on Information and Knowledge Management (CIKM). (Em 2007: Qualis Internacional A)	249	\N	20	2016-09-20 09:21:24.645	2007	f
225	E	Sessão de Demos - XXVI SBBD	\N	\N	20	2016-09-20 09:26:16.647	2011	f
223	E	Anais Semana Acadêmica do PPGC	\N	\N	20	2016-09-20 09:25:26.444	1999	t
231	E	15th IEEE International Conference on Computer and Information Technology (CIT-2015)	264	\N	20	2016-09-20 10:32:58.379	2015	f
220	E	2nd International Conference on Electronic Commerce and Web Technologies - EC-Web 2001 (Qualis Internacional A)	442	\N	20	2016-09-20 09:24:07.528	2001	t
221	E	Workshop Intelligent Documents	\N	\N	20	2016-09-20 09:24:59.143	1999	t
229	E	SBSI - Simpósio Brasileiro de Sistemas de Informação	1383	\N	20	2016-09-20 10:35:21.205	2011	t
254	E	SSI 2003 Simpósio de Segurança em Informática	\N	\N	20	2016-09-20 10:40:24.875	2003	f
261	E	XIX Simpósio Brasileiro de Redes de Computadores	1377	\N	20	2016-09-20 10:41:39.752	2001	f
262	E	SSI 2001 Simpósio de Segurança em Informática	\N	\N	20	2016-09-20 10:41:48.301	2001	f
250	E	WSEg 2004 - IV Workshop em Segurança de Sistemas Computacionais	\N	\N	20	2016-09-20 10:37:31.033	2004	t
330	E	NOMS 2016 - Network Operations and Management Symposium	1206	\N	20	2016-09-21 10:30:57.748	2016	t
328	E	VI SCTF - Simpósio de Computadores Tolerantes a Falhas	\N	\N	20	2016-09-20 11:08:07.653	1995	f
338	E	XXXVII Conferencia Latinoamericana de Informática	271	\N	20	2016-09-21 10:32:35.15	2011	t
346	E	ICN 2008. Seventh International Conference on Networking, 2008	813	\N	20	2016-09-21 10:33:50.472	2008	t
335	E	The 2014 International Conference on Security and Management (SAM 2014)	1348	\N	20	2016-09-21 10:31:35.379	2014	f
336	E	The Eighth International Conference on Emerging Security Information, Systems and Technologies (SECURWARE 2014)	1412	\N	20	2016-09-21 10:31:49	2014	f
351	E	Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos (SBRC 2008). XIII Workshop de Gerência e Operação de Redes e Serviços (WGRS 2008).	1377	\N	20	2016-09-21 10:34:39.691	2008	t
354	E	Global Telecommunications Conference, 2007. GLOBECOM '07. IEEE	614	\N	20	2016-09-21 10:34:48.963	2007	t
316	E	FIE'2002 - IEEE Frontiers in Education Conference	555	\N	20	2016-09-20 11:05:59.202	2002	t
364	E	The Eleventh International Conference on Networks - ICN 2012	813	\N	20	2016-09-21 10:36:55.053	2012	f
370	E	ICONS 08. Third International Conference on Systems, 2008	824	\N	20	2016-09-21 10:37:45.174	2008	f
326	E	Congress of the Brazilian Microelectronics Society	\N	\N	20	2016-09-20 11:07:57.018	1996	t
371	E	10th IEEE/IFIP Network Operations and Management Symposium (NOMS 2006)	1206	\N	20	2016-09-21 10:37:55.323	2006	f
342	E	VIII Workshop em Clouds, Grids e Aplicações - Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos (SBRC)	1377	\N	20	2016-09-21 10:33:03.753	2010	t
306	E	9th IEEE International Symposium on Quality Electronic Design (ISQED)	993	\N	20	2016-09-20 11:04:27.538	2008	f
307	E	Symposium on Integrated Circuits and System Design	1364	\N	20	2016-09-20 11:04:43.656	2007	f
311	E	Latin-American Test Workshop	1077	\N	20	2016-09-20 11:05:30.67	2006	f
314	E	ISCIS'04 - IX. International Symposium on Computer and Information Sciences	959	\N	20	2016-09-20 11:05:51.135	2004	f
317	E	First International Symposium on Future Intelligent Earth Observing Systems	\N	\N	20	2016-09-20 11:06:17.743	2002	f
324	E	4th International IEEE On-Line Testing Workshop	930	\N	20	2016-09-20 11:07:13.925	1998	f
327	E	X SBMICRO - Congress of the Brazilian Microeletronics Society	\N	\N	20	2016-09-20 11:07:59.522	1995	f
331	E	IEEE SERVICES 2016 - 12th IEEE World Congress on Services.	1425	\N	20	2016-09-21 10:31:04.902	2016	f
339	E	LANOMS - Latin American Network Operations and Management Symposium	1068	\N	20	2016-09-21 10:32:36.575	2011	f
340	E	The Second International Conference on eHealth, Telemedicine, and Social Medicine - eTELEMED 2010	507	\N	20	2016-09-21 10:32:39.103	2010	f
343	E	Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos (SBRC 2009). XIII Workshop de Gerência e Operação de Redes e Serviços (WGRS 2008)	1377	\N	20	2016-09-21 10:33:13.832	2009	f
356	E	19th IEEE International Symposium on Computer-Based Medical Systems	201	\N	20	2016-09-21 10:34:56.617	2006	f
372	E	Congresso da SBC 2006 - SEMISH 2006	1420	\N	20	2016-09-21 10:39:07.22	2006	f
362	E	ICN 2016, The Fifteenth International Conference on Networks	813	\N	20	2016-09-21 10:36:08.13	2016	t
374	E	4th International Workshop on Middleware for Grid Computing	1119	\N	20	2016-09-21 10:39:11.16	2006	f
381	E	Jornadas Chilenas de Computación 2005 - IX Workshop de Sistemas Distribuídos y paralelismo 2005	\N	\N	20	2016-09-21 10:41:35.739	2005	t
387	E	XII ? Simpósio Brasileiro de Sistemas de Informação	1383	\N	20	2016-09-21 10:44:29.068	2016	t
389	E	XXXVI Congresso da Sociedade Brasileira de Computação / XVI Workshop de Informática Médica	1650	\N	20	2016-09-21 10:51:25.892	2016	t
392	E	VIII Circuito de Tecnologia da Informação (CITI 2015)	\N	\N	20	2016-09-21 10:51:38.337	2015	t
400	E	X Simpósio de Excelência em Gestão e Tecnologia - SEGeT	\N	\N	20	2016-09-21 10:52:42.154	2013	t
412	E	5th World Congress on Software Quality	\N	\N	20	2016-09-21 11:05:12.996	2011	t
409	E	SBIE ? Simpósio Brasileiro de Informática na Educação	1371	\N	20	2016-09-21 10:55:07.974	2012	f
414	E	WEBMEDIA - Simpósio Brasileiro de Sistemas Multimídia e Web	1632	\N	20	2016-09-21 11:06:14.624	2011	t
417	E	EuroSPI - European Systems & Software Process Improvement and Innovation	530	\N	20	2016-09-21 11:06:52.58	2010	t
424	E	Doctorial Symposium at 15th European Systems & Software Process Improvement and Innovation Conference (EuroSPI)	530	\N	20	2016-09-21 11:08:04.784	2008	t
426	E	CBIS - Congresso Brasileiro de Informática em Saúde	199	\N	20	2016-09-21 11:09:25.363	2008	t
367	E	IX Workshop em Clouds, Grids e Aplicações - Simpósio Brasileiro de Redes de Computadores	1377	\N	20	2016-09-21 10:37:26.263	2011	t
373	E	Simpósio Brasileiro em Segurança da Informação e de Sistemas Computacionais (SBSeg).	1382	\N	20	2016-09-21 10:39:10.459	2006	t
375	E	Ninth IFIP/IEEE International Symposium on Integrated Network Management (IM 2005)	907	\N	20	2016-09-21 10:39:23.523	2005	t
383	E	4th Latin American Network Operations and Management Symposium (LANOMS 2005)	1068	\N	20	2016-09-21 10:42:03.744	2005	f
382	E	V Simpósio Brasileiro en Segurança da Informação e de Sistemas Computacionais	1382	\N	20	2016-09-21 10:42:05.11	2005	f
384	E	29th IEEE Conference on Software Engineering Education and Training (CSEE&T 2016)	331	\N	20	2016-09-21 10:44:25.585	2016	f
386	E	18th International Conference on Enterprise Information Systems (ICEIS 2016)	224	\N	20	2016-09-21 10:44:28.447	2016	f
390	E	Computer on the Beach	\N	\N	20	2016-09-21 10:51:30.79	2015	f
394	E	CBMS - Computer-Based Medical Systems	201	\N	20	2016-09-21 10:51:52.161	2014	f
416	E	11th International Conference on Product Focused Software Development and Process Improvement (PROFES 2010)	1292	\N	20	2016-09-21 11:06:49.559	2010	f
385	E	29th IEEE International Symposium on Computer-Based Medical Systems (IEEE CBMS 2016)	201	\N	20	2016-09-21 10:44:27.282	2016	t
399	E	13. ergodesign & usihc	\N	\N	20	2016-09-21 10:52:36.823	2013	t
446	E	Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos	1377	\N	20	2016-09-21 11:19:00.275	2013	t
459	E	Conferencia Latinoamericana de Informática	271	\N	20	2016-09-21 11:21:06.965	2008	f
462	E	Workshop de Testes e Tolerância a Falhas	1694	\N	20	2016-09-21 11:21:22.335	2007	f
451	E	Workshop de Trabalhos de Iniciação Cietífica	\N	\N	20	2016-09-21 11:20:22.56	2011	t
471	E	Workshop on Socio-Technical Aspects in Security and Trust	\N	\N	20	2016-09-21 11:24:37.649	2014	f
465	E	IEEE Symposium on Computers and Communications	958	\N	20	2016-09-21 11:21:24.807	2006	t
494	E	2015 25th International Workshop on Power and Timing Modeling, Optimization and Simulation (PATMOS)	1251	\N	20	2016-09-24 18:32:42.315	2015	t
499	E	2013 IEEE INTERNATIONAL CONFERENCE ON MULTIMEDIA AND EXPO (ICME2013)	802	\N	20	2016-09-24 18:33:15.047	2013	f
515	E	XIV Simpósio Brasiliero em Segurança da Informação e de Sistemas Computacionais	1382	\N	20	2016-09-24 18:49:09.85	2014	f
517	E	18th IEEE Symposium on Computers and Communications	958	\N	20	2016-09-24 18:50:15.151	2013	f
516	E	XXXI Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos	1377	\N	20	2016-09-24 18:49:19.021	2013	t
533	E	2014 22nd Euromicro International Conference on Parallel, Distributed and NetworkBased Processing (PDP)	1259	\N	20	2016-09-27 10:19:28.685	2014	t
538	E	Fault Tolerance for HPC at eXtreme Scale Workshop	\N	\N	20	2016-09-27 10:20:32.718	2014	t
445	E	International Conference on Web Services	870	\N	20	2016-09-21 11:18:55.034	2013	t
430	E	I Escola Regional de Informática da Região Norte- ERIN 2009	\N	\N	20	2016-09-21 11:10:30.399	2009	f
431	E	World Conference on Computers and Education	1612	\N	20	2016-09-21 11:10:35.851	2009	f
435	E	Autonomous Agents and Multi-Agent Systems	8	\N	20	2016-09-21 11:11:12.23	2007	f
436	E	8th Conference on Intelligent Tutoring Systems	794	\N	20	2016-09-21 11:11:17.329	2006	f
437	E	IEEE International Conference on Advanced Learning Technologies	697	\N	20	2016-09-21 11:11:18.239	2006	f
440	E	2015 IEEE International Conference on Web Services (ICWS)	870	\N	20	2016-09-21 11:18:03.116	2015	f
442	E	IEEE International Conference on Web Services	870	\N	20	2016-09-21 11:20:02.861	2012	f
545	E	2014 IEEE 28th International Conference on Advanced Information Networking and Applications (AINA)	63	\N	20	2016-09-27 10:24:09.543	2014	f
553	E	the 50th Annual Design Automation Conference	345	\N	20	2016-09-27 10:26:02.775	2013	f
548	E	15th IEEE International Symposium on Object/component/service-oriented Real-time distributed Computing	988	\N	20	2016-09-27 10:24:56.001	2012	t
554	E	2013 IEEE 20th International Conference on Electronics, Circuits, and Systems (ICECS)	756	\N	20	2016-09-27 10:26:06.985	2013	f
564	E	International Conference Parallel and Distributed Processing Techniques and Applications	1260	\N	20	2016-09-27 10:27:02.978	2016	t
574	E	11th CONTECSI International Conference on Information Systems and Technology Management	310	\N	20	2016-09-27 10:29:13.212	2014	t
577	E	SBAC-PAD: Workshop de Iniciação Científica (WIC)	\N	\N	20	2016-09-27 10:29:26.791	2013	t
573	E	2014 IEEE 13th International Symposium on Network Computing and Applications (NCA)	1186	\N	20	2016-09-27 10:28:59.285	2014	f
575	E	2013 IEEE 22nd International Workshop On Enabling Technologies: Infrastructure For Collaborative Enterprises (WETICE)	1641	\N	20	2016-09-27 10:29:16.129	2013	f
609	E	27th International Conference on Computers and Their Applications - CATA	192	\N	20	2016-09-27 22:05:19.104	2012	t
610	E	IEEE INDIN2012 - IEEE International Conference on Industrial Informatics	919	\N	20	2016-09-27 22:05:21.672	2012	t
590	E	2015 IEEE International Conference on Fuzzy Systems (FUZZIEEE)	593	\N	20	2016-09-27 10:42:16.811	2015	f
591	E	2015 12th International Conference on Fuzzy Systems and Knowledge Discovery (FSKD)	585	\N	20	2016-09-27 10:42:22.057	2015	f
627	E	SBSI - Simpósio Brasileiro de Sistemas de Informação: MINICURSO	1383	\N	20	2016-09-27 22:07:20.754	2015	t
551	E	34th IEEE International Conference on Computer Design	718	\N	20	2016-09-27 10:25:45.495	2016	t
624	E	2015 Winter Simulation Conference (WSC)	1684	\N	20	2016-09-27 22:06:48.012	2015	f
595	E	Evolutionary Computation (CEC), 2012 IEEE Congress on ,	222	\N	20	2016-09-27 11:34:38.912	2012	t
569	E	2015 IEEE Symposium on Computers and Communication (ISCC)	958	\N	20	2016-09-27 10:27:54.642	2015	t
578	E	XIV Simpósio em Sistemas Computacionais - Workshop de Iniciação Científica	\N	\N	20	2016-09-27 10:29:35.102	2013	t
556	E	2012 IEEE 30th International Conference on Computer Design (ICCD 2012)	718	\N	20	2016-09-27 10:26:10.257	2012	f
558	E	XVI Escola Regional de Alto Desempenho (ERAD 2016)	488	\N	20	2016-09-27 10:26:36.554	2016	f
561	E	2016 IEEE 25th International Conference on Enabling Technologies: Infrastructure for Collaborative Enterprises (WETICE)	1641	\N	20	2016-09-27 10:26:44.802	2016	f
567	E	2015 Ninth International Conference on Complex, Intelligent, and Software Intensive Systems (CISIS)	259	\N	20	2016-09-27 10:27:25.818	2015	f
628	E	VIII CITENEL - Congresso de Inovação Tecnológica em Energia Elétrica	\N	\N	20	2016-09-27 22:07:27.977	2015	f
632	E	Congreso Internacional de Distribuición Electrica	\N	\N	20	2016-09-27 22:07:42.576	2014	f
644	E	Proceedings of the 17th International Workshop on Data Warehousing and OLAP	394	\N	20	2016-09-27 22:09:04.422	2014	t
642	E	Simpósio Brasileiro de Geoinformática	605	\N	20	2016-09-27 22:08:53.106	2014	f
646	E	XXIV Simpósio Brasileiro de Informática na Educação	1371	\N	20	2016-09-27 22:09:12.289	2013	t
656	E	2014 Ninth International Conference on Availability, Reliability and Security (ARES)	116	\N	20	2016-09-27 22:10:42.23	2014	t
665	E	Seventeenth Portuguese Conference on Artificial Intelligence (EPIA-2015)	484	\N	20	2016-09-27 22:11:32.79	2015	t
645	E	2013 IEEE International Symposium on Multimedia (ISM)	978	\N	20	2016-09-27 22:09:09.793	2013	f
654	E	2015 IEEE 13th International Conference on Industrial Informatics (INDIN)	919	\N	20	2016-09-27 22:10:32.52	2015	f
677	E	International Conference on Advances in Databases, Knowledge, and Data Applications	358	\N	20	2016-09-27 22:13:08.38	2014	t
635	E	CIbSE - Conferência Iberoamericana de Engenharia de Software	241	\N	20	2016-09-27 22:07:53.63	2012	t
647	E	32nd International Conference on Conceptual Modeling	487	\N	20	2016-09-27 22:09:14.855	2013	t
672	E	12 ICALT - International Conference on Advanced Learning Technologies	697	\N	20	2016-09-27 22:12:22.864	2012	f
655	E	II Workshop de Comunicação em Sistemas Embarcados Críticos (WoCCES)	\N	\N	20	2016-09-27 22:10:38.188	2014	t
659	E	2012 IEEE 17th Conference on Emerging Technologies &amp; Factory Automation (ETFA 2012)	508	\N	20	2016-09-27 22:10:51.04	2012	t
670	E	11th International Workshops of Practical Applications of Agents and Multi-Agent Systems	1238	\N	20	2016-09-27 22:12:16.924	2013	t
17	E	7th International Conference on Intelligent Text Processing and Computational Linguistics	244	\N	20	2016-09-19 22:02:35.899	2016	f
18	E	20th International Conference on Information Visualisation	1026	\N	20	2016-09-19 22:02:43.474	2016	f
21	E	EMBC'16 - 38th Annual International Conference of the IEEE Engineering in Medicine and Biology Society	466	\N	20	2016-09-19 22:04:25.526	2016	f
631	E	ISCA 29th International Conference on Computers and their Applications	192	\N	20	2016-09-27 22:07:37.938	2014	f
633	E	ICAISC - International Conference on Artificial Intelligence and Soft Computing	693	\N	20	2016-09-27 22:07:44.669	2014	f
637	E	21st IEEE Symposium on Computers and Communications	958	\N	20	2016-09-27 22:08:18.338	2016	f
643	E	Seminário Grandes Desafios da Computação no Brasil	\N	\N	20	2016-09-27 22:08:57.646	2014	f
648	E	XIII Brazilian Symposium on GeoInformatics (GeoInfo)	\N	\N	20	2016-09-27 22:09:22.51	2012	f
658	E	13ª Conferência sobre Redes de Computadores	\N	\N	20	2016-09-27 22:10:47.946	2013	f
667	E	XVI Encuentro Iberoamericano de Educación Superior a Distância	\N	\N	20	2016-09-27 22:11:53.341	2014	f
32	E	ICIP 2014 - 21st IEEE International Conference on Image Processing	785	\N	20	2016-09-19 22:07:55.459	2014	t
26	E	7° Congresso Brasileiro de Telemedicina e Telessaúde	\N	\N	20	2016-09-19 22:06:16.62	2015	f
29	E	CBMS 2014 - XXVII International Symposium on Computer-Based Medical Systems	201	\N	20	2016-09-19 22:06:48.741	2014	f
33	E	IEEE HealthCom 2014 - 16th International Conference on E-health Networking, Application & Services	\N	\N	20	2016-09-19 22:08:16.149	2014	f
44	E	23rd SIBGRAPI - Conference on Graphics, Patterns and Images, 2010	1431	\N	20	2016-09-19 22:09:50.463	2010	f
37	E	XVIII Jornada da Associação Brasileira de Radiologia Odontológica	\N	\N	20	2016-09-19 22:08:49.999	2012	f
31	E	SIBGRAPI 2014 - Conference on Patterns, Graphics and Images	1431	\N	20	2016-09-19 22:07:45.652	2014	t
50	E	SBQS - Simpósio Brasileiro de Qualidade de Software	1376	\N	20	2016-09-19 22:11:19.408	2009	t
54	E	SEC-08 - IEEE International Symposium on Scientific and Engineering Computing	\N	\N	20	2016-09-19 22:11:48.534	2008	t
22	E	SBSI 2016 - XII Simpósio Brasileiro de Sistemas de Informação	1383	\N	20	2016-09-19 22:05:07.68	2016	f
61	E	Twentieth IEEE International Symposium on ComputerBased Medical Systems	201	\N	20	2016-09-19 22:13:05.583	2007	t
34	E	CIARP 2014 ? 19th Iberoamerican Congress on Pattern Recognition	239	\N	20	2016-09-19 22:08:23.418	2014	t
36	E	COT 2013 - IV Computer on the Beach	\N	\N	20	2016-09-19 22:08:43.446	2013	f
47	E	2009 22nd IEEE International Symposium on ComputerBased Medical Systems (CBMS)	201	\N	20	2016-09-19 22:10:17.464	2009	t
59	E	20th IEEE International Symposium on COMPUTER-BASED MEDICAL SYSTEMS	201	\N	20	2016-09-19 22:12:59.049	2007	t
62	E	SIIM 2007 - Society for Imaging Informatics in Medicine Annual Meeting	\N	\N	20	2016-09-19 22:13:14.036	2007	f
58	E	CBIS 2008 - XI Congresso Brasileiro de Informática em Saúde	199	\N	20	2016-09-19 22:12:55.478	2008	f
46	E	COLIBRI 2009 - Colloquium of Computation: Brazil / INRIA, Cooperations, Advances and Challenges	\N	\N	20	2016-09-19 22:10:10.495	2009	f
19	E	VII COMPUTER ON THE BEACH - CotB 2016	\N	\N	20	2016-09-19 22:03:21.706	2016	f
23	E	WIM 2016 - XVI Workshop de Informática Médica	1650	\N	20	2016-09-19 22:05:17.969	2016	f
27	E	The 9th International Conference on Complex, Intelligent, and Software Intensive Systems (CISIS'15	259	\N	20	2016-09-19 22:06:19.34	2015	f
28	E	XVII Simposio Brasileiro de Sensoriamento Remoto	\N	\N	20	2016-09-19 22:06:24.07	2015	f
30	E	SET EXPO 2014 ? Broadcast and New Media Technology ? Trade Show and Conference,	\N	\N	20	2016-09-19 22:07:28.102	2014	f
45	E	COT 2010 - I Computer on the Beach	\N	\N	20	2016-09-19 22:09:52.187	2010	f
68	E	XII Simpósio Brasileiro de Sensoriamento Remoto - SBSR	\N	\N	20	2016-09-19 22:14:46.283	2005	f
73	E	CBMS 2002 - XV IEEE Symposium on Computer-Based Medical Systems	201	\N	20	2016-09-19 22:15:49.357	2002	f
56	E	XIV Simpósio Brasileiro de Sistemas Multimídia e Web (WebMedia'08)	1632	\N	20	2016-09-19 23:43:01.121	2008	f
82	E	Society for Neuroscience's 31st Annual Meeting	\N	\N	20	2016-09-19 22:16:52.71	2001	f
83	E	SCPDI - Simpósio Catarinense de Processamento Digital de Imagens	\N	\N	20	2016-09-19 22:17:10.08	2001	f
108	E	AATD´96 - II Workshop Automatische Analyse von Tomographie-Daten	\N	\N	20	2016-09-19 22:21:14.383	1996	f
110	E	AATD´95 - Workshop "Automatische Analyse von Tomographie-Daten"	\N	\N	20	2016-09-19 22:21:29.13	1995	f
115	E	1. Workshop on the "Brazilian-German Programme on Information Technology", GMD, Berlin, 1993	\N	\N	20	2016-09-19 22:22:14.65	1993	f
116	E	Proceedings do Workshop "Ähnlichkeit von Fällen"	\N	\N	20	2016-09-19 22:22:15.872	1992	f
117	E	GI Workshop Fälle in hybriden Systemen	\N	\N	20	2016-09-19 22:22:17.63	1992	f
118	E	14th IEEE/IFIP International Conference on Embedded and Ubiquitous Computing	512	\N	20	2016-09-19 23:34:37.959	2016	f
123	E	2014 IEEE Emerging Technology and Factory Automation (ETFA)	508	\N	20	2016-09-19 23:35:38.667	2014	f
129	E	2013 International Symposium on Rapid System Prototyping (RSP)	1327	\N	20	2016-09-19 23:36:52.196	2013	f
75	E	CBIS'2002 - VIII Congresso Brasileiro de Informática em Saúde	199	\N	20	2016-09-19 22:15:52.304	2002	t
76	E	WIM/2002 - II Workshop de Informática Médica	1650	\N	20	2016-09-19 22:15:54.273	2002	t
85	E	2000 International Conference on Mathematics and Engineering Techniques in Medicine and Biological Sciences - METMBS'2000	1114	\N	20	2016-09-19 22:17:30.275	2000	t
78	E	14th IEEE SYMPOSIUM ON COMPUTER BASED MEDICAL SYSTEMS - CBMS'2001	201	\N	20	2016-09-19 22:16:14.157	2001	t
119	E	21st IEEE International Conference on Emerging Technologies and Factory Automation	508	\N	20	2016-09-19 23:34:39.498	2016	t
128	E	Intel Embedded Research and Education Summit	\N	\N	20	2016-09-19 23:36:45.015	2013	t
205	E	III Escola Regional de Banco de Dados - III ERBD	\N	\N	20	2016-09-20 09:21:20.362	2007	f
208	E	III Workshop de Bibliotecas Digitais (em conjunto com WebMedia)	1626	\N	20	2016-09-20 09:22:21.981	2007	f
210	E	XXII Congresso Regional de Iniciação Científica e Tecnológica em Engenharia	\N	\N	20	2016-09-20 09:23:20.622	2007	f
212	E	II Escola Regional de Banco de Dados - II ERBD	\N	\N	20	2016-09-20 09:23:32.707	2006	f
213	E	II Workshop de Bibliotecas Digitais (WDL) - (Qualis Nacional C)	1626	\N	20	2016-09-20 09:23:34.58	2006	f
214	E	III Sessão de Demos do SBBD - Premiada com a 2a. Melhor Demo	\N	\N	20	2016-09-20 09:23:38.262	2006	f
217	E	6th ACM International Workshop on Web Information and Data Management (WIDM) - (Em 2004: Qualis Internacional A)	1648	\N	20	2016-09-20 09:23:48.272	2004	f
236	E	The 2014 International Conference on Security and Management	1348	\N	20	2016-09-20 10:34:52.415	2014	f
237	E	The Tenth International Conference on Networking and Services ICNS 2014	817	\N	20	2016-09-20 10:34:55.891	2014	f
240	E	2012 IEEE/IFIP Network Operations and Management Symposium (NOMS 2012)	1206	\N	20	2016-09-20 10:35:07.082	2012	f
245	E	4th International Workshop on Middleware for Grid Computing - MGC 2006. In conjunction with ACM/IFIP/USENIX 7th International Middleware Conference 2006	1119	\N	20	2016-09-20 10:36:33.522	2006	f
161	E	International Conference of the Chilean Computer Science Society	1389	\N	20	2016-10-02 15:40:28.809	2015	t
222	E	Workshop intelligent documents	\N	\N	20	2016-09-20 09:24:57.518	1999	t
232	E	Workshop de Trabalhos de Iniciação Científica e de Graduação (WTICG)	\N	\N	20	2016-09-20 10:33:12.973	2015	t
233	E	IEEE/IFIP Network Operations and Management Symposium	1206	\N	20	2016-09-20 10:33:35.607	2014	t
234	E	SECURWARE 2014, The Eighth International Conference on Emerging Security Information, Systems and Technologies	1412	\N	20	2016-09-20 10:34:49.049	2014	t
243	E	IFIP International Conference on New Technologies, Mobility and Security	\N	\N	20	2016-09-20 10:36:08.514	2008	t
249	E	I Simpósio Brasileiro de Sistemas de Informação	1383	\N	20	2016-09-20 10:37:20.75	2004	t
352	E	ICWMC 2007 - The Third International Conference on Wireless and Mobile Communications	868	\N	20	2016-09-21 10:34:43.223	2007	f
359	E	XXIV Simpósio Brasileiro de Redes de Computadores - XI Workshop de Gerência e Operação de Redes e Serviços	1644	\N	20	2016-09-21 10:35:12.448	2006	f
360	E	International Conference on Systems (ICONS'06)	824	\N	20	2016-09-21 10:35:15.633	2006	f
361	E	5Th Intenational Conference on Networks (ICN'06)	813	\N	20	2016-09-21 10:35:33.329	2006	f
376	E	XXIII Simpósio Brasileiro de Redes de Computadores	1377	\N	20	2016-09-21 10:39:58.423	2005	f
380	E	Congresso da SBC-2005. WPerformance-2005.	\N	\N	20	2016-09-21 10:41:17.537	2005	f
388	E	The 28th International Conference on Software Engineering and Knowledge Engineering	1415	\N	20	2016-09-21 10:44:33.581	2016	f
395	E	XIV Congresso Brasileiro de Informática em Saúde (CBIS)	199	\N	20	2016-09-21 10:52:08.591	2014	f
397	E	13th International Conference on Software Reuse (ICSR)	846	\N	20	2016-09-21 10:52:17.935	2013	f
398	E	14th IFIP Working Conference on Virtual Enterprise	1291	\N	20	2016-09-21 10:52:34.634	2013	f
408	E	International Conference on Software Engineering Advances (ICSEA)	838	\N	20	2016-09-21 10:54:01.132	2012	f
411	E	PROFES - 12th International Conference on Product Focused Software. Development and Process Improvement	1292	\N	20	2016-09-21 11:05:31.813	2011	f
413	E	25th Brazilian Symposium on Software Engineering (SBES)	1367	\N	20	2016-09-21 11:06:01.873	2011	f
350	E	Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos (SBRC 2008). VI Workshop de Computação em Grade e Aplicações (WCGA2008).	1377	\N	20	2016-09-21 10:34:36.581	2008	t
368	E	Seminario Integrado de Software e Hardware - Congresso da Sociedade Brasileira de Computação	1420	\N	20	2016-09-21 10:37:30.469	2011	t
377	E	Service Assurance with Partial and Intermittent Resources	1353	\N	20	2016-09-21 10:40:15.086	2005	t
379	E	Congresso da SBC-2005 -SEMISH-2005	1420	\N	20	2016-09-21 10:41:05.242	2005	t
393	E	10th International Conference on Software Engineering Advances (ICSEA 2015)	838	\N	20	2016-09-21 10:51:43.668	2015	t
396	E	Interaction South America	\N	\N	20	2016-09-21 10:52:39.149	2013	t
401	E	I Simpósio de Pesquisas em Games da UFSC	\N	\N	20	2016-09-21 10:53:26.549	2013	t
486	E	27th South Symposium on Microelectronics (SIM2012)	\N	\N	20	2016-09-22 15:19:03.602	2012	f
488	E	2016 IEEE INTERNATIONAL SYMPOSIUM ON CIRCUITS AND SYSTEMS (ISCAS2016)	957	\N	20	2016-09-24 18:32:14.947	2016	f
496	E	29th South Symposium on Microelectronics (SIM2014)	\N	\N	20	2016-09-24 18:32:50.612	2014	f
501	E	26th Symposium on Integrated Circuits and Systems Design (SBCCI2013)	1364	\N	20	2016-09-24 18:33:17.937	2013	f
527	E	EMBC'16 - 38th Annual International Conference of the IEEE Engineering in Medicine and Biology Society	466	\N	9	2016-09-26 18:27:49.08	2016	f
528	E	SBSI 2016 - XII Simpósio Brasileiro de Sistemas de Informação	1383	\N	9	2016-09-26 18:27:56.944	2016	f
532	E	2015 IEEE 24th International Conference on Enabling Technologies: Infrastructure for Collaborative Enterprises (WETICE)	1641	\N	20	2016-09-27 10:18:53.182	2015	f
534	E	2014 44th Annual IEEE/IFIP International Conference on Dependable Systems and Networks (DSN)	403	\N	20	2016-09-27 10:19:41.485	2014	f
539	E	2014 21st International Conference on High Performance Computing (HiPC)	641	\N	20	2016-09-27 10:20:36.384	2014	f
544	E	30th IEEE International Conference on Advanced Information Networking and Applications	63	\N	20	2016-09-27 10:24:07.907	2016	f
546	E	IV Brazilian Symposium on Computing Systems Engineering	1368	\N	20	2016-09-27 10:24:29.147	2014	f
549	E	CLEI 2012 - XXXVIII Conferência LatinoAmericana em Informática	271	\N	20	2016-09-27 10:24:59.205	2012	f
552	E	Design Automation and Test in Europe	354	\N	20	2016-09-27 10:25:57.031	2013	f
555	E	2012 Design, Automation &amp; Test in Europe Conference &amp; Exhibition (DATE 2012)	354	\N	20	2016-09-27 10:26:08.775	2012	f
485	E	European PKI Workshop: Theory and Practice (EuroPKI'07)	\N	\N	20	2016-09-21 11:29:18.524	2007	t
492	E	2015 IEEE 6th Latin American Symposium on Circuits &amp; Systems (LASCAS 2015)	1070	\N	20	2016-09-24 18:32:35.64	2015	t
495	E	2014 IEEE 5th Latin American Symposium on Circuits and Systems (LASCAS)	1070	\N	20	2016-09-24 18:32:44.518	2014	t
497	E	ACM/IEEE DESIGN, AUTOMATION & TEST IN EUROPE (DATE2013)	354	\N	20	2016-09-24 18:33:10.724	2013	t
562	E	CSBC 2016 - BreSci - 10º Brazilian e-Science Workshop	412	\N	20	2016-09-27 10:26:55.534	2016	t
563	E	CSBC - WIM - 16º Workshop de Informática Médica	1650	\N	20	2016-09-27 10:26:58.93	2016	t
565	E	2016 IEEE 29th International Symposium on ComputerBased Medical Systems (CBMS)	201	\N	20	2016-09-27 10:27:05.486	2016	t
568	E	2015 IEEE 14th International Symposium on Network Computing and Applications (NCA)	1186	\N	20	2016-09-27 10:27:53.266	2015	t
649	E	14th IEEE International Conference on Industrial Informatics (INDIN)	919	\N	20	2016-09-27 22:09:43.12	2016	f
650	E	29th International Symposium on Computer-Based Medical Systems (CBMS)	201	\N	20	2016-09-27 22:09:45.256	2016	f
651	E	Simpósio Brasileiro de Redes de Computadores e Sistemas Distribuídos (SBRC)	1377	\N	20	2016-09-27 22:09:46.744	2016	f
662	E	2015 Fourteenth Mexican International Conference on Artificial Intelligence (MICAI)	1121	\N	20	2016-09-27 22:11:20.679	2015	f
638	E	XVII Intl. Conf. on Big Data Analytics and Knowledge Discovery	\N	\N	20	2016-09-27 22:08:32.153	2015	t
664	E	CAVA 2015 - VII Congresso Internacional de Ambientes Virtuais de Aprendizagem Adaptativos e Acessivos	\N	\N	20	2016-09-27 22:11:27.121	2015	f
653	E	2015 IEEE World Conference on Factory Communication Systems (WFCS)	\N	\N	20	2016-09-27 22:10:29.829	2015	t
666	E	II Congreso Internacional de Ingeniería Informática y Sistemas de Información	\N	\N	20	2016-09-27 22:11:47.614	2014	t
669	E	11th International Workshops of PAAMS - Practical Applications of Agents and Multi-Agent Systems	1238	\N	20	2016-09-27 22:12:14.214	2013	t
682	E	International Conference on Systems and Networks Communications (ICSNC 2013)	841	\N	20	2016-09-27 22:14:09.126	2013	t
684	E	SERP'13 - The 2013 International Conference on Software Engineering Research and Practice	1424	\N	20	2016-09-27 22:14:29.909	2013	t
686	E	XIII Workshop de Ferramentas e Aplicações (WFA)	\N	\N	20	2016-09-27 22:14:55.905	2014	t
671	E	European Workshop on Multi-Agent Systems	513	\N	20	2016-09-27 22:12:18.215	2013	f
680	E	The Fifth International Conference on eHealth, Telemedicine, and Social Medicine	507	\N	20	2016-09-27 22:13:50.707	2013	f
\.


--
-- PostgreSQL database dump complete
--
