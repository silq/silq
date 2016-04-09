'use strict';

angular.module('silq2App')
    .controller('PrincipalController', function ($scope, Principal, Upload, DadoGeral, Cache, Flash) {
        $scope.status = 'loading';

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
            Cache.invalidate();
            Flash.create('success', '<strong>Sucesso!</strong> Currículo enviado');
        };
    });
