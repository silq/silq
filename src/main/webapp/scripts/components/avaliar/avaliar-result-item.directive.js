'use strict';

angular.module('silq2App')
    .directive('avaliarResultItem', function(QualisModal, Feedback, Flash, Principal) {
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
                $scope.admin = false;
                Principal.hasAuthority('ROLE_ADMIN').then(function(result) {
                    $scope.admin = result;
                });

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

                var contains = function(arr, key, value) {
                    var result = false;
                    arr.forEach(function(el) {
                        if (el[key] === value) {
                            result = true;
                        }
                    });
                    return result;
                };

                var clearFeedbackList = function() {
                    $scope.item.conceitos.forEach(function(c) {
                        // Remove a flag de feedback de conceitos anteriores
                        c.feedback = false;

                        // Se veio do modal, também remove da lista de conceitos
                        if (c.modal) {
                            var index = $scope.item.conceitos.indexOf(c);
                            $scope.item.conceitos.splice(index, 1);
                        }
                    });
                };

                var emitChange = function() {
                    Flash.create('success', '<strong>Feedback registrado!</strong> Atualize a página para recalcular as estatísticas');

                    // Emite o evento de feedback para atualização
                    // do status 'modificado' em avaliar-result.controller
                    $scope.$emit('silq:feedback');
                };

                $scope.feedbackDelete = function(item) {
                    clearFeedbackList();

                    var body = {
                        query: item.tituloVeiculo,
                        ano: item.ano
                    };

                    var feedbackRequest;
                    if (item.issn) {
                        feedbackRequest = Feedback.deletePeriodico(body);
                    } else {
                        feedbackRequest = Feedback.deleteEvento(body);
                    }

                    feedbackRequest.then(function() {
                        emitChange();
                    });
                };

                $scope.feedback = function(item, conceito) {
                    var query = item.tituloVeiculo;
                    var ano = item.ano;
                    var id = conceito.id;
                    var feedbackRequest;

                    clearFeedbackList();

                    // Adiciona a flag de feedback ao novo conceito
                    conceito.feedback = true;

                    // Se não possuir o conceito, adiciona-o à lista
                    // Isso acontece quando o conceito vem do modal
                    if (!contains($scope.item.conceitos, 'id', conceito.id)) {
                        conceito.modal = true;
                        $scope.item.conceitos.push(conceito);
                    }

                    if (item.issn) {
                        feedbackRequest = Feedback.periodico({
                            query: query,
                            ano: ano,
                            periodicoId: id
                        });
                    } else {
                        feedbackRequest = Feedback.evento({
                            query: query,
                            ano: ano,
                            eventoId: id
                        });
                    }

                    feedbackRequest.then(function() {
                        emitChange();
                    });
                };
            }
        };
    });
