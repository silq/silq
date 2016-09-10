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
                            var conceito = result.resultado;
                            conceito.similaridade = result.similaridade.value;
                            conceito.tituloVeiculo = conceito.titulo;
                            conceito.conceito = conceito.estrato;
                            $scope.feedback(item, conceito);
                        });
                };

                $scope.feedback = function(item, conceito) {
                    var query = item.tituloVeiculo;
                    var id = conceito ? conceito.id : null;
                    var feedbackRequest;

                    $scope.item.conceitos.forEach(function(c) {
                        // Remove a flag de conceitos anteriores
                        c.flagged = false;

                        // Se veio do modal, também remove da lista de conceitos
                        if (c.modal) {
                            var index = $scope.item.conceitos.indexOf(c);
                            $scope.item.conceitos.splice(index, 1);
                        }
                    });

                    if (conceito) {
                        // Adiciona a flag ao novo conceito
                        conceito.flagged = true;

                        // Se não possuir o conceito, adiciona-o à lista
                        // Isso acontece quando o conceito vem do modal
                        if ($scope.item.conceitos.indexOf(conceito) <= 0) {
                            conceito.modal = true;
                            $scope.item.conceitos.push(conceito);
                        }
                    }

                    if (item.issn) {
                        feedbackRequest = Feedback.periodico({
                            query: query,
                            periodicoId: id
                        });
                    } else {
                        feedbackRequest = Feedback.evento({
                            query: query,
                            eventoId: id
                        });
                    }

                    feedbackRequest.then(function() {
                        Flash.create('success', '<strong>Feedback registrado!</strong> Atualize a página para recalcular as estatísticas');

                        // Emite o evento de feedback para atualização
                        // do status 'modificado' em avaliar-result.controller
                        $scope.$emit('silq:feedback');
                    });
                };

                $scope.feedbackRemove = function(item) {
                    $scope.feedback(item, null);
                };
            }
        };
    });
