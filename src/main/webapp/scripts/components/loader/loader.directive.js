'use strict';

angular.module('silq2App')
    .directive('loader', function() {
        return {
            restrict: 'E',
            templateUrl: 'scripts/components/loader/loader.html',
            transclude: true
        };
    });
