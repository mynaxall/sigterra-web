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
            'ngImgCrop',
            'dndLists',
            'ngclipboard',
            'base64',
            'xeditable',
            'ngOnload',
            'monospaced.elastic',
            'ngMask',
            'textAngular',
            'dibari.angular-ellipsis',
            'angular-img-cropper'
        ])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
