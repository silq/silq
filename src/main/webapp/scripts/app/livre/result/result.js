'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('result-livre', {
                parent: 'avaliar-livre',
                url: '/result/{cacheId}/{resultId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/livre/result/result.html',
                        controller: 'ResultLivreController'
                    }
                }
            });
    });
