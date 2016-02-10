'use strict';

angular.module('silq2App')
    .controller('RegisterController', function ($scope, $state, $timeout, Auth, LANDING_PAGE) {
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.registerAccount = {};
        $timeout(function (){angular.element('[ng-model="registerAccount.login"]').focus();});

        $scope.register = function () {
            if ($scope.registerAccount.senha !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.doNotMatch = null;
                $scope.error = null;
                $scope.errorEmailExists = null;

                Auth.createAccount($scope.registerAccount).then(function () {
                    // Efetua login após cadastro
                    Auth.login({
                        email: $scope.registerAccount.email,
                        password: $scope.registerAccount.senha,
                        rememberMe: true
                    }).then(function() {
                        $state.go(LANDING_PAGE);
                    }).catch(function() {
                        $scope.error = 'ERROR';
                    });
                }).catch(function (response) {
                    if (response.status === 400 && response.data === 'E-mail já encontra-se cadastrado') {
                        $scope.errorEmailExists = 'ERROR';
                    } else {
                        $scope.error = 'ERROR';
                    }
                });
            }
        };
    });
