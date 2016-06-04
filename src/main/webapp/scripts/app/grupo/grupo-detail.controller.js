'use strict';

angular.module('silq2App')
    .controller('GrupoDetailController', function ($scope, $state, $stateParams, Grupo, Cache, Flash, AvaliarConfigModal) {
        Grupo.get($stateParams.id).then(function(resp) {
            $scope.grupo = resp.data;
        });

        $scope.files = [];
        $scope.uploadConfig = {
            url: 'api/grupos/' + $stateParams.id + '/addPesquisador'
        };

        $scope.uploaded = function(resp) {
            $scope.grupo.pesquisadores.push(resp.data);
            Cache.invalidate();
            Flash.create('success', '<strong>Sucesso!</strong> Pesquisador(es) adicionado(s)');
        };

        $scope.avaliarPesquisador = function(pesquisador) {
            AvaliarConfigModal.open({
                area: $scope.grupo.nomeArea
            }).then(function(form) {
                $state.go('avaliacao.individual', {
                    id: pesquisador.id,
                    avaliarForm: form
                });
            });
        };

        $scope.avaliarGrupo = function() {
            AvaliarConfigModal.open({
                area: $scope.grupo.nomeArea
            }).then(function(form) {
                $state.go('grupo.avaliacao', {
                    id: $scope.grupo.id,
                    avaliarForm: form
                });
            });
        };

        $scope.removePesquisador = function(pesquisador) {
            Grupo.removePesquisador($stateParams.id, pesquisador.id).then(function() {
                var index = $scope.grupo.pesquisadores.indexOf(pesquisador);
                index != -1 && $scope.grupo.pesquisadores.splice(index, 1);
                Flash.create('success', '<strong>Sucesso!</strong> O pesquisador "'+pesquisador.nome+'" foi removido do grupo.');
            }).catch(function(err) {
                console.error(err);
            });
        };
    });
