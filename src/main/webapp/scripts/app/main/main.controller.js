'use strict';

angular.module('silq2App')
    .controller('MainController', function ($scope, $state, Principal, LANDING_PAGE) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;

            // Se estiver logado, redireciona para página home
            if (Principal.isAuthenticated()) {
                $state.go(LANDING_PAGE);
            }
        });
    });
