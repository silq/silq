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
