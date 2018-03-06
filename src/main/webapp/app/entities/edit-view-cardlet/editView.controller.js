(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('EditViewController', EditViewController)

    EditViewController.$inject = ['$scope'];

    function EditViewController($scope) {
        var vm = this;
        vm.activeTab = 1;

        $scope.setActive = function (id) {
            vm.activeTab = id;
        }

    }


})();
