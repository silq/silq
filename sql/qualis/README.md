# Qualis 2010-2014

Arquivos Qualis baixados de https://sucupira.capes.gov.br/sucupira/public/consultas/coleta/veiculoPublicacaoQualis/listaConsultaGeralPeriodicos.jsf na data de 22 de abril de 2016 e mantidos em `originais.tar.gz`.

Os arquivos deste diretório foram manipulados a partir dos originais para correções de erros e padronizações:

* Títulos da linha de cabeçalho alterados para 'co_issn	no_titulo	no_area_avaliacao	no_estrato'

* Find and replace '&amp;' por '&'

* Find por caracteres do tipo '&#' e substituição pelo símbolo correspondente
	Exemplo: '&#351;' por 'ş'

* Algumas avaliações possuíam ISSNs errôneos ou fora do padrão:
	Foram encontrados utilizando Find com a expressão regular `^(?!....-....).*$`
	(procura por tudo que não está no formado `9999-9999`)

	- ISSNs sem dígito separador, ex.: 10002334
		Expressão regular de busca: `^(\w\w\w\w)(\w\w\w\w)$`
		Expressão regular de substituição: `$1-$2`
		Tiveram o separador incluído -> '1000-2334'

	- ISSNs com números faltando, ex.: 0034-167 (Revista Brasileira de Enfermagem)
		Foram procurados por título no Google e tiveram seus ISSNs arrumados de forma manual.
		Em algumas ocasiões o número de ISSN resultante tornava a tupla exatamente igual a outra (mesmo conceito). Nestes casos, uma das tuplas foi removida.

# Qualis 2015

Baixado de https://sucupira.capes.gov.br/sucupira/public/consultas/coleta/veiculoPublicacaoQualis/listaConsultaGeralPeriodicos.jsf na data de 21 de abril de 2017 e adicionado a `originais.tar.gz`.

Passaram pelo mesmo processo de normalização de dados dos Qualis 2010-2014 descritos acima, além dos segintes:

* A área de avaliação dos Qualis 2010-2014 'ADMINISTRAÇÃO, CIÊNCIAS CONTÁBEIS E TURISMO' é apresentado na tabela Qualis de 2015 como 'ADMINISTRAÇÃO PÚBLICA E DE EMPRESAS, CIÊNCIAS CONTÁBEIS E TURISMO'. Foi realizado um find & replace na tabela para alterar o nome da área para 'ADMINISTRAÇÃO, CIÊNCIAS CONTÁBEIS E TURISMO'.

* A área de avaliação dos Qualis 2010-2014 'ARQUITETURA E URBANISMO' foi alterada para 'ARQUITETURA, URBANISMO E DESIGN' no Qualis 2015. Foi realizada uma atualização dos registros Qualis 2010-2014 para a nova nomenclatura 'ARQUITETURA, URBANISMO E DESIGN':

```
update tb_qualis_periodico set no_area_avaliacao = 'ARQUITETURA, URBANISMO E DESIGN' where no_area_avaliacao = 'ARQUITETURA E URBANISMO';
-- 2153 registros atualizados
```

* A área 'COMUNICAÇÃO E INFORMAÇÃO' de 2015 não existia nos Qualis anteriores. Foi adicionada.

* As áreas 'CIÊNCIAS SOCIAIS APLICADAS I' e 'EDUCAÇÃO' constam nos Qualis 2010-2014, porém não constam em 2015. Foram mantidas.

* As áreas 'FILOSOFIA/TEOLOGIA:subcomissão FILOSOFIA' e 'FILOSOFIA/TEOLOGIA:subcomissão TEOLOGIA' passaram a ser nomeadas 'FILOSOFIA' e 'TEOLOGIA', respectivamente, no Qualis 2015. A mudança foi realizada para os registros antigos:

update tb_qualis_periodico set no_area_avaliacao = 'FILOSOFIA' where no_area_avaliacao = 'FILOSOFIA/TEOLOGIA:subcomissão FILOSOFIA';
-- 4044 registros atualizados

update tb_qualis_periodico set no_area_avaliacao = 'TEOLOGIA' where no_area_avaliacao = 'FILOSOFIA/TEOLOGIA:subcomissão TEOLOGIA';
-- 1766 registros atualizados

# Qualis conferência 2013-2015

* Títulos da linha de cabeçalho alterados para os nomes correspondentes das colunas da tabela `tb_qualis_evento` da base de dados. As colunas desnecessárias foram removidas.

* Remoção da sigla do início do nome do evento utilizando a expressão regular `^(.+) - (.*)$`.

* Os registros foram inseridos com o ano base de 2015 através do arquivo `QualisConferencias2013-2015.csv` e copiados para os anos base 2013 e 2014.
