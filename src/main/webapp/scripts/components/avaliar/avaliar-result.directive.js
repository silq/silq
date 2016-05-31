'use strict';

angular.module('silq2App')
    .directive('avaliarResult', function() {
        return {
            restrict: 'E',
            scope: {
                results: '=results'
            },
            templateUrl: 'scripts/components/avaliar/avaliar-result.html',
            link: function($scope) {
                var createShallowResults = function(results) {
                    var arr = angular.copy(results);
                    arr.forEach(function(item) {
                        var conceito = item.conceitos[0];
                        if (conceito) {
                            item.conceitoEstrato = conceito.conceito;
                            item.conceitoSimilaridade = conceito.similaridade;
                            item.conceitoTitulo = conceito.tituloVeiculo;
                            item.conceitoAno = conceito.ano;
                        }
                    });
                    return arr;
                };

                $scope.getArtigosCSV = function() {
                    return createShallowResults($scope.results.artigos);
                };

                $scope.getTrabalhosCSV = function() {
                    return createShallowResults($scope.results.trabalhos);
                };

                $scope.loadGraficos = function() {
                    var stats = $scope.results.stats;
                    var artigos = stats.publicacoesPorAno.artigos;
                    var trabalhos = stats.publicacoesPorAno.trabalhos;

                    $scope.series = ['Artigos', 'Trabalhos'];
                    $scope.labels = [];
                    $scope.data = [[], []];

                    for (var ano = stats.anoPrimeiraPublicacao; ano <= stats.anoUltimaPublicacao; ano++) {
                        $scope.labels.push(ano);

                        var countArtigos = parseInt(artigos[ano] ? artigos[ano].total : 0);
                        var countTrabalhos = parseInt(trabalhos[ano] ? trabalhos[ano].total : 0);

                        $scope.data[0].push(countArtigos);
                        $scope.data[1].push(countTrabalhos);
                    }
                };
            }
        };
    });
