(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('BusinessDialogController', BusinessDialogController);

    BusinessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Business', 'Cardlet', 'TabType', 'InputProperties'];

    function BusinessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Business, Cardlet, TabType, InputProperties) {
        var vm = this;

        vm.business = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.cardlets = Cardlet.query();
        vm.tabtypes = TabType.query();
        vm.usernames = InputProperties.query({filter: 'business-is-null'});
        $q.all([vm.business.$promise, vm.usernames.$promise]).then(function() {
            if (!vm.business.userName || !vm.business.userName.id) {
                return $q.reject();
            }
            return InputProperties.get({id : vm.business.userName.id}).$promise;
        }).then(function(userName) {
            vm.usernames.push(userName);
        });
        vm.useremails = InputProperties.query({filter: 'business-is-null'});
        $q.all([vm.business.$promise, vm.useremails.$promise]).then(function() {
            if (!vm.business.userEmail || !vm.business.userEmail.id) {
                return $q.reject();
            }
            return InputProperties.get({id : vm.business.userEmail.id}).$promise;
        }).then(function(userEmail) {
            vm.useremails.push(userEmail);
        });
        vm.phones = InputProperties.query({filter: 'business-is-null'});
        $q.all([vm.business.$promise, vm.phones.$promise]).then(function() {
            if (!vm.business.phone || !vm.business.phone.id) {
                return $q.reject();
            }
            return InputProperties.get({id : vm.business.phone.id}).$promise;
        }).then(function(phone) {
            vm.phones.push(phone);
        });
        vm.addresses = InputProperties.query({filter: 'business-is-null'});
        $q.all([vm.business.$promise, vm.addresses.$promise]).then(function() {
            if (!vm.business.address || !vm.business.address.id) {
                return $q.reject();
            }
            return InputProperties.get({id : vm.business.address.id}).$promise;
        }).then(function(address) {
            vm.addresses.push(address);
        });
        vm.companies = InputProperties.query({filter: 'business-is-null'});
        $q.all([vm.business.$promise, vm.companies.$promise]).then(function() {
            if (!vm.business.company || !vm.business.company.id) {
                return $q.reject();
            }
            return InputProperties.get({id : vm.business.company.id}).$promise;
        }).then(function(company) {
            vm.companies.push(company);
        });
        vm.sites = InputProperties.query({filter: 'business-is-null'});
        $q.all([vm.business.$promise, vm.sites.$promise]).then(function() {
            if (!vm.business.site || !vm.business.site.id) {
                return $q.reject();
            }
            return InputProperties.get({id : vm.business.site.id}).$promise;
        }).then(function(site) {
            vm.sites.push(site);
        });
        vm.jobs = InputProperties.query({filter: 'business-is-null'});
        $q.all([vm.business.$promise, vm.jobs.$promise]).then(function() {
            if (!vm.business.job || !vm.business.job.id) {
                return $q.reject();
            }
            return InputProperties.get({id : vm.business.job.id}).$promise;
        }).then(function(job) {
            vm.jobs.push(job);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.business.id !== null) {
                Business.update(vm.business, onSaveSuccess, onSaveError);
            } else {
                Business.save(vm.business, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sigterraWebApp:businessUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.modifiedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
