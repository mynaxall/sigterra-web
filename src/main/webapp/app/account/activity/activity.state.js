(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('activity', {
                parent: 'account',
                url: '/activity',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Activity'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/account/activity/activity.html',
                        controller: 'ActivityController',
                        controllerAs: 'vm'
                    }
                }
            })

    }
})();
