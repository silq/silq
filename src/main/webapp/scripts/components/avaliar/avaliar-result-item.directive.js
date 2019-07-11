'use strict';

angular.module('silq2App')
    .directive('avaliarResultItem', function(QualisModal, Feedback, Flash, Principal) {
        return {
            restrict: 'E',
            scope: {
                item: '=item',
                filter: '=filter',
                evaluationSystem: '=evaluationSystem',
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

                $scope.obterNotaQUALIS = function(ano, conceitos) {
                    var conceito = _.find(conceitos, function(p){ return p.ano === ano; });
                    return conceito != null ? conceito.conceito : 'Não há avaliação Qualis neste ano' ;
                };

                $scope.obterNotaSICLAP = function(ano, conceitos) {
                    var CONCEITO_ENUM = {
                        A1: 8,
                        A2: 7,
                        B1: 6,
                        B2: 5,
                        B3: 4,
                        B4: 3,
                        B5: 2,
                        C: 1,
                        NA: 0,
                        properties: {
                            8: {name: "A1"},
                            7: {name: "A2"},
                            6: {name: "B1"},
                            5: {name: "B2"},
                            4: {name: "B3"},
                            3: {name: "B4"},
                            2: {name: "B5"},
                            1: {name: "C"},
                            0: {name: "NA"}
                        }
                    };
                    var primeiroConceito;
                    var segundoConceito = null;
                    var primeirosConceitos = _.filter(conceitos, function(p){ return p.ano < ano; });
                    primeiroConceito = getConceito(primeirosConceitos);
                    if(primeiroConceito != null){
                        var anoPrimeiroConceito = primeiroConceito.ano;
                        var segundosConceitos = _.filter(conceitos, function(p){ return p.ano < anoPrimeiroConceito; });
                        segundoConceito = getConceito(segundosConceitos);
                    }
                    primeiroConceito = CONCEITO_ENUM[primeiroConceito != null ? primeiroConceito.conceito : "NA"];
                    segundoConceito = CONCEITO_ENUM[segundoConceito != null ? segundoConceito.conceito : "NA"];
                    var result = CONCEITO_ENUM.properties[Math.max(primeiroConceito, segundoConceito)].name;
                    return result === 'NA' ? 'Não há avaliação Siclap para este ano' : result;
                };

                var getConceito = function(conceitos){
                    var conceito = null;
                    if (conceitos.length > 0) {
                        conceitos = _.sortBy(conceitos, ['feedback']).reverse();
                        if (!conceitos[0].feedback){
                            conceitos = _.sortBy(conceitos, ['similaridade', 'ano']).reverse();
                        }
                        conceito = conceitos[0]
                    }
                    return conceito;
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
                    $scope.item.feedbackNegativo = false;
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
                    var id = conceito ? conceito.id : null;
                    var feedbackRequest;

                    clearFeedbackList();

                    if (conceito) {
                        // Adiciona a flag de feedback ao novo conceito
                        conceito.feedback = true;

                        // Se não possuir o conceito, adiciona-o à lista
                        // Isso acontece quando o conceito vem do modal
                        if (!contains($scope.item.conceitos, 'id', conceito.id)) {
                            conceito.modal = true;
                            $scope.item.conceitos.push(conceito);
                        }
                    } else {
                        // é um feedback negativo
                        $scope.item.feedbackNegativo = true;
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
