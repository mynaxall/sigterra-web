(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', '$scope', '$location', '$anchorScroll'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService, $scope, $location, $anchorScroll) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        $scope.homeLogo = false;

        $scope.$watch('$location.path()', function(value){
            if(($location.path()) === "/"){
                $scope.homeLogo = true;
            }else{
                $scope.homeLogo = false;
            }
        })

        $scope.$on('$locationChangeStart', function(event) {
            if(($location.path()) === "/"){
                $scope.homeLogo = true;

            }else{
                $scope.homeLogo = false;
            }
        });



        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.gotoFooter = gotoFooter;
        vm.howItWorks = howItWorks;
        vm.$state = $state;

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }

        function howItWorks() {
            $location.hash('howItWorks');

            // call $anchorScroll()
            $anchorScroll();
        }

        function gotoFooter() {
            $location.hash('contacts');

            // call $anchorScroll()
            $anchorScroll();

        };
    }
})();
