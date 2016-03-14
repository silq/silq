'use strict';

angular.module('silq2App')
    .controller('MinhaAvaliacaoController', function ($scope, DadoGeral, Similarity, Flash) {
        $scope.results = null;
        $scope.avaliarForm = {
            nivelSimilaridade: '0.6'
        };

        DadoGeral.get().$promise.then(function(dados) {
            $scope.avaliarForm.area = dados.areaConhecimento;
        });

        $scope.submit = function() {
            Similarity.avaliarAtual($scope.avaliarForm).then(function(response) {
                $scope.results = response.data;
                Flash.create('success', 'Avaliação concluída');
            });
        };
    });
