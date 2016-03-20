'use strict';

angular.module('silq2App')
    .controller('GrupoDetailController', function ($scope, $stateParams, entity, Grupo, Upload) {
        $scope.grupo = entity;
        $scope.load = function (id) {
            Grupo.get({id: id}, function(result) {
                $scope.grupo = result;
            });
        };

        $scope.files = [];
        $scope.uploadConfig = {
            url: 'api/grupos/' + $stateParams.id + '/addPesquisador'
        };
    });
