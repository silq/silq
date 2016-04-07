'use strict';

angular.module('silq2App')
    .controller('PrincipalController', function ($scope, $state, Principal, Upload, DadoGeral, Flash) {
        $scope.status = 'loading';
        
        Principal.identity().then(function(account) {
            $scope.account = account;
        });

        DadoGeral.get().$promise.then(function(dados) {
            if (dados.nome) {
                $scope.dados = dados;
            } else {
                $scope.dados = null;
            }

            $scope.status = 'success';
        });

        $scope.atualizar = function() {
            $scope.dados = null;
        };

        // Dropbox configuration
        $scope.files = [];
        $scope.uploadConfig = {
            url: 'api/dado-geral'
        };
        $scope.uploaded = function(resp) {
            $scope.files = [];
            $scope.dados = resp.data;
            Flash.create('success', '<strong>Sucesso!</strong> Currículo enviado');
        };
    });
