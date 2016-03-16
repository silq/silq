'use strict';

angular.module('silq2App')
    .factory('DadoGeral', function ($resource) {
        return $resource('api/dado-geral/', {}, {
                'get': {
                    method: 'GET',
                    cache: true,
                    transformResponse: function (data) {
                        if (data) {
                            return angular.fromJson(data);
                        }
                        return null;
                    }
                },
                'delete': { method: 'DELETE' }
            });
        });
