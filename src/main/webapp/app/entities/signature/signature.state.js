(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('signature', {
            parent: 'entity',
            url: '/signature?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Signatures'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/signature/signatures.html',
                    controller: 'SignatureController',
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
        .state('signature-detail', {
            parent: 'entity',
            url: '/signature/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Signature'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/signature/signature-detail.html',
                    controller: 'SignatureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Signature', function($stateParams, Signature) {
                    return Signature.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'signature',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('signature-detail.edit', {
            parent: 'signature-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signature/signature-dialog.html',
                    controller: 'SignatureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Signature', function(Signature) {
                            return Signature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('signature.new', {
            parent: 'signature',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signature/signature-dialog.html',
                    controller: 'SignatureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                path: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('signature', null, { reload: 'signature' });
                }, function() {
                    $state.go('signature');
                });
            }]
        })
        .state('signature.edit', {
            parent: 'signature',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signature/signature-dialog.html',
                    controller: 'SignatureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Signature', function(Signature) {
                            return Signature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('signature', null, { reload: 'signature' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('signature.delete', {
            parent: 'signature',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/signature/signature-delete-dialog.html',
                    controller: 'SignatureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Signature', function(Signature) {
                            return Signature.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('signature', null, { reload: 'signature' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
