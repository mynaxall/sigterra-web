(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('registerSuccess', {
            parent: 'account',
            url: '/registerSuccess',
            data: {
                authorities: [],
                pageTitle: 'Registration'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/registerSuccess/registerSuccess.html',
                    controller: 'RegisterSuccessController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
