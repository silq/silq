'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('qualis', {
                parent: 'site',
                url: '/qualis',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/qualis/qualis.html',
                        controller: 'QualisController'
                    }
                }
            });
    });
