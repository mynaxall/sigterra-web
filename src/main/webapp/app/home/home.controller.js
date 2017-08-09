(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$anchorScroll', '$location'];

    function HomeController ($scope, Principal, LoginService, $state, $location, $anchorScroll) {
        var vm = this;


        vm.showHaeder = false;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });


        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }


        function setFixedNavbar(){
            document.getElementById("default-navbar").className += " navbar-fixed-top";
        }

        setFixedNavbar();

    }
})();
