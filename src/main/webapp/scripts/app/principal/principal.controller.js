'use strict';

angular.module('silq2App')
    .controller('PrincipalController', function ($scope, $state, Principal, Upload, DadoGeral, Flash) {
        Principal.identity().then(function(account) {
            $scope.account = account;
        });

        var loadDadosGerais = function() {
            DadoGeral.get().$promise.then(function(dados) {
                if (dados.nome) {
                    $scope.dados = dados;
                } else {
                    $scope.dados = null;
                }
            });
        };
        loadDadosGerais();

        $scope.atualizar = function() {
            $scope.dados = null;
        };

        // Dropbox configuration
        $scope.files = [];
        $scope.uploadConfig = {
            url: 'api/dado-geral'
        };
        $scope.uploaded = function(resp) {
            console.log(resp);
            $scope.files = [];
            loadDadosGerais();
            Flash.create('success', '<strong>Sucesso!</strong> Currículo enviado');
        };
    });
