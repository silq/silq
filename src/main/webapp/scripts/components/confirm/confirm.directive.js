angular.module('silq2App')
    .directive('confirmClick', function($uibModal) {
        var ModalInstanceCtrl = function($scope, $uibModalInstance) {
           $scope.ok = function() {
             $uibModalInstance.close();
           };

           $scope.cancel = function() {
             $uibModalInstance.dismiss('cancel');
           };
         };

        return {
            restrict: 'A',
            scope: {
              confirmClick: '&'
            },
            link: function($scope, element, attrs) {
                element.bind('click', function() {
                    var message = attrs.confirmMessage || 'Você tem certeza?';

                    var modalHtml = '<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true" ng-click="cancel()">&times;</button><h4 class="modal-title">Confirmar ação</h4></div>';
                    modalHtml += '<div class="modal-body">' + message + '</div>';
                    modalHtml += '<div class="modal-footer"><button class="btn btn-primary" ng-click="ok()">Confirmar</button><button class="btn btn-default" ng-click="cancel()">Cancelar</button></div>';

                    var modalInstance = $uibModal.open({
                      template: modalHtml,
                      controller: ModalInstanceCtrl
                    });

                    modalInstance.result.then(function() {
                        $scope.confirmClick({ item: $scope.item});
                    }, function() {
                        // Modal dismissed
                    });
                });
            }
        }
    });
