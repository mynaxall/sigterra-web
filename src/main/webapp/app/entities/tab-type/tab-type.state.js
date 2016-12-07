(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tab-type', {
            parent: 'entity',
            url: '/tab-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TabTypes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tab-type/tab-types.html',
                    controller: 'TabTypeController',
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
        .state('tab-type-detail', {
            parent: 'entity',
            url: '/tab-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TabType'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tab-type/tab-type-detail.html',
                    controller: 'TabTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TabType', function($stateParams, TabType) {
                    return TabType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tab-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tab-type-detail.edit', {
            parent: 'tab-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tab-type/tab-type-dialog.html',
                    controller: 'TabTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TabType', function(TabType) {
                            return TabType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tab-type.new', {
            parent: 'tab-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tab-type/tab-type-dialog.html',
                    controller: 'TabTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                type: null,
                                path: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tab-type', null, { reload: 'tab-type' });
                }, function() {
                    $state.go('tab-type');
                });
            }]
        })
        .state('tab-type.edit', {
            parent: 'tab-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tab-type/tab-type-dialog.html',
                    controller: 'TabTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TabType', function(TabType) {
                            return TabType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tab-type', null, { reload: 'tab-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tab-type.delete', {
            parent: 'tab-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tab-type/tab-type-delete-dialog.html',
                    controller: 'TabTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TabType', function(TabType) {
                            return TabType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tab-type', null, { reload: 'tab-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
