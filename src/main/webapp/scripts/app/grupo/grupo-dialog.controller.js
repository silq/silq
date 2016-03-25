'use strict';

angular.module('silq2App').controller('GrupoDialogController', function($scope, $stateParams, $uibModalInstance, entity, Grupo, Flash) {

        $scope.grupo = entity;
        $scope.load = function(id) {
            Grupo.get({id : id}, function(result) {
                $scope.grupo = result;
            });
        };

        var onSaveSuccess = function(result) {
            $scope.$emit('silq2App:grupoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
            Flash.create('success', '<strong>Sucesso!</strong> Grupo "<i>' + result.nomeGrupo + '"</i> foi salvo');
        };

        var onSaveError = function(e) {
            $scope.isSaving = false;
            Flash.create('danger', e.data.description || e.data.message);
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.grupo.id !== null) {
                Grupo.update($scope.grupo, onSaveSuccess, onSaveError);
            } else {
                Grupo.save($scope.grupo, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
});
