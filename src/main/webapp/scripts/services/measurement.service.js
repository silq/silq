'use strict';

angular.module('silq2App')
    .factory('Measurement', function ($resource, $http) {
        return {
            measure: function(params) {
                return $http.get('api/measurement', {
                    params: params
                });
            }
        };
    });
