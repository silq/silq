'use strict';

angular.module('silq2App')
    .factory('DadoGeral', function ($resource, $http, Grupo) {
        var DadoGeral = $resource('api/dado-geral/', {}, {
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

        /**
         * Invalida o cache do dado geral.
         * Necessário ao excluir/alterar o currículo do usuário atual.
         */
        DadoGeral.cacheInvalidate = function(dadoGeral) {
            $http.defaults.cache.remove('api/dado-geral');

            // Como os grupos são removidos ao alterar o currículo, limpamos
            // os grupos em cache também
            Grupo.cacheInvalidate();
        };

        return DadoGeral;
    });
