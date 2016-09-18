'use strict';

angular.module('silq2App')
    .factory('Feedback', function ($resource, $http) {
        return {
            evento: function(body) {
                return $http.post('api/feedback/evento/', body, {
                    loadingIndicator: false
                });
            },
            periodico: function(body) {
                return $http.post('api/feedback/periodico/', body, {
                    loadingIndicator: false
                });
            },
            deleteEvento: function(body) {
                return $http.post('api/feedback/evento/delete', body, {
                    loadingIndicator: false
                });
            },
            deletePeriodico: function(body) {
                return $http.post('api/feedback/periodico/delete', body, {
                    loadingIndicator: false
                });
            }
        };
    });
