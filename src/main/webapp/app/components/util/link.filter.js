(function () {
    'use strict';

    angular
        .module('sigterraWebApp')
        .filter('link', function () {
            return function (value) {
                if (!value) {
                    return ''
                } else if (value.startsWith('http://') || value.startsWith('https://')) {
                    return value;
                } else {
                    return 'http://' + value;
                }
            };
        });
})();
