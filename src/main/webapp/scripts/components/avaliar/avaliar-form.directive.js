'use strict';

angular.module('silq2App')
    .directive('avaliarForm', function() {
        var currentYear = new Date().getFullYear();
        var years = function() {
            var years = [];
            var initialYear = 1980;

            var i = parseInt(currentYear);
            while (i >= initialYear) {
                years.push(i--);
            }

            return years;
        };

        return {
            restrict: 'E',
            scope: {
                model: '=model',
                submit: '&submit'
            },
            templateUrl: 'scripts/components/avaliar/avaliar-form.html',
            link: function($scope) {
                $scope.years = years();    
                $scope.model.periodoAvaliacao = {};

                $scope.changePeriodoInicio = function() {
                    $scope.model.periodoAvaliacao.fim = currentYear.toString();
                };

                $scope.yearFilter = function(value) {
                    return value >= $scope.model.periodoAvaliacao.inicio;
                };
            }
        };
    });
