'use strict';

angular.module('silq2App')
    .controller('ResultLivreListController', function ($scope, $state, $stateParams, Flash, Avaliacao) {
        $scope.multiple = false;

        Avaliacao.livreResult($stateParams.cacheId).then(function(response) {
            $scope.results = response.data;
            $scope.multiple = $scope.results.length > 1;

            if (!$scope.multiple) {
                $state.go('.detail', {
                    resultId: 0
                });
            }
        });
    });
