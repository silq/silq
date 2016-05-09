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
