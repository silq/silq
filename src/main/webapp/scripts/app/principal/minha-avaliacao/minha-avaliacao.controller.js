'use strict';

angular.module('silq2App')
    .controller('MinhaAvaliacaoController', function ($scope, DadoGeral, Similarity, Flash) {
        $scope.results = null;
        $scope.avaliarForm = {
            nivelSimilaridade: '0.6'
        };

        DadoGeral.get().then(function(resp) {
            $scope.avaliarForm.area = resp.data.areaConhecimento;
        });

        $scope.submit = function() {
            Similarity.avaliarAtual($scope.avaliarForm).then(function(response) {
                $scope.results = response.data;
                Flash.create('success', '<strong>Sucesso!</strong> Avaliação concluída');
            });
        };
    });
