'use strict';

angular.module('silq2App')
    .factory('Qualis', function ($resource, $http, Cache) {
        return {
            queryPeriodicos: function(query) {
                return $http.get('api/qualis/periodicos?query=' + query, {
                    loadingIndicator: false
                });
            },
            queryEventos: function(query) {
                return $http.get('api/qualis/eventos?query=' + query, {
                    loadingIndicator: false
                });
            },
        };
    });
