'use strict';

angular.module('silq2App')
    .controller('AvaliarController', function ($scope, Similarity, Upload, Flash) {
        var cacheId = Math.random().toString(36).substring(7);

        $scope.files = [];
        $scope.avaliarForm = {
            nivelSimilaridade: '0.6',
            cacheId: cacheId
        };
        $scope.results = null;

        $scope.uploadFiles = function(files) {
            if (!files) return;
            files.forEach(function(file) {
                $scope.files.push(file);
                file.uploading = true;

                Upload.upload({
                    url: 'api/avaliar/upload',
                    data: {
                        file: file,
                        cacheId: cacheId
                    }
                }).then(function (resp) {
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

        $scope.submit = function() {
            if ($scope.files.length <= 0) {
                // Flash.create('warning', 'Selecione ao menos um currículo para avaliar');
                // return; // TODO
            }

            var abort = false;
            $scope.files.forEach(function(file) {
                if (file.uploading === true) {
                    abort = true;
                    return;
                }
            });

            if (abort) {
                Flash.create('warning', '<strong>Uploads em andamento!</strong> Aguarde o carregamento dos currículos terminar.');
                return;
            }

            Similarity.avaliar($scope.avaliarForm).then(function(response) {
                $scope.results = response.data;
                Flash.create('success', 'Avaliação concluída');
            }).catch(function(err) {
                console.error(err);
            });
        };
    });
