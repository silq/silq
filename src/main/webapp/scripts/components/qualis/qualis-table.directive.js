'use strict';

angular.module('silq2App')
    .directive('qualisTable', function(Qualis, $timeout) {
        return {
            restrict: 'E',
            scope: {
                /**
                 * Define se a tabela deve pesquisar somente por 'periodicos',
                 * 'eventos' ou ambos.
                 */
                mode: '=?',

                /**
                 * Query inicial.
                 */
                query: '=?',

                /**
                 * Função a ser executada ao selecionar um resultado.
                 * Recebe o item selecionado como parâmetro.
                 */
                select: '&'
            },
            templateUrl: 'scripts/components/qualis/qualis-table.html',
            link: function($scope) {
                $scope.query = angular.isDefined($scope.query) ? $scope.query : '';
                $scope.mode = angular.isDefined($scope.mode) ? $scope.mode : null;
                $scope.tipo = $scope.mode || 'periodicos';
                $scope.results = [];

                $scope.onItemClick = function(selected) {
                    $scope.select({selected: selected});
                };

                $scope.search = function(query) {
                    var p = $scope.tipo === 'periodicos' ?
                        Qualis.queryPeriodicos(query) :
                        Qualis.queryEventos(query);

                    p.then(function(resp) {
                        $scope.results = resp.data;
                    });
                };

                $scope.search($scope.query);

                var delayedSearch;
                $scope.queryChange = function() {
                    if (delayedSearch) {
                        $timeout.cancel(delayedSearch);
                    }

                    delayedSearch = $timeout(function() {
                        $scope.search($scope.query);
                    }, 500);
                };

                $scope.submit = function() {
                    $scope.search($scope.query);
                };
            }
        };
    });
