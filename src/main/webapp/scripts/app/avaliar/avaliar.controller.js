'use strict';

angular.module('silq2App')
    .controller('AvaliarController', function ($scope, $state, Similarity, Upload, Flash) {
        var cacheId = Math.random().toString(36).substring(7);

        $scope.files = [];
        $scope.avaliarForm = {
            nivelSimilaridade: '0.6',
            cacheId: cacheId
        };

        $scope.uploadConfig = {
            url: 'api/avaliar/upload',
            data: {
                cacheId: cacheId
            }
        };

        $scope.submit = function() {
            if ($scope.files.length <= 0) {
                Flash.create('warning', 'Selecione ao menos um currículo para avaliar');
                return;
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
                Flash.create('success', 'Avaliação concluída');
                $state.go('result', {
                    cacheId: cacheId
                });
            }).catch(function(err) {
                console.error(err);
            });
        };
    });
