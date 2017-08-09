(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('address-book', {
            parent: 'entity',
            url: '/address-book?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AddressBooks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/address-book/address-books.html',
                    controller: 'AddressBookController',
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
        .state('address-book-detail', {
            parent: 'entity',
            url: '/address-book/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AddressBook'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/address-book/address-book-detail.html',
                    controller: 'AddressBookDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AddressBook', function($stateParams, AddressBook) {
                    return AddressBook.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'address-book',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('address-book-detail.edit', {
            parent: 'address-book-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address-book/address-book-dialog.html',
                    controller: 'AddressBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AddressBook', function(AddressBook) {
                            return AddressBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('address-book.new', {
            parent: 'address-book',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address-book/address-book-dialog.html',
                    controller: 'AddressBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createdDate: null,
                                modifiedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('address-book', null, { reload: 'address-book' });
                }, function() {
                    $state.go('address-book');
                });
            }]
        })
        .state('address-book.edit', {
            parent: 'address-book',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address-book/address-book-dialog.html',
                    controller: 'AddressBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AddressBook', function(AddressBook) {
                            return AddressBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('address-book', null, { reload: 'address-book' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('address-book.delete', {
            parent: 'address-book',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address-book/address-book-delete-dialog.html',
                    controller: 'AddressBookDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AddressBook', function(AddressBook) {
                            return AddressBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('address-book', null, { reload: 'address-book' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
