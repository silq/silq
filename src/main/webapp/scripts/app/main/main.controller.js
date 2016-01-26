'use strict';

angular.module('silq2App')
    .controller('MainController', function ($scope, $state, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;

            // Se estiver logado, redireciona para dashboard
            if (Principal.isAuthenticated()) {
                $state.go('principal');
            }
        });
    });
