'use strict';

angular.module('silq2App')
    .controller('AvaliarController', function ($scope, DadoGeral) {
        $scope.avaliarForm = {
            nivelSimilaridade: '0.6'
        };

        DadoGeral.get().$promise.then(function(dados) {
            $scope.avaliarForm.area = dados.areaConhecimento;
        });

        $scope.submit = function() {
            console.log($scope.avaliarForm);
        };
    });
