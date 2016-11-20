'use strict';

angular.module('silq2App')
    .directive('chartPublicacoesPorAno', function() {
        return {
            restrict: 'E',
            scope: {
                stats: '=?stats'
            },
            templateUrl: 'scripts/components/charts/chart-publicacoes-por-ano.html',
            link: function($scope) {
                $scope.agrupador = 'todos';
                $scope.grupoConceitual = 'A1,A2,B1';
                $scope.chart = {};

                var reload = function() {
                    var stats = $scope.stats;
                    var artigos = stats.publicacoesPorAno.artigos;
                    var trabalhos = stats.publicacoesPorAno.trabalhos;
                    var grupos = $scope.grupoConceitual.split(',');

                    var chart = {};
                    chart.series = grupos;
                    chart.labels = [];
                    chart.data = [];

                    for (var j in chart.series) {
                        chart.data.push([]);
                    }

                    for (var ano = stats.anoPrimeiraPublicacao; ano <= stats.anoUltimaPublicacao; ano++) {
                        chart.labels.push(ano + '');

                        for (var i in chart.series) {
                            var serie = chart.series[i];

                            var countArtigos = parseInt(artigos[ano] && artigos[ano][serie] ? artigos[ano][serie] : 0);
                            var countTrabalhos = parseInt(trabalhos[ano] && trabalhos[ano][serie] ? trabalhos[ano][serie] : 0);

                            if ($scope.agrupador === 'todos') {
                                chart.data[i].push(countArtigos + countTrabalhos);
                            } else if ($scope.agrupador === 'artigos') {
                                chart.data[i].push(countArtigos);
                            } else if ($scope.agrupador === 'trabalhos') {
                                chart.data[i].push(countTrabalhos);
                            } else {
                                chart.data[i].push(0);
                            }
                        }
                    }

                    $scope.chart = chart;
                };

                // Se 'stats' for modificado, atualiza o gráfico:
                $scope.$watch('stats', function(value) {
                    $scope.stats = value;
                    if ($scope.stats) {
                        reload();
                    }
                });

                $scope.changeGrupoConceitual = function() {
                    reload();
                };

                $scope.changeAgrupador = function() {
                    reload();
                };
            }
        };
    });
