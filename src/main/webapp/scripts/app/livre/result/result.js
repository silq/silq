'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('resultLivre', {
                parent: 'avaliar-livre',
                url: '/result/{cacheId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/livre/result/result-list.html',
                        controller: 'ResultLivreListController'
                    }
                }
            })
            .state('resultLivre.detail', {
                parent: 'resultLivre',
                url: '/{resultId}',
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
