'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('result', {
                parent: 'avaliar',
                url: '/result/{cacheId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/avaliar/result/result.html',
                        controller: 'ResultController'
                    },
                    'content-header@': {
                        templateUrl: 'scripts/components/content-header/content-header.html',
                        controller: 'ContentHeaderController'
                    }
                },
                resolve: {

                }
            });
    });
