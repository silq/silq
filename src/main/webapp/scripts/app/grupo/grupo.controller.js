'use strict';

angular.module('silq2App')
    .controller('GrupoController', function ($scope, $state, DadoGeral, Grupo) {
        $scope.status = 'loading';
        $scope.grupos = [];

        DadoGeral.get(function(dadoGeral) {
            $scope.hasCurriculum = !!dadoGeral.id;
            $scope.status = 'success';
        });

        Grupo.query(function(result) {
           $scope.grupos = result;
           $scope.status = 'success';
        });

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.grupo = {
                nomeGrupo: null,
                nomeInstituicao: null,
                nomeArea: null,
                id: null
            };
        };
    });
