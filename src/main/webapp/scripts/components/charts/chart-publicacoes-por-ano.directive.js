'use strict';

angular.module('silq2App')
    .directive('chartPublicacoesPorAno', function() {
        return {
            restrict: 'E',
            scope: {
                stats: '=?stats',
                evaluationSystem: '=evaluationSystem'
            },
            templateUrl: 'scripts/components/charts/chart-publicacoes-por-ano.html',
            link: function($scope) {
                $scope.evaluationSystem = 'QUALIS';
                $scope.tipoGrafico = 'LINHA';
                $scope.agrupador = [
                    { name: 'Artigos', selected: true },
                    { name: 'Trabalhos', selected: true },
                    { name: 'Resumos', selected: true }
                ];
                $scope.grupos = [
                    { name: 'A1', selected: true },
                    { name: 'A2', selected: true },
                    { name: 'B1', selected: true },
                    { name: 'B2', selected: false },
                    { name: 'B3', selected: false },
                    { name: 'B4', selected: false },
                    { name: 'B5', selected: false },
                    { name: 'C',  selected: false },
                    { name: 'sem-conceito',  selected: false }
                ];
                $scope.chart = {};

                var reload = $scope.reload = function() {
                    var stats = $scope.stats;
                    if(typeof stats === 'undefined'){
                        return;
                    }
                    var evaluationSystem = $scope.evaluationSystem;
                    var artigos = evaluationSystem === 'QUALIS' ? stats.publicacoesPorAno.artigos : stats.publicacoesPorAno.artigosSICLAP;
                    var trabalhos = evaluationSystem === 'QUALIS' ? stats.publicacoesPorAno.trabalhos : stats.publicacoesPorAno.trabalhosSICLAP;
                    var resumos = evaluationSystem === 'QUALIS' ? stats.publicacoesPorAno.resumos : stats.publicacoesPorAno.resumosSICLAP;
                    var grupos = $scope.grupos
                        .filter(function(g) { return g.selected; })
                        .map(function(g) { return g.name; });

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
                            var countResumos = parseInt(resumos[ano] && resumos[ano][serie] ? resumos[ano][serie] : 0);

                            var count = 0;
                            for (var j in $scope.agrupador) {
                                var a = $scope.agrupador[j];
                                if (a.selected && a.name === 'Artigos') {
                                    count += countArtigos;
                                }
                                if (a.selected && a.name === 'Trabalhos') {
                                    count += countTrabalhos;
                                }
                                if (a.selected && a.name === 'Resumos') {
                                    count += countResumos;
                                }
                            }
                            chart.data[i].push(count);
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

                $scope.$watch('evaluationSystem', function(value) {
                    reload();
                });
            }
        };
    });
