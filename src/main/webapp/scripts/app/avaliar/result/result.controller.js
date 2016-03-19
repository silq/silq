'use strict';

angular.module('silq2App')
    .controller('ResultController', function ($scope, $stateParams, Similarity) {
        $scope.ready = false;
        $scope.multiple = false;
        $scope.active = $stateParams.resultId || null;

        Similarity.result($stateParams.cacheId).then(function(response) {
            $scope.ready = true;
            $scope.results = response.data;
            $scope.multiple = $scope.results.length > 1;

            if (!$scope.multiple) {
                $scope.active = 0;
            }
        }).catch(function(err) {
            console.error(err);
        });
    });
