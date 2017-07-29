# SILQ

[![Creative Commons Attribution-NonCommercial 4.0 International License.](https://i.creativecommons.org/l/by-nc/4.0/88x31.png)](http://creativecommons.org/licenses/by-nc/4.0/)
[![Build Status](https://travis-ci.org/silq/silq.svg?branch=master)](https://travis-ci.com/silq/silq)

Sistema de Integração Lattes-Qualis: http://silq.inf.ufsc.br.

## Dependências

Antes de poder construir esta aplicação, instale e configure em sua máquina:

1. Java 8
1. [Maven][]: Para gerenciamento das dependências Java;
1. [Node.js][]: Utilizamos para rodar o servidor de desenvolvimento e para construir o front-end do projeto;
1. [Docker][]: Para gerenciamento das imagens das bases de dados de desenvolvimento e teste.

Após instalar o node, instale as dependências front-end da aplicação com:

```sh
$ npm install
```

Também utilizamos [Grunt][] como build system. Instale com:

```sh
$ npm install -g grunt-cli
```

Para instalar as dependências maven, rode em um terminal:

```sh
$ mvn clean install -DskipTests
```

## Banco de dados

Utilizamos docker para gerenciamento dos bancos de dados de desenvolvimento e teste. Utilize `docker-compose` (pode ser necessário instalá-lo) para criar as bases de dados de desenvolvimento e de testes com o comando `docker-compose -f docker-compose.yml up -d`.

As credenciais de conexão com o banco estão no arquivo `docker-compose.yml`.

### Criação do esquema e dados iniciais

Execute o arquivo `sql/schema.sql`. Posteriormente, execute `sql/qualis_evento.sql` e `sql/qualis_periodico.sql` para inserção dos registros Qualis.

Os seguintes comandos podem ser utilizados para tanto:

```
psql -h 0.0.0.0 -p 5432 -U postgres -d silq2 -f sql/schema.sql
psql -h 0.0.0.0 -p 5432 -U postgres -d silq2 -f sql/qualis_evento.sql
psql -h 0.0.0.0 -p 5432 -U postgres -d silq2 -f sql/qualis_periodico.sql
```

Também é necessário executar estes scripts para a base de testes.

## Construindo a aplicação

Rode os seguintes comandos em dois terminais separados para criar uma experiência de desenvolvimento mais agradável:

```sh
$ mvn
$ grunt
```

[Bower][] é utilizado para gerenciar as dependências CSS e Javascript. Atualize ou adicione dependências no arquivo `bower.json`. Rode `bower install` ou `bower update` para gerenciar as dependências.

## Testes automatizados

Com o banco de testes iniciados, rode no terminal:

```
$ mvn clean install
```

O comando irá executar os testes unitários do back-end.

Para rodar os testes end-to-end (frontend), execute (com a aplicação iniciada):

```
$ npm test
```

## Construindo para produção

Para criar uma versão otimizada para produção, utilize:

```sh
$ mvn -Pprod clean package
```

Isto irá concatenar e minificar os arquivos CSS e Javascript, além de modificar `index.html` com as novas referências a estes arquivos.

Para rodar a versão _standalone_ do projeto, utilizando um servidor Tomcat e ativando o perfil de produção, utilize:

```sh
$ java -jar target/*.war --spring.profiles.active=prod
```

Então navegue para [http://localhost:8080](http://localhost:8080) in em seu navegador.

*A base desta aplicação foi gerada utilizando JHipster, você pode encontrar documentação a respeito em  [https://jhipster.github.io](https://jhipster.github.io).*

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
[Maven]: https://maven.apache.org/
[Docker]: https://www.docker.com/
