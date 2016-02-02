'use strict';

angular.module('silq2App')
    .controller('PrincipalController', function ($scope, $state, Principal, Upload, DadoGeral, Flash) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
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

        $scope.uploadFile = function(file) {
            file.progress = 1;
            file.status = 'Enviando arquivo...';

            Upload.upload({
                url: 'api/upload/',
                data: {file: file}
            }).then(function () {
                Flash.create('success', '<strong>Sucesso!</strong> Currículo enviado');
                loadDadosGerais();
                file.progress = null;
            }, function (resp) {
                file.status = 'Ocorreu um erro';
                console.log(resp);
            }, function (evt) {
                file.progress = parseInt(100.0 * evt.loaded / evt.total);
            });
        };

        $scope.atualizar = function() {
            $scope.dados = null;
        };

        $scope.remover = function() {
            DadoGeral.delete().$promise.then(function() {
                $scope.dados = null;
                Flash.create('success', '<strong>Sucesso!</strong> Currículo removido');
            });
        };
    });
