'use strict';

angular.module('silq2App')
    .controller('ContentHeaderController', function ($scope, $state, Grupo) {
        Grupo.query(function(grupos) {
            $scope.$state = $state;
            $scope.countGrupos = grupos.length;
        });
    });
