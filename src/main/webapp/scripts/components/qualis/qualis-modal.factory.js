'use strict';

angular.module('silq2App')
    .factory('QualisModal', function ($uibModal, $templateCache, $http) {
        var templateUrl = 'scripts/components/qualis/qualis-modal.html';
        var tableTemplateUrl = 'scripts/components/qualis/qualis-table.html';

        // Carrega o qualis-modal e qualis-table templates antes de seu uso
        // para evitar um delay de carregamento e não mostrar o loadingIndicator
        // ao usar o modal pela primeira vez:
        if ($templateCache.get(templateUrl) === undefined) {
            $http.get(templateUrl).then(function(resp) {
                if (resp.status === 200) {
                    $templateCache.put(templateUrl, resp.data);
                }
            });
        }

        if ($templateCache.get(tableTemplateUrl) === undefined) {
            $http.get(tableTemplateUrl).then(function(resp) {
                if (resp.status === 200) {
                    $templateCache.put(tableTemplateUrl, resp.data);
                }
            });
        }

        return {
            open: function(item) {
                var modalInstance = $uibModal.open({
                    templateUrl: templateUrl,
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
