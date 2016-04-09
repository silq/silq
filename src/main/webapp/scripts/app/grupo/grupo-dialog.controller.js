'use strict';

angular.module('silq2App').controller('GrupoDialogController', function($scope, $stateParams, $uibModalInstance, Grupo, Flash) {
        $scope.grupo = {};

        if ($stateParams.id) {
            Grupo.get($stateParams.id).then(function(resp) {
                $scope.grupo = resp.data;
            });
        }

        var onSaveSuccess = function(resp) {
            $uibModalInstance.close(resp.data);
            $scope.isSaving = false;
            Flash.create('success', '<strong>Sucesso!</strong> Grupo "<i>' + resp.data.nomeGrupo + '"</i> foi salvo');
        };

        var onSaveError = function(e) {
            $scope.isSaving = false;
            Flash.create('danger', e.data.description || e.data.message);
        };

        $scope.save = function() {
            if ($scope.grupo.id !== undefined) {
                Grupo.update($scope.grupo).then(onSaveSuccess).catch(onSaveError);
            } else {
                Grupo.create($scope.grupo).then(onSaveSuccess).catch(onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
});
