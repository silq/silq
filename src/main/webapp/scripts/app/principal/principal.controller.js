'use strict';

angular.module('silq2App')
    .controller('PrincipalController', function ($scope, $state, Principal, Upload, DadoGeral, Cache, Flash) {
        Principal.identity().then(function(account) {
            $scope.account = account;
        });

        DadoGeral.get().then(function(resp) {
            var dados = resp.data;
            if (dados.nome) {
                $scope.dados = dados;
            } else {
                $scope.dados = null;
            }
        });

        $scope.atualizar = function() {
            $scope.dados = null;
        };

        $scope.remover = function() {
            DadoGeral.delete().then(function() {
                $scope.dados = null;
                Flash.create('success', '<strong>Sucesso!</strong> Currículo removido');
            });
        };

        // Dropbox configuration
        $scope.files = [];
        $scope.uploadConfig = {
            url: 'api/dado-geral'
        };
        $scope.uploaded = function(resp) {
            $scope.files = [];
            $scope.dados = resp.data;
            Cache.invalidate();
            Flash.create('success', '<strong>Sucesso!</strong> Currículo enviado');
        };
    });
