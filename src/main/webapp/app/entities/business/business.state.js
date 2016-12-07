(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('business', {
            parent: 'entity',
            url: '/business?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Businesses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/business/businesses.html',
                    controller: 'BusinessController',
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
        .state('business-detail', {
            parent: 'entity',
            url: '/business/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Business'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/business/business-detail.html',
                    controller: 'BusinessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Business', function($stateParams, Business) {
                    return Business.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'business',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('business-detail.edit', {
            parent: 'business-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business/business-dialog.html',
                    controller: 'BusinessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Business', function(Business) {
                            return Business.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('business.new', {
            parent: 'business',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business/business-dialog.html',
                    controller: 'BusinessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                createdDate: null,
                                modifiedDate: null,
                                pisition: null,
                                icon: null,
                                userName: null,
                                jobPosition: null,
                                companyName: null,
                                companySite: null,
                                email: null,
                                phone: null,
                                address: null,
                                twitter: null,
                                facebook: null,
                                google: null,
                                linkedIn: null,
                                photo: null,
                                mainColor: null,
                                color: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('business', null, { reload: 'business' });
                }, function() {
                    $state.go('business');
                });
            }]
        })
        .state('business.edit', {
            parent: 'business',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business/business-dialog.html',
                    controller: 'BusinessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Business', function(Business) {
                            return Business.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('business', null, { reload: 'business' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('business.delete', {
            parent: 'business',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business/business-delete-dialog.html',
                    controller: 'BusinessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Business', function(Business) {
                            return Business.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('business', null, { reload: 'business' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
