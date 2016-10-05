'use strict';

angular.module('silq2App')
    .controller('MeasurementController', function ($scope, $stateParams, $filter, Measurement) {
        $scope.results = [];
        var params = $stateParams;

        var chartOptions = function(xLabel, yLabel) {
            return {
                legend: {
                    display: true
                },
                scales: {
                    xAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: xLabel
                        }
                    }],
                    yAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: yLabel
                        }
                    }],
                }
            };
        };

        var chart1 = function(results) {
            var chart = {};
            chart.labels = []; // Thresholds
            chart.series = ['Exatidão', 'MRR'];
            chart.options = chartOptions('Threshold', 'Trabalhos corretamente avaliados / MRR');
            var match = []; // Valores Exatidão
            var mrr = []; // Valores MRR

            results.forEach(function(data) {
                chart.labels.push($filter('number')(data.threshold, 3));

                match.push(data.noFeedback.match);
                mrr.push(data.noFeedback.meanReciprocralRank);
            });
            chart.data = [match, mrr];
            return chart;
        };

        var chart2 = function(results) {
            var chart = {};
            chart.labels = []; // Thresholds
            chart.series = ['trgm', 'trgm + query_aliasing'];
            chart.options = chartOptions('Threshold', 'Exatidão');
            var a = []; // Valores trgm
            var b = []; // Valores trgm + query_aliasing

            results.forEach(function(data) {
                chart.labels.push($filter('number')(data.threshold, 3));

                a.push(data.noFeedback.match);
                b.push(data.withFeedback.match);
            });
            chart.data = [a, b];
            return chart;
        };

        Measurement.measure(params).then(function(resp) {
            $scope.results = resp.data;
            $scope.chart1 = chart1($scope.results);
            $scope.chart2 = chart2($scope.results);
        });
    });
