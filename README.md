# SILQ 2

[![Build Status](https://travis-ci.com/CarlosBonetti/silq2.svg?token=TncWKXR1N9y1CQNXWyip&branch=master)](https://travis-ci.com/CarlosBonetti/silq2)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/dd5df6c7ef01470da3e2c2b5a626eff0)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CarlosBonetti/silq2&amp;utm_campaign=Badge_Grade)

A base desta aplicação foi gerada utilizando JHipster, você pode encontrar documentação a respeito em  [https://jhipster.github.io](https://jhipster.github.io).

## Dependências

Antes de poder construir esta aplicação, instale e configure em sua máquina:

1. [Node.js][]: Utilizamos para rodar o servidor de desenvolvimento e para construir o front-end do projeto. Requere versão 4 ou superior.

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
$ mvn clean install
```

## Construindo a aplicação

Rode os seguintes comandos em dois terminais separados para criar uma experiência de desenvolvimento mais agradável:

```sh
$ mvn
$ grunt
```

[Bower][] é utilizado para gerenciar as dependências CSS e Javascript. Atualize ou adicione dependências no arquivo `bower.json`. Rode `bower install` ou `bower update` para gerenciar as dependências.

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

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Grunt]: http://gruntjs.com/
