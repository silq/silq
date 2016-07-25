'use strict';

angular.module('silq2App')
    .factory('QualisModal', function ($uibModal) {
        return {
            open: function(item) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'scripts/components/qualis/qualis-modal.html',
                    controller: 'QualisModalController',
                    size: 'lg',
                    resolve: {
                        item: function () {
                            return item;
                        }
                    }
                });

                return modalInstance.result;
            }
        };
    })
    .controller('QualisModalController', function($scope, $uibModalInstance, item) {
        $scope.item = item;
        $scope.mode = item.issn !== undefined ? 'periodicos' : 'eventos';
        $scope.query = item.tituloVeiculo + ' ' + item.ano;

        $scope.ok = function(selected) {
            $uibModalInstance.close(selected);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    });
