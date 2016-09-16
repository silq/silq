'use strict';

angular.module('silq2App')
    .controller('MeasurementController', function ($scope, Measurement) {
        $scope.results = [];

        Measurement.measure().then(function(resp) {
            $scope.results = resp.data;
        });
    });
