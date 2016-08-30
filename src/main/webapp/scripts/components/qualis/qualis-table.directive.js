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
                 * Parâmetros de busca inicial.
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
                $scope.query = angular.merge({}, {
                    // Opções default (serão consideradas se não vierem como parãmetro):
                    query: '',
                    area: undefined,
                    page: 1
                }, $scope.query);

                $scope.mode = angular.isDefined($scope.mode) ? $scope.mode : null;
                $scope.tipo = $scope.mode || 'eventos';
                $scope.results = [];
                $scope.loading = false;

                $scope.onItemClick = function(selected) {
                    $scope.select({selected: selected});
                };

                $scope.search = function() {
                    $scope.loading = true;
                    var params = angular.copy($scope.query);
                    params.page -= 1; // API paging is 0-indexed

                    var p = $scope.tipo === 'periodicos' ?
                        Qualis.queryPeriodicos(params) :
                        Qualis.queryEventos(params);

                    p.then(function(resp) {
                        $scope.loading = false;
                        $scope.results = resp.data;
                    });
                };

                var delayedSearch;
                $scope.queryChange = function() {
                    if (delayedSearch) {
                        $timeout.cancel(delayedSearch);
                    }

                    delayedSearch = $timeout(function() {
                        $scope.search();
                    }, 300);
                };

                $scope.submit = function() {
                    $scope.query.page = 1;
                    $scope.search();
                };

                $scope.pageChanged = function() {
                    $scope.search();
                };

                $scope.search();
            }
        };
    });
