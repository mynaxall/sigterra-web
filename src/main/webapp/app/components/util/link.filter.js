(function () {
    'use strict';

    angular
        .module('sigterraWebApp')
        .filter('link', function () {
            return function (value) {
                if (!value || value.startsWith('http://' || value.startsWith('https://'))) return '';
                return 'http://' + value;

            };
        });
})();
