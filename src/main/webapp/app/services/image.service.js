

(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .factory('ImageService', ImageService);

    ImageService.$inject = ['MAX_IMAGE'];

    function ImageService (MAX_IMAGE) {
        var service = {
            imageSize: imageSize
        };

        return service;

        function imageSize (bounds) {
                var width = bounds.right - bounds.left;
                var height = bounds.top - bounds.bottom;
                if (width > MAX_IMAGE || height > MAX_IMAGE) {
                    if (width > height) {
                        var dif = width / MAX_IMAGE;
                        height = height / dif;
                        width = MAX_IMAGE
                    } else {
                        var dif = height / MAX_IMAGE;
                        width = width / dif;
                        height = MAX_IMAGE
                    }

                }
            return {'width': width, 'height': height};
        }
    }
})();


