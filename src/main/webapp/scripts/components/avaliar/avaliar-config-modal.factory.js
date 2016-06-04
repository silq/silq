'use strict';

angular.module('silq2App')
    .factory('AvaliarConfigModal', function ($uibModal) {
        return {
            open: function(form) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'scripts/components/avaliar/avaliar-config-modal.html',
                    controller: 'AvaliarConfigModalController',
                    size: 'md',
                    resolve: {
                        form: function () {
                            return form;
                        }
                    }
                });

                return modalInstance.result;
            }
        };
    })
    .controller('AvaliarConfigModalController', function($scope, $uibModalInstance, form) {
        // Valores default do form de avaliação
        $scope.form = {
            nivelSimilaridade: '0.6'
        };

        // Popula os dados de form vindos como parâmetro, sobrescrevendo os defaults
        if (form) {
            for (var key in form) {
                $scope.form[key] = form[key];
            }
        }

        $scope.ok = function () {
            $uibModalInstance.close($scope.form);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    });
