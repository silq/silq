'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('avaliar', {
                parent: 'site',
                url: '/avaliar/{curriculumId:int}?{avaliarForm:json}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/avaliar/avaliar.html',
                        controller: 'AvaliarController'
                    }
                }
            });
    });
