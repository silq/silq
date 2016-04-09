'use strict';

angular.module('silq2App')
    .controller('RemoverCurriculumController', function ($scope, $state, DadoGeral, Flash) {
        $scope.remover = function() {
            DadoGeral.delete().then(function() {
                Flash.create('success', '<strong>Sucesso!</strong> Currículo removido');
                $state.go('principal');
            });
        };
    });
