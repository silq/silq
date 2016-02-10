'use strict';

function years() {
    var currentYear = new Date().getFullYear(), years = [];
    var finalYear = 1980;

    while (currentYear >= finalYear) {
        years.push(currentYear--);
    }

    return years;
}

angular.module('silq2App')
    .directive('avaliarForm', function() {
        return {
            restrict: 'E',
            scope: {
                model: '=model',
                submit: '&submit'
            },
            templateUrl: 'scripts/components/avaliar/avaliar-form.html',
            link: function($scope) {
                $scope.years = years();
                $scope.yearFilter = function(value) {
                    return value >= $scope.model.anoPublicacaoDe;
                };
            }
        };
    });
