'use strict';

angular.module('silq2App')
    .factory('Feedback', function ($resource, $http) {
        return {
            evento: function(query, eventoId) {
                return $http.post('api/feedback/evento/', {
                    query: query,
                    eventoId: eventoId
                }, {
                    loadingIndicator: false
                });
            },
            periodico: function(query, periodicoId) {
                return $http.post('api/feedback/periodico/', {
                    query: query,
                    periodicoId: periodicoId
                }, {
                    loadingIndicator: false
                });
            },
        };
    });
