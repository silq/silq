'use strict';

angular.module('silq2App')
    .factory('DadoGeral', function ($http, Cache) {
        return {
            get: function() {
                return $http.get('api/dado-geral', {
                    cache: true
                });
            },
            delete: function() {
                Cache.invalidate();
                return $http.delete('api/dado-geral');
            }
        };
    });
