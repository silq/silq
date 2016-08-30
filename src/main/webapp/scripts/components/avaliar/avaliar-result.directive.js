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
                    var arr = []; // Array de linhas da tabela
                    results.forEach(function(item) {
                        var r = angular.copy(item.obj);
                        var conceito = item.conceitos[0];
                        if (conceito) {
                            r.conceitoEstrato = conceito.conceito;
                            r.conceitoSimilaridade = conceito.similaridade;
                            r.conceitoTitulo = conceito.tituloVeiculo;
                            r.conceitoAno = conceito.ano;
                        }
                        arr.push(r);
                    });
                    return arr;
                };

                $scope.getArtigosCSV = function() {
                    return createShallowResults($scope.results.artigos);
                };

                $scope.getTrabalhosCSV = function() {
                    return createShallowResults($scope.results.trabalhos);
                };
            }
        };
    });
