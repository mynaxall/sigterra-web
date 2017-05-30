(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .directive('checkPositionScroll', checkPositionScroll);

    checkPositionScroll.$inject = ['$window'];

    function checkPositionScroll($window) {
        return {
            restrict: 'A',
            link: function(scope, element) {
                var topPosition = 80;
                $window.addEventListener('scroll', function() {
                    if(angular.element($window).scrollTop() > topPosition) {
                        element.addClass('top-fix');
                    } else {
                        element.removeClass('top-fix');
                    }
                });
            }
        }
    }
})();
