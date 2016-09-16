'use strict';

angular.module('silq2App')
    .factory('Measurement', function ($resource, $http) {
        return {
            measure: function() {
                return $http.get('api/measurement');
            }
        };
    });
