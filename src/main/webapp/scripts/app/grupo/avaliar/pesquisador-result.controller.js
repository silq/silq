'use strict';

angular.module('silq2App')
    .controller('PesquisadorResultController', function ($scope, $stateParams, Grupo, Flash) {
        $scope.ready = false;
        $scope.grupoId = $stateParams.grupoId;

        Grupo.avaliarPesquisador($stateParams.grupoId, $stateParams.pesquisadorId).then(function(resp) {
            $scope.result = resp.data;
        }).catch(function(err) {
            console.error(err);
            Flash.create('danger', '<strong>Ops!</strong> Ocorreu um erro');
        });
    });
