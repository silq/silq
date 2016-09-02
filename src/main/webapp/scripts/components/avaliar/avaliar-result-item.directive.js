'use strict';

angular.module('silq2App')
    .directive('avaliarResultItem', function(QualisModal, Feedback, Flash) {
        return {
            restrict: 'E',
            scope: {
                item: '=item',
                filter: '=filter',
                form: '=form'
            },
            templateUrl: 'scripts/components/avaliar/avaliar-result-item.html',
            link: function($scope) {
                $scope.mais = false;
                $scope.verMais = function(flag) {
                    $scope.mais = flag;
                };

                $scope.sugerir = function(item) {
                    QualisModal.open(item, $scope.form)
                        .then(function(result) {
                            $scope.feedback(item, result.resultado);
                        });
                };

                $scope.feedback = function(item, conceito) {
                    var query = item.tituloVeiculo;
                    var id = conceito.id;
                    var feedbackRequest;

                    // Remove o conceito do feedback e o insere novamente no início da lista
                    $scope.item.conceitos[0].flagged = false;
                    $scope.item.conceitos.splice($scope.item.conceitos.indexOf(conceito), 1);
                    $scope.item.conceitos.unshift(conceito);
                    conceito.flagged = true;

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
