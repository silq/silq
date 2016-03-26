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
                    },
                    'content-header@': {
                        templateUrl: 'scripts/components/content-header/content-header.html',
                        controller: 'ContentHeaderController'
                    }
                },
                resolve: {
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
                    },
                    'content-header@': {
                        templateUrl: 'scripts/components/content-header/content-header.html',
                        controller: 'ContentHeaderController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Grupo', function($stateParams, Grupo) {
                        return Grupo.get({id : $stateParams.id});
                    }]
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
                parent: 'grupo',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Editar grupo'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/grupo/grupo-dialog.html',
                        controller: 'GrupoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Grupo', function(Grupo) {
                                return Grupo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('grupo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('grupo.delete', {
                parent: 'grupo',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Deletar grupo'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/grupo/grupo-delete-dialog.html',
                        controller: 'GrupoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Grupo', function(Grupo) {
                                return Grupo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('grupo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    });
