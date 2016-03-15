'use strict';

angular.module('silq2App')
    .controller('ResultController', function ($scope, $stateParams, Similarity) {
        Similarity.result($stateParams.cacheId).then(function(response) {
            console.log(response);
            $scope.results = response.data;
        }).catch(function(err) {
            console.error(err);
        });
    });
