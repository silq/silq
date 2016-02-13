'use strict';

angular.module('silq2App')
    .controller('GrupoController', function ($scope, $state, DadoGeral, Grupo) {

        DadoGeral.get(function(dadoGeral) {
            $scope.hasCurriculum = !!dadoGeral.id;
        });

        $scope.grupos = [];
        $scope.loadAll = function() {
            Grupo.query(function(result) {
               $scope.grupos = result;
            });
        };
        $scope.loadAll();

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
