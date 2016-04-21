'use strict';

angular.module('silq2App')
    .controller('GrupoController', function ($scope, $state, Grupo) {
        $scope.grupos = [];

        Grupo.query().then(function(resp) {
           $scope.grupos = resp.data;
        });
    });
