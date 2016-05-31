'use strict';

angular.module('silq2App')
    .controller('AvaliarResultController', function ($scope, $stateParams, Avaliacao, Flash) {
        $scope.results = null;

        Avaliacao.avaliar($stateParams.id, $stateParams.avaliarForm)
            .then(function(response) {
                $scope.results = response.data;
                Flash.create('success', '<strong>Sucesso!</strong> Avaliação concluída');
            });

        $scope.goBack = function() {
            window.history.back();
        }
    });
