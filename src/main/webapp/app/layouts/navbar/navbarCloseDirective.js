(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .directive('navbarCloseDirective', navbarCloseDirective);

    navbarCloseDirective.$inject = ['$document'];
    function navbarCloseDirective($document) {
        return {
            restrict: "A",
            link: function(scope, element) {
                $document.on('click', function(e) {
                    if(e.target.closest('.navbar-toggle') || e.target.closest('.navbar-collapse')) {
                        return;
                    } else {
                        if(angular.element('.navbar-collapse').hasClass('in')) {
                            angular.element('.navbar-toggle').trigger('click');
                        }
                    }
                });
            }
        }
    }
})();
