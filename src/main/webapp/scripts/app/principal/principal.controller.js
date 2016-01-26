'use strict';

angular.module('silq2App')
    .controller('PrincipalController', function ($scope, $state, Principal, Upload) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.uploadFile = function(file) {
            file.progress = 1;
            file.status = 'Enviando arquivo...';

            Upload.upload({
                url: 'api/upload/',
                data: {file: file}
            }).then(function (resp) {
                file.status = 'Arquivo enviado!';
                console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
            }, function (resp) {
                file.status = 'Ocorreu um erro.';
                console.log(resp);
            }, function (evt) {
                file.progress = parseInt(100.0 * evt.loaded / evt.total);
            });
        };
    });
