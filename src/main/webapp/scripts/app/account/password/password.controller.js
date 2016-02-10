'use strict';

angular.module('silq2App')
    .controller('PasswordController', function ($scope, $state, Auth, Principal, Flash) {
        Principal.identity().then(function(account) {
            $scope.account = account;
        });

        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.changePassword = function () {
            if ($scope.password !== $scope.confirmPassword) {
                $scope.error = null;
                $scope.success = null;
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.doNotMatch = null;
                Auth.changePassword($scope.password).then(function () {
                    $scope.error = null;
                    Flash.create('success', '<strong>Sucesso!</strong> Senha alterada. Execute login novamente');
                    $state.go('login');
                }).catch(function () {
                    $scope.error = 'ERROR';
                });
            }
        };
    });
