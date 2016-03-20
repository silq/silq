'use strict';

angular.module('silq2App')
    .directive('dropbox', function(Upload) {
        return {
            restrict: 'E',
            scope: {
                config: '=config',
                files: '=files'
            },
            transclude: true,
            templateUrl: 'scripts/components/dropbox/dropbox.html',
            link: function($scope) {
                $scope.uploadFiles = function(files) {
                    if (!files) return;
                    files.forEach(function(file) {
                        $scope.files.push(file);
                        file.uploading = true;

                        if (!$scope.config.data) {
                            $scope.config.data = {
                                file: file
                            };
                        } else {
                            $scope.config.data.file = file;
                        }

                        Upload.upload($scope.config)
                            .then(function (resp) {
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
            }
        };
    });
