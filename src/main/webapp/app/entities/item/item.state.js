(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('item', {
            parent: 'entity',
            url: '/item?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Items'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/item/items.html',
                    controller: 'ItemController',
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
        .state('item-detail', {
            parent: 'entity',
            url: '/item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Item'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/item/item-detail.html',
                    controller: 'ItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Item', function($stateParams, Item) {
                    return Item.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('item-detail.edit', {
            parent: 'item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item/item-dialog.html',
                    controller: 'ItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Item', function(Item) {
                            return Item.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('item.new', {
            parent: 'item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item/item-dialog.html',
                    controller: 'ItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                icon: null,
                                createdDate: null,
                                modifiDate: null,
                                mainColor: null,
                                color: null,
                                pisition: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('item', null, { reload: 'item' });
                }, function() {
                    $state.go('item');
                });
            }]
        })
        .state('item.edit', {
            parent: 'item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item/item-dialog.html',
                    controller: 'ItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Item', function(Item) {
                            return Item.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('item', null, { reload: 'item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('item.delete', {
            parent: 'item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item/item-delete-dialog.html',
                    controller: 'ItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Item', function(Item) {
                            return Item.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('item', null, { reload: 'item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
