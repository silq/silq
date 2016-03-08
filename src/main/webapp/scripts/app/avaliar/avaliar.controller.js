'use strict';

angular.module('silq2App')
    .controller('AvaliarController', function ($scope, DadoGeral, Similarity, Flash) {
        $scope.files = [];
        $scope.avaliarForm = {
            nivelSimilaridade: '0.6'
        };

        $scope.submit = function() {
            if ($scope.files.length <= 0) {
                Flash.create('danger', 'Selecione ao menos um currículo para avaliar');
                return;
            }

            // Similarity.compareMine($scope.avaliarForm).$promise.then(function(results) {
            //     $scope.results = results;
            //     Flash.create('success', 'Avaliação concluída');
            // });
        };

        $scope.uploadFiles = function(files) {
            if (!files) return;
            files.forEach(function(file) {
                console.log(file);
                $scope.files.push(file);
            });
        };
    });
