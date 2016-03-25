'use strict';

angular.module('silq2App')
    .directive('dropbox', function(Upload, Flash) {
        return {
            restrict: 'E',
            scope: {
                config: '=config',
                files: '=files',
                success: '=?success',
                multiple: '=?multiple'
            },
            transclude: true,
            templateUrl: 'scripts/components/dropbox/dropbox.html',
            compile: function(element, attrs) {
                attrs.success = angular.isDefined(attrs.success) ? attrs.success : function() {};
                attrs.multiple = angular.isDefined(attrs.multiple) ? attrs.multiple : 'true';
                console.log(attrs.multiple);
            },
            controller: function($scope) {
                if (!$scope.config.data) {
                    $scope.config.data = {};
                }

                $scope.uploadFiles = function(files) {
                    if (!files) return;
                    files.forEach(function(file) {
                        $scope.files.push(file);
                        file.uploading = true;

                        var params = JSON.parse(JSON.stringify($scope.config)); // clone config
                        params.data.file = file;

                        Upload.upload(params)
                            .then(function (resp) {
                                file.uploading = false;
                                $scope.success(resp);
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
