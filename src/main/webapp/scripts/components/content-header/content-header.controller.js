'use strict';

angular.module('silq2App')
    .controller('ContentHeaderController', function ($scope, $state, Grupo) {
        Grupo.query().then(function(resp) {
            $scope.$state = $state;
            $scope.countGrupos = resp.data.length;
        });
    });
