'use strict';

angular.module('silq2App')
	.controller('GrupoDeleteController', function($scope, $uibModalInstance, entity, Grupo, Flash) {

        $scope.grupo = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Grupo.delete({id: id},
                function () {
					Flash.create('success', '<strong>Sucesso!</strong> Grupo excluído');
                    $uibModalInstance.close(true);
					Grupo.cacheInvalidate($scope.grupo);
                });
        };

    });
