'use strict';

angular.module('silq2App')
    .controller('PesquisadorResultController', function ($scope, $stateParams, Grupo, Flash) {
        $scope.status = 'loading';
        $scope.ready = false;
        $scope.grupoId = $stateParams.grupoId;

        Grupo.avaliarPesquisador($stateParams.grupoId, $stateParams.pesquisadorId).then(function(resp) {
            $scope.result = resp.data;
            $scope.status = 'success';
        }).catch(function(err) {
            $scope.status = 'error';
            console.error(err);
            Flash.create('danger', '<strong>Ops!</strong> Ocorreu um erro');
        });
    });
