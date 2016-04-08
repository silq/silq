'use strict';

angular.module('silq2App')
    .factory('Grupo', function ($resource, $http) {
        var Grupo = $resource('api/grupos/:id', {}, {
            'get': {
                method: 'GET',
                cache: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });

        /**
         * Invalida o cache de grupos.
         * Necessário ao alterar um grupo ou pesquisador para atulizar o cache.
         */
        Grupo.cacheInvalidate = function(grupo) {
            $http.defaults.cache.remove('api/grupos');
            if (grupo) {
                $http.defaults.cache.remove('api/grupos/' + grupo.id);
            }
        };

        Grupo.removePesquisador = function(grupoId, pesquisadorId) {
            return $http.delete('api/grupos/' + grupoId + '/removePesquisador/' + pesquisadorId);
        };

        Grupo.avaliarPesquisador = function(grupoId, pesquisadorId) {
            return $http.get('api/grupos/'+grupoId+'/avaliar/' + pesquisadorId, {
                cache: true
            });
        };

        return Grupo;
    });
