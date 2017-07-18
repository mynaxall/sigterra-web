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


        function setFixedNavbar(){
            document.getElementById("default-navbar").className += " navbar-fixed-top";
        }

        setFixedNavbar();

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

        $scope.gotoBottom = function() {
            // set the location.hash to the id of
            // the element you wish to scroll to.
            var el = document.getElementById('howItWorks');
            el.scrollIntoView();
        };
    }
})();
