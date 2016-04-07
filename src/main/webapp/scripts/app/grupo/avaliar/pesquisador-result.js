'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pesquisador-result', {
                parent: 'grupo',
                url: '/{grupoId}/result/{pesquisadorId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/grupo/avaliar/pesquisador-result.html',
                        controller: 'PesquisadorResultController'
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
