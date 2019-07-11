'use strict';

angular.module('silq2App')
    .directive('classificacaoTable', function() {
        return {
            restrict: 'E',
            scope: {
                avaliacoes: '=?avaliacoes'
            },
            templateUrl: 'scripts/components/classificacao/classificacao-table.html',
            link: function($scope) {
                $scope.currentYear = new Date().getFullYear();
                $scope.orderByField = 'nome';
                $scope.reverseSort = false;
                $scope.classificacoes = [];
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
                    var anoLimite = new Date().getFullYear()-4;
                    var nota = 0;
                    for(var index in artigos) {
                        if(Number(index) >= anoLimite){
                            var conceitos = artigos[index];
                            for(var conceito in conceitos) {
                                nota = nota +  (CONCEITO_NOTA_ENUM[conceito] * conceitos[conceito])
                            }
                        }
                    }
                    for(var index in trabalhos) {
                        if(Number(index) >= anoLimite){
                            var conceitos = trabalhos[index];
                            for(var conceito in conceitos) {
                                nota = nota + (CONCEITO_NOTA_ENUM[conceito] * conceitos[conceito])
                            }
                        }
                    }
                    return nota;
                };

                $scope.obterClassificacao = function(artigos, trabalhos) {
                    var nota = $scope.obterPontuacao(artigos, trabalhos);
                    var quantidadeArtigos = 0;
                    var anoLimite = new Date().getFullYear()-4;
                    debugger;
                    for(var index in artigos) {
                        if(Number(index) >= anoLimite){
                            quantidadeArtigos = quantidadeArtigos + artigos[index]['total']
                        }
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

                $scope.$watch('avaliacoes', function(value) {
                    $scope.avaliacoes = value;
                    if ($scope.avaliacoes) {

                        var avaliacoes = $scope.avaliacoes;
                        avaliacoes.forEach(function(element){
                            $scope.classificacoes.push({
                                nome: element.dadosGerais.nome,
                                pontuacao: $scope.obterPontuacao(element.stats.publicacoesPorAno.artigosSICLAP, element.stats.publicacoesPorAno.trabalhosSICLAP),
                                classificacao: $scope.obterClassificacao(element.stats.publicacoesPorAno.artigosSICLAP, element.stats.publicacoesPorAno.trabalhosSICLAP),
                                primeiroAno: {
                                    trabalhos: $scope.obterResultado(element.stats.publicacoesPorAno.trabalhosSICLAP[$scope.currentYear-4]),
                                    artigos: $scope.obterResultado(element.stats.publicacoesPorAno.artigosSICLAP[$scope.currentYear-4])
                                },
                                segundoAno: {
                                    trabalhos: $scope.obterResultado(element.stats.publicacoesPorAno.trabalhosSICLAP[$scope.currentYear-3]),
                                    artigos: $scope.obterResultado(element.stats.publicacoesPorAno.artigosSICLAP[$scope.currentYear-3])
                                },
                                terceiroAno: {
                                    trabalhos: $scope.obterResultado(element.stats.publicacoesPorAno.trabalhosSICLAP[$scope.currentYear-2]),
                                    artigos: $scope.obterResultado(element.stats.publicacoesPorAno.artigosSICLAP[$scope.currentYear-2])
                                },
                                quartoAno: {
                                    trabalhos: $scope.obterResultado(element.stats.publicacoesPorAno.trabalhosSICLAP[$scope.currentYear-1]),
                                    artigos: $scope.obterResultado(element.stats.publicacoesPorAno.artigosSICLAP[$scope.currentYear-1])
                                }
                            })
                        });
                    }
                });
            }
        };
    });
