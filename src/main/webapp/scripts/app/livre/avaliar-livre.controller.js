'use strict';

angular.module('silq2App')
    .controller('AvaliarLivreController', function ($scope, $state, Similarity, Upload, Flash) {
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
            var hasValidCurriculum = false; // Se ao menos um currículo válido foi enviado
            var hasUploadingCurriculum = false; // Se existe algum currículo em processo de envio

            $scope.files.forEach(function(file) {
                if (file.status === 'uploading') {
                    hasUploadingCurriculum = true;
                    return;
                }

                if (file.status === 'success') {
                    hasValidCurriculum = true;
                    return;
                }
            });

            if (!hasValidCurriculum) {
                Flash.create('warning', 'Selecione ao menos um currículo válido para avaliar');
                return;
            }

            if (hasUploadingCurriculum) {
                Flash.create('warning', '<strong>Uploads em andamento!</strong> Aguarde o carregamento dos currículos terminar.');
                return;
            }

            Similarity.avaliar($scope.avaliarForm).then(function(response) {
                Flash.create('success', 'Avaliação concluída');
                $state.go('result-livre', {
                    cacheId: cacheId
                });
            }).catch(function(err) {
                console.error(err);
            });
        };
    });
