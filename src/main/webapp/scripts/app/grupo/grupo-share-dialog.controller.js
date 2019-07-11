'use strict';

angular.module('silq2App').controller('GrupoShareController', function($scope, $stateParams, $uibModalInstance, Grupo, Flash) {
    $scope.email = '';

    if ($stateParams.id) {
        Grupo.get($stateParams.id).then(function(resp) {
            $scope.grupo = resp.data;
        });
    }

    var onSaveSuccess = function(resp) {
        $uibModalInstance.close(resp.data);
        $scope.isSaving = false;
        Flash.create('success', '<strong>Sucesso!</strong> Grupo foi compartilhado com o usuário de email "<i>' + $scope.email + '"</i>');
    };

    var onSaveError = function(e) {
        $scope.isSaving = false;
        Flash.create('danger', e.data.description || e.data.message);
    };

    $scope.share = function(id) {
        Grupo.share(id,$scope.email).then(onSaveSuccess).catch(onSaveError);
    };

    $scope.clear = function() {
        $uibModalInstance.dismiss('cancel');
    };
});
