'use strict';

angular.module('silq2App')
    .directive('classificacaoTable', function() {
        return {
            restrict: 'E',
            scope: {
                avaliacoes: '=?avaliacoes',
                periodo: '=?periodo'
            },
            templateUrl: 'scripts/components/classificacao/classificacao-table.html',
            link: function($scope) {
                $scope.currentYear = new Date().getFullYear();
                $scope.orderByField = 'nome';
                $scope.reverseSort = false;
                $scope.classificacoes = [];
                $scope.years = [];
                $scope.obterPontuacao = function(artigos, trabalhos) {
                    var CONCEITO_NOTA_ENUM = {
                        'A1': 100,
                        'A2': 85,
                        'B1': 70,
                        'B2': 0,
                        'B3': 0,
                        'B4': 0,
                        'B5': 0,
                        'C': 0,
                        'sem-conceito': 0,
                        'total': 0
                    };
                    var nota = 0;
                    for(var index in artigos) {
                        var conceitos = artigos[index];
                        for(var conceito in conceitos) {
                            nota = nota +  (CONCEITO_NOTA_ENUM[conceito] * conceitos[conceito])
                        }
                    }
                    for(var index in trabalhos) {
                        var conceitos = trabalhos[index];
                        for(var conceito in conceitos) {
                            nota = nota + (CONCEITO_NOTA_ENUM[conceito] * conceitos[conceito])
                        }
                    }
                    return nota;
                };

                $scope.totalizador = function(classificacoes){
                    var totalizador = 0;
                    classificacoes.forEach(function(classificacao){
                        totalizador += classificacao.pontuacao
                    });
                    return totalizador
                };

                $scope.obterClassificacao = function(artigos, trabalhos) {
                    var nota = $scope.obterPontuacao(artigos, trabalhos);
                    var quantidadeArtigos = 0;
                    for(var index in artigos) {
                        quantidadeArtigos = quantidadeArtigos + artigos[index]['total']
                    }
                    if(nota >= 200 && quantidadeArtigos >= 3){
                        return 'GRUPO I'
                    }else if(nota >= 200 && quantidadeArtigos >= 2){
                        return 'GRUPO II'
                    }else if(nota >= 200 && quantidadeArtigos >= 1){
                        return 'GRUPO III'
                    }else{
                        return 'SEM GRUPO'
                    }
                };

                $scope.obterResultado = function(trabalho) {
                    if(trabalho){
                        var a1 = "A1" in trabalho ? trabalho.A1 : 0;
                        var a2 = "A2" in trabalho ? trabalho.A2 : 0;
                        var b1 = "B1" in trabalho ? trabalho.B1 : 0;
                        return {
                            a1: a1,
                            a2: a2,
                            b1: b1
                        }
                    }
                    return {
                        a1: 0,
                        a2: 0,
                        b1: 0
                    }
                };

                var reload = $scope.reload = function() {
                    var avaliacoes = $scope.avaliacoes;
                    var inicio = $scope.periodo.inicio
                    var fim = $scope.periodo.fim

                    var artigosSiclap = avaliacoes.map(function(avaliacao) { return avaliacao.stats.publicacoesPorAno.artigosSICLAP });
                    var trabalhosSiclap = avaliacoes.map(function(avaliacao) { return avaliacao.stats.publicacoesPorAno.trabalhosSICLAP });

                    if(!inicio && !fim){
                        _.each(artigosSiclap, function(obj){
                            _.each(_.keys(obj), function(k){
                                if(_.has(obj[k],"A1") || _.has(obj[k],"A2") || _.has(obj[k],"B1") ){
                                    if(inicio > Number(k) || !inicio){
                                        inicio = Number(k)
                                    }
                                    if(fim < Number(k) || !fim){
                                        fim = Number(k)
                                    }
                                }
                            })
                        });
                        _.each(trabalhosSiclap, function(obj){
                            _.each(_.keys(obj), function(k){
                                if(_.has(obj[k],"A1") || _.has(obj[k],"A2") || _.has(obj[k],"B1") ){
                                    if(inicio > Number(k) || !inicio){
                                        inicio = Number(k)
                                    }
                                    if(fim < Number(k) || !fim){
                                        fim = Number(k)
                                    }
                                }
                            })
                        })
                    }

                    for (var i = inicio; i <= fim; i++) {
                        $scope.years.push(i)
                    }

                    avaliacoes.forEach(function(element){
                        var avaliacao = [];
                        $scope.years.forEach(function(year){
                            avaliacao.push({
                                ano: year,
                                trabalhos: $scope.obterResultado(element.stats.publicacoesPorAno.trabalhosSICLAP[year]),
                                artigos: $scope.obterResultado(element.stats.publicacoesPorAno.artigosSICLAP[year])
                            })
                        });

                        $scope.classificacoes.push({
                            nome: element.dadosGerais.nome,
                            pontuacao: $scope.obterPontuacao(element.stats.publicacoesPorAno.artigosSICLAP, element.stats.publicacoesPorAno.trabalhosSICLAP),
                            classificacao: $scope.obterClassificacao(element.stats.publicacoesPorAno.artigosSICLAP, element.stats.publicacoesPorAno.trabalhosSICLAP),
                            avaliacao: avaliacao
                        })
                    });
                    console.log($scope.classificacoes)
                };

                $scope.$watch('avaliacoes', function(value) {
                    $scope.avaliacoes = value;
                    if ($scope.avaliacoes) {
                        reload()
                    }
                });
            }
        };
    });
