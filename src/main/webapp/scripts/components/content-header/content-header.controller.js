'use strict';

angular.module('silq2App')
    .controller('ContentHeaderController', function ($scope, Grupo) {
        Grupo.query(function(grupos) {
            $scope.countGrupos = grupos.length;
        });
    });
