'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('main', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController'
                    }
                },
                resolve: {

                }
            })
            .state('about', {
                parent: 'site',
                url: '/about',
                data: {
                    authorities: [],
                    pageTitle: 'Sobre'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/about.html'
                    }
                },
                resolve: {

                }
            })
            .state('contact', {
                parent: 'site',
                url: '/contact',
                data: {
                    authorities: [],
                    pageTitle: 'Contato'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/contact.html'
                    }
                },
                resolve: {

                }
            })
            .state('termo', {
                parent: 'site',
                url: '/termo',
                data: {
                    authorities: [],
                    pageTitle: 'Termo de serviço'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/termo.html'
                    }
                },
                resolve: {

                }
            });
    });
