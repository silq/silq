'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('principal', {
                parent: 'site',
                url: '/principal',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/principal/principal.html',
                        controller: 'PrincipalController'
                    }
                },
                resolve: {

                }
            });
    });
