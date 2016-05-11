'use strict';

angular.module('silq2App')
    .directive('avaliarResultItem', function() {
        return {
            restrict: 'E',
            scope: {
                item: '=item',
                filter: '=filter'
            },
            templateUrl: 'scripts/components/avaliar/avaliar-result-item.html',
            link: function($scope) {
                $scope.mais = false;
                $scope.verMais = function(flag) {
                    $scope.mais = flag;
                };
            }
        };
    });
