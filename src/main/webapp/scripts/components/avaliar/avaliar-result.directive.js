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
                    $scope.labels = [];
                    $scope.data = [[]];

                    for (var i in $scope.results.publicacoesPorAno) {
                        $scope.labels.push(i);
                        $scope.data[0].push($scope.results.publicacoesPorAno[i]);
                    }
                };
            }
        };
    });
