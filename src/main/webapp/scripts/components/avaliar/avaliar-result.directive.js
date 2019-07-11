'use strict';

angular.module('silq2App')
    .directive('avaliarResult', function($state) {
        return {
            restrict: 'E',
            scope: {
                results: '=results'
            },
            templateUrl: 'scripts/components/avaliar/avaliar-result.html',
            link: function($scope) {
                $scope.evaluationSystem = 'QUALIS';
                $scope.needRefresh = false;

                $scope.$on('silq:feedback', function() {
                    $scope.needRefresh = true;
                });

                $scope.refreshResults = function() {
                     $state.reload();
                };

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
                    var trabalhos = $.grep( $scope.results.trabalhos, function( n ) { return n.obj.natureza == 'Completo' });
                    return createShallowResults(trabalhos);
                };

                $scope.getResumosCSV = function() {
                    var resumos = $.grep( $scope.results.trabalhos, function( n ) { return n.obj.natureza != 'Completo' });
                    return createShallowResults(resumos);
                };
            }
        };
    });
