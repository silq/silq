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

        return Grupo;
    });
