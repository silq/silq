'use strict';

angular.module('silq2App')
    .controller('QualisController', function ($scope, $timeout, Qualis) {
        $scope.query = '';
        $scope.tipo = 'periodicos';
        $scope.results = [];

        $scope.search = function(query) {
            var p = $scope.tipo == 'periodicos' ?
                Qualis.queryPeriodicos(query) :
                Qualis.queryEventos(query);

            p.then(function(resp) {
                $scope.results = resp.data;
            });
        };

        $scope.search('');

        var delayedSearch;
        $scope.queryChange = function() {
            delayedSearch && $timeout.cancel(delayedSearch);
            delayedSearch = $timeout(function() {
                search($scope.query);
            }, 500);
        };

        $scope.submit = function() {
            $scope.search($scope.query);
        };
    });
