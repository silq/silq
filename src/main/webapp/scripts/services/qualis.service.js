'use strict';

angular.module('silq2App')
    .factory('Qualis', function ($resource, $http) {
        return {
            queryPeriodicos: function(query, page) {
                return $http.get('api/qualis/periodicos', {
                    params: {
                        query: query,
                        page: page || 1
                    },
                    loadingIndicator: false
                });
            },
            queryEventos: function(query, page) {
                return $http.get('api/qualis/eventos', {
                    params: {
                        query: query,
                        page: page || 1
                    },
                    loadingIndicator: false
                });
            },
        };
    });
