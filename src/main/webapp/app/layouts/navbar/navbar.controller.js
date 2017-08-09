(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', '$scope', '$location'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService, $scope, $location) {
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
        vm.gotoBottom = gotoBottom;
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

        function gotoBottom() {
            // set the location.hash to the id of
            // the element you wish to scroll to.
            var el = document.getElementById('howItWorks');
            el.scrollIntoView();
        };

        function howItWorks() {
            var el = document.getElementById('howItWorks');
            el.scrollIntoView();
        }

        function gotoFooter() {
            // set the location.hash to the id of
            // the element you wish to scroll to.
            var el = document.getElementById('sigFooter');
            el.scrollIntoView();
        };
    }
})();
