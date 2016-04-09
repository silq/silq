'use strict';

angular.module('silq2App')
    .controller('GrupoController', function ($scope, $state, DadoGeral, Grupo) {
        $scope.grupos = [];

        DadoGeral.get().then(function(resp) {
            $scope.hasCurriculum = !!resp.data.id;
        });

        Grupo.query().then(function(resp) {
           $scope.grupos = resp.data;
        });
    });
