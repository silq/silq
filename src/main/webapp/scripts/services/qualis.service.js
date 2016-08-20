'use strict';

angular.module('silq2App')
    .factory('Qualis', function ($resource, $http) {
        return {
            queryPeriodicos: function(params) {
                return $http.get('api/qualis/periodicos', {
                    params: params,
                    loadingIndicator: false
                });
            },
            queryEventos: function(params) {
                return $http.get('api/qualis/eventos', {
                    params: params,
                    loadingIndicator: false
                });
            },
        };
    });
