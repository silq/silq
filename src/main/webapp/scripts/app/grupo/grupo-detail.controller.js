'use strict';

angular.module('silq2App')
    .controller('GrupoDetailController', function ($scope, $rootScope, $stateParams, entity, Grupo) {
        $scope.grupo = entity;
        $scope.load = function (id) {
            Grupo.get({id: id}, function(result) {
                $scope.grupo = result;
            });
        };
    });
