'use strict';

angular.module('silq2App')
    .controller('MeasurementController', function ($scope, $stateParams, $filter, Measurement) {
        $scope.results = [];

        var params = $stateParams;
        Measurement.measure(params).then(function(resp) {
            $scope.results = resp.data;

            $scope.labels = []; // Thresholds
            $scope.series = ['Exatidão', 'MRR'];
            var match = []; // Valores Exatidão
            var mrr = []; // Valores MRR
            $scope.results.forEach(function(data) {
                $scope.labels.push($filter('number')(data.threshold, 3));

                match.push(data.noFeedback.match);
                mrr.push(data.noFeedback.meanReciprocralRank);
            });
            $scope.data = [match, mrr];
            $scope.options = {
                legend: {
                    display: true
                },
                scales: {
                    xAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: 'Threshold'
                        }
                    }],
                    yAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: 'Trabalhos corretamente avaliados / MRR'
                        }
                    }],
                }
            };
        });
    });
