'use strict';

angular.module('silq2App')
    .controller('MeasurementController', function ($scope, $stateParams, Measurement) {
        $scope.results = [];

        var params = $stateParams;
        Measurement.measure(params).then(function(resp) {
            $scope.results = resp.data;

            $scope.labels = []; // Thresholds
            $scope.series = ['Exatidão', 'MRR'];
            var match = []; // Valores Exatidão
            var mrr = []; // Valores MRR
            $scope.results.forEach(function(data) {
                $scope.labels.push(data.threshold);
                match.push(data.match);
                mrr.push(data.meanReciprocralRank);
            });
            $scope.data = [match, mrr];
        });
    });
