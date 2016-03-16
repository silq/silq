'use strict';

angular.module('silq2App')
    .directive('avaliarResult', function() {
        return {
            restrict: 'E',
            scope: {
                results: '=results'
            },
            templateUrl: 'scripts/components/avaliar/avaliar-result.html'
        };
    });
