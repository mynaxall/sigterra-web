(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('item-data', {
            parent: 'entity',
            url: '/item-data?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ItemData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/item-data/item-data.html',
                    controller: 'ItemDataController',
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
        .state('item-data-detail', {
            parent: 'entity',
            url: '/item-data/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ItemData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/item-data/item-data-detail.html',
                    controller: 'ItemDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ItemData', function($stateParams, ItemData) {
                    return ItemData.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'item-data',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('item-data-detail.edit', {
            parent: 'item-data-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-data/item-data-dialog.html',
                    controller: 'ItemDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ItemData', function(ItemData) {
                            return ItemData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('item-data.new', {
            parent: 'item-data',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-data/item-data-dialog.html',
                    controller: 'ItemDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                createdDate: null,
                                modifiedDate: null,
                                firstImage: null,
                                secondImage: null,
                                thirdImage: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('item-data', null, { reload: 'item-data' });
                }, function() {
                    $state.go('item-data');
                });
            }]
        })
        .state('item-data.edit', {
            parent: 'item-data',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-data/item-data-dialog.html',
                    controller: 'ItemDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ItemData', function(ItemData) {
                            return ItemData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('item-data', null, { reload: 'item-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('item-data.delete', {
            parent: 'item-data',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-data/item-data-delete-dialog.html',
                    controller: 'ItemDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ItemData', function(ItemData) {
                            return ItemData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('item-data', null, { reload: 'item-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
