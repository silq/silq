'use strict';

angular.module('silq2App')
    .directive('avaliarForm', function() {
        return {
            restrict: 'E',
            scope: {
                model: '=model',
                submit: '&submit'
            },
            templateUrl: 'scripts/components/avaliar/form.html'
        };
    });
