'use strict';

angular.module('silq2App')
    .controller('GrupoAvaliacaoController', function ($scope, $state, $stateParams, Grupo, Cache, Flash) {
        Grupo.get($stateParams.id).then(function(resp) {
            $scope.grupo = resp.data;
        });

        Grupo.avaliarGrupo($stateParams.id).then(function(resp) {
            $scope.results = resp.data;
        });
    });
