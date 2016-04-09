'use strict';

angular.module('silq2App')
	.controller('GrupoDeleteController', function($scope, $uibModalInstance, $stateParams, Grupo, Flash) {
		Grupo.get($stateParams.id).then(function(resp) {
			$scope.grupo = resp.data;
		});

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.confirmDelete = function (id) {
            Grupo.delete(id).then(function() {
				Flash.create('success', '<strong>Sucesso!</strong> Grupo excluído');
                $uibModalInstance.close(true);
            });
        };
    });
