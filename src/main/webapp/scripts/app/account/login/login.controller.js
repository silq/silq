'use strict';

angular.module('silq2App')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth) {
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="email"]').focus();});
        $scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                email: $scope.email,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                // Forçamos o reload para a atualização do MainController
                // e suas variavéis $scope de usuário no caso da caixa de login
                // estar na página inicial
                $state.go('home', {}, {reload: true});
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
