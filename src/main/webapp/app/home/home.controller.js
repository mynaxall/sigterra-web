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
        vm.activeTabId = 'landing-img-1';
        vm.activateTabMobileId = 'landing-img-5';
        vm.login = login;
        vm.register = register;
        vm.activateTab = activateTab;
        vm.activateTabMobile = activateTabMobile;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        function login() {
            LoginService.open();
        }


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

        function activateTab(id) {
            if(vm.activeTabId !== id) {
                document.getElementById(vm.activeTabId).classList.remove('activeLandingTab');
                document.getElementById(id).classList.add('activeLandingTab');
                vm.activeTabId = id;
            }

        }

        function activateTabMobile(id) {
            if(vm.activateTabMobileId !== id) {
                document.getElementById(vm.activateTabMobileId).classList.remove('activeLandingTab');
                document.getElementById(id).classList.add('activeLandingTab');
                vm.activateTabMobileId = id;
            }
        }

        setFixedNavbar();

    }
})();
