'use strict';

angular.module('silq2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('grupo', {
                parent: 'site',
                url: '/grupos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Grupos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/grupo/grupos.html',
                        controller: 'GrupoController'
                    }
                }
            })
            .state('grupo.detail', {
                parent: 'grupo',
                url: '/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Dados do grupo'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/grupo/grupo-detail.html',
                        controller: 'GrupoDetailController'
                    }
                }
            })
            .state('grupo.avaliacao', {
                parent: 'grupo.detail',
                url: '/avaliacao?{avaliarForm:json}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Avaliação de grupo'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/grupo/grupo-avaliacao.html',
                        controller: 'GrupoAvaliacaoController'
                    }
                }
            })
            .state('grupo.new', {
                parent: 'grupo',
                url: '/-/new',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Novo grupo'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/grupo/grupo-dialog.html',
                        controller: 'GrupoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nomeGrupo: null,
                                    nomeInstituicao: null,
                                    nomeArea: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('grupo', null, { reload: true });
                    }, function() {
                        $state.go('grupo');
                    });
                }]
            })
            .state('grupo.edit', {
                parent: 'grupo.detail',
                url: '/edit',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Editar grupo'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/grupo/grupo-dialog.html',
                        controller: 'GrupoDialogController',
                        size: 'lg'
                    }).result.then(function() {
                        $state.go('grupo.detail', {id: $stateParams.id}, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('grupo.delete', {
                parent: 'grupo.detail',
                url: '/delete',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Deletar grupo'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/grupo/grupo-delete-dialog.html',
                        controller: 'GrupoDeleteController',
                        size: 'md'
                    }).result.then(function() {
                        $state.go('grupo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    });
