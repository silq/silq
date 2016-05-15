'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('avaliar-result', {
                parent: 'avaliar',
                url: '/result',
                params: {
                    'avaliarForm': {}
                },
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/avaliar/result/result.html',
                        controller: 'AvaliarResultController'
                    },
                    'content-header@': {
                        templateUrl: 'scripts/components/content-header/content-header.html',
                        controller: 'ContentHeaderController'
                    }
                }
            });
    });
