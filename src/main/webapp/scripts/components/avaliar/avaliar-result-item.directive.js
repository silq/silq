'use strict';

angular.module('silq2App')
    .directive('avaliarResultItem', function(QualisModal, Feedback, Flash) {
        return {
            restrict: 'E',
            scope: {
                item: '=item',
                filter: '=filter'
            },
            templateUrl: 'scripts/components/avaliar/avaliar-result-item.html',
            link: function($scope) {
                $scope.mais = false;
                $scope.verMais = function(flag) {
                    $scope.mais = flag;
                };

                $scope.sugerir = function(item) {
                    QualisModal.open(item)
                        .then(function(result) {
                            $scope.feedback(item, result);
                        });
                };

                $scope.feedback = function(item, conceito) {
                    var query = item.tituloVeiculo;
                    var id = conceito.resultado.id;
                    var feedbackRequest;

                    if (item.issn) {
                        feedbackRequest = Feedback.periodico(query, id);
                    } else {
                        feedbackRequest = Feedback.evento(query, id);
                    }

                    feedbackRequest.then(function() {
                        Flash.create('success', '<strong>Sucesso!</strong> Feedback registrado');
                    });
                };
            }
        };
    });
