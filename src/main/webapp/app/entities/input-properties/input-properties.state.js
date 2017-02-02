(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('input-properties', {
            parent: 'entity',
            url: '/input-properties?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'InputProperties'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/input-properties/input-properties.html',
                    controller: 'InputPropertiesController',
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
        .state('input-properties-detail', {
            parent: 'entity',
            url: '/input-properties/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'InputProperties'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/input-properties/input-properties-detail.html',
                    controller: 'InputPropertiesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'InputProperties', function($stateParams, InputProperties) {
                    return InputProperties.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'input-properties',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('input-properties-detail.edit', {
            parent: 'input-properties-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/input-properties/input-properties-dialog.html',
                    controller: 'InputPropertiesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InputProperties', function(InputProperties) {
                            return InputProperties.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('input-properties.new', {
            parent: 'input-properties',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/input-properties/input-properties-dialog.html',
                    controller: 'InputPropertiesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                value: null,
                                bold: null,
                                italic: null,
                                underline: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('input-properties', null, { reload: 'input-properties' });
                }, function() {
                    $state.go('input-properties');
                });
            }]
        })
        .state('input-properties.edit', {
            parent: 'input-properties',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/input-properties/input-properties-dialog.html',
                    controller: 'InputPropertiesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InputProperties', function(InputProperties) {
                            return InputProperties.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('input-properties', null, { reload: 'input-properties' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('input-properties.delete', {
            parent: 'input-properties',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/input-properties/input-properties-delete-dialog.html',
                    controller: 'InputPropertiesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InputProperties', function(InputProperties) {
                            return InputProperties.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('input-properties', null, { reload: 'input-properties' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
