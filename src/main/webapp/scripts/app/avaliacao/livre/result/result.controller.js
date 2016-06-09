'use strict';

angular.module('silq2App')
    .controller('AvaliacaoLivreResultController', function ($scope, $stateParams, Avaliacao) {
        $scope.multiple = false;

        Avaliacao.livreResult($stateParams.cacheId).then(function(response) {
            var results = response.data;
            $scope.multiple = results.length > 1;
            $scope.result = results[$stateParams.resultId];
        });
    });
