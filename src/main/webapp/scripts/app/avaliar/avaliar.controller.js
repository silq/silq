'use strict';

angular.module('silq2App')
    .controller('AvaliarController', function ($scope, Similarity, Upload, Flash) {
        $scope.files = [];
        $scope.avaliarForm = {
            nivelSimilaridade: '0.6'
        };

        $scope.submit = function() {
            if ($scope.files.length <= 0) {
                Flash.create('danger', 'Selecione ao menos um currículo para avaliar');
                return;
            }

            $scope.files.forEach(function(file) {
                console.log(file);
            });

            Flash.create('info', 'Em desenvolvimento!');
        };

        $scope.uploadFiles = function(files) {
            if (!files) return;
            files.forEach(function(file) {
                $scope.files.push(file);
                file.uploading = true;

                Upload.upload({
                    url: 'api/dado-geral/',
                    data: {file: file}
                }).then(function () {
                    file.uploading = false;
                }, function (resp) {
                    Flash.create('danger', '<strong>Ops!</strong> Ocorreu um erro');
                    file.uploading = false;
                    console.error(resp);
                }, function (evt) {
                    file.progress = parseInt(100.0 * evt.loaded / evt.total);
                });
            });
        };
    });
