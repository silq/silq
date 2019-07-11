'use strict';

angular.module('silq2App')
    .controller('GrupoClassificacaoController', function ($scope, $state, $stateParams, Grupo, Cache, Flash) {
        Grupo.get($stateParams.id).then(function(resp) {
            $scope.grupo = resp.data;
        });

        Grupo.classificarGrupo($stateParams.id, $stateParams.avaliarForm).then(function(resp) {
            $scope.results = resp.data;
        });
    });
