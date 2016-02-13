'use strict';

angular.module('silq2App')
    .controller('AvaliarController', function ($scope, DadoGeral, Similarity, Flash) {
        $scope.results = null;
        $scope.avaliarForm = {
            nivelSimilaridade: '0.6'
        };

        DadoGeral.get().$promise.then(function(dados) {
            $scope.avaliarForm.area = dados.areaConhecimento;
        });

        $scope.submit = function() {
            Similarity.compareMine($scope.avaliarForm).$promise.then(function(results) {
                $scope.results = results;
                Flash.create('success', 'Avaliação concluída');
            });
        };
    });
