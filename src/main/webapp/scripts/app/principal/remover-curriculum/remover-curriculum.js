'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('remover-curriculum', {
                parent: 'site',
                url: '/principal/remover-curriculum',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/principal/remover-curriculum/remover-curriculum.html',
                        controller: 'RemoverCurriculumController'
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
