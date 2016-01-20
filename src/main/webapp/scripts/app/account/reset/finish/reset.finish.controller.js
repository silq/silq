'use strict';

angular.module('silq2App')
    .controller('ResetFinishController', function ($scope, $stateParams, $timeout, Auth) {

        $scope.keyMissing = $stateParams.key === undefined;
        $scope.doNotMatch = null;

        $scope.resetAccount = {};
        $timeout(function (){angular.element('[ng-model="resetAccount.novaSenha"]').focus();});

        $scope.finishReset = function() {
            if ($scope.resetAccount.novaSenha !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                Auth.resetPasswordFinish({key: $stateParams.key, novaSenha: $scope.resetAccount.novaSenha}).then(function () {
                    $scope.success = 'OK';
                }).catch(function (response) {
                    $scope.success = null;
                    $scope.error = 'ERROR';

                });
            }

        };
    });
