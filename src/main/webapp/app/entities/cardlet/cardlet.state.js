(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cardlet', {
            parent: 'entity',
            url: '/cardlet?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Cardlets'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cardlet/cardlets.html',
                    controller: 'CardletController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('cardlet-detail', {
            parent: 'entity',
            url: '/cardlet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Cardlet'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cardlet/cardlet-detail.html',
                    controller: 'CardletDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Cardlet', function($stateParams, Cardlet) {
                    return Cardlet.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cardlet',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cardlet-detail.edit', {
            parent: 'cardlet-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cardlet/cardlet-dialog.html',
                    controller: 'CardletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cardlet', function(Cardlet) {
                            return Cardlet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cardlet.new', {
            parent: 'cardlet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cardlet/cardlet-dialog.html',
                    controller: 'CardletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                createdDate: null,
                                modifiedDate: null,
                                active: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cardlet', null, { reload: 'cardlet' });
                }, function() {
                    $state.go('cardlet');
                });
            }]
        })
        .state('cardlet.edit', {
            parent: 'cardlet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cardlet/cardlet-dialog.html',
                    controller: 'CardletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cardlet', function(Cardlet) {
                            return Cardlet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cardlet', null, { reload: 'cardlet' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cardlet.delete', {
            parent: 'cardlet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cardlet/cardlet-delete-dialog.html',
                    controller: 'CardletDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cardlet', function(Cardlet) {
                            return Cardlet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cardlet', null, { reload: 'cardlet' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
