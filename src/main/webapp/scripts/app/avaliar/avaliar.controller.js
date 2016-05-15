'use strict';

angular.module('silq2App')
    .controller('AvaliarController', function ($scope, $state, $stateParams) {
        // Valores default do form de avaliação
        $scope.avaliarForm = {
            nivelSimilaridade: '0.6'
        };

        // Popula os dados de form vindos como parâmetro, sobrescrevendo os defaults
        if ($stateParams.avaliarForm) {
            for (var key in $stateParams.avaliarForm) {
                $scope.avaliarForm[key] = $stateParams.avaliarForm[key];
            }
        }

        $scope.submit = function() {
            $state.go('avaliar-result', {
                avaliarForm: $scope.avaliarForm
            });
        };
    });
