'use strict';

angular.module('silq2App')
    .directive('chartPublicacoesPorAno', function(Upload, Flash) {
        return {
            restrict: 'E',
            scope: {
                stats: '=?stats'
            },
            templateUrl: 'scripts/components/charts/chart-publicacoes-por-ano.html',
            link: function($scope) {
                $scope.agrupador = 'todos';
                $scope.grupoConceitual = 'A1,A2,B1';

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

                var reload = function() {
                    var stats = $scope.stats;
                    var artigos = stats.publicacoesPorAno.artigos;
                    var trabalhos = stats.publicacoesPorAno.trabalhos;
                    var grupos = $scope.grupoConceitual.split(',');

                    $scope.series = grupos;
                    $scope.labels = [];
                    $scope.data = [];
                    for (var i in $scope.series) {
                        $scope.data.push([]);
                    }

                    for (var ano = stats.anoPrimeiraPublicacao; ano <= stats.anoUltimaPublicacao; ano++) {
                        $scope.labels.push(ano);

                        for (var i in $scope.series) {
                            var serie = $scope.series[i];

                            var countArtigos = parseInt(artigos[ano] && artigos[ano][serie] ? artigos[ano][serie] : 0);
                            var countTrabalhos = parseInt(trabalhos[ano] && trabalhos[ano][serie] ? trabalhos[ano][serie] : 0);

                            if ($scope.agrupador === 'todos') {
                                $scope.data[i].push(countArtigos + countTrabalhos);
                            } else if ($scope.agrupador === 'artigos') {
                                $scope.data[i].push(countArtigos);
                            } else if ($scope.agrupador === 'trabalhos') {
                                $scope.data[i].push(countTrabalhos);
                            } else {
                                $scope.data[i].push(0);
                            }
                        }
                    }
                };
            }
        };
    });
