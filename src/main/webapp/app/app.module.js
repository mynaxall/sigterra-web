(function() {
    'use strict';

    angular
        .module('sigterraWebApp', [
            'ngStorage',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'dndLists',
            'ngclipboard',
            'base64',
            'xeditable',
            'ngOnload',
            'monospaced.elastic',
            'ngMask',
            'textAngular',
            'dibari.angular-ellipsis',
            'angular-img-cropper',
            'angular-carousel'
        ])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
