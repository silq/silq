'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('avaliacao', {
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER']
                }
            })
            .state('avaliacao.individual', {
                parent: 'avaliacao',
                url: '/avaliar/individual/{id:int}?{avaliarForm:json}&{resultState:string}',
                params: {
                    'avaliarForm': {}
                },
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/avaliacao/individual/avaliacao-individual.html',
                        controller: 'AvaliacaoIndividualController'
                    }
                }
            })
            .state('avaliacao.livre', {
                parent: 'site',
                url: '/avaliar/livre',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/avaliacao/livre/avaliacao-livre.html',
                        controller: 'AvaliacaoLivreController'
                    }
                }
            })
            .state('avaliacao.livre.result', {
                parent: 'avaliacao.livre',
                url: '/{cacheId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/avaliacao/livre/result/result-list.html',
                        controller: 'AvaliacaoLivreResultController'
                    }
                }
            })
            .state('avaliacao.livre.result.detail', {
                parent: 'avaliacao.livre.result',
                url: '/{resultId}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/avaliacao/livre/result/result.html',
                        controller: 'AvaliacaoLivreResultDetailController'
                    }
                }
            });
    });
