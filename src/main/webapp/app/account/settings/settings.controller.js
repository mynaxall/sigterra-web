(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .directive('pwCheck', [function () {
            return {
                require: 'ngModel',
                link: function (scope, elem, attrs, ctrl) {
                    var firstPassword = '#' + attrs.pwCheck;
                    elem.add(firstPassword).on('keyup', function () {
                        scope.$apply(function () {
                            var v = elem.val()===$(firstPassword).val();
                            ctrl.$setValidity('pwmatch', v);
                        });
                    });
                }
            }
        }])
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', '$scope', '$http'];

    function SettingsController (Principal, Auth, $scope, $http) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */


        Principal.identity().then(function(account) {
            vm.settingsAccount = account;
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }


        vm.changePassword = changePassword;
        vm.doNotMatch = null;
        vm.errorPassword = null;
        vm.successPassword = null;

        Principal.identity().then(function(account) {
            vm.account = account;
        });

        function changePassword () {
            if (vm.password !== vm.confirmPassword) {
                vm.error = null;
                vm.successPassword = null;
                vm.doNotMatch = 'ERROR';
            } else {
                vm.doNotMatch = null;
                Auth.changePassword(vm.password).then(function () {
                    vm.error = null;
                    vm.successPassword = 'OK';
                }).catch(function () {
                    vm.errorPassword = null;
                    vm.error = 'ERROR';
                });
            }
        }

        vm.clear = clear;
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        $scope.myImage='';
        $scope.myCroppedImage = '';

        $scope.showCroppedImage = false;

        var handleFileSelect=function(evt) {
            var file=evt.currentTarget.files[0];
            console.log(file)
            var reader = new FileReader();
            reader.onload = function (evt) {
                $scope.$apply(function($scope){
                    $scope.myImage=evt.target.result;
                });
            };
            reader.readAsDataURL(file);
            console.log("asdas")
        };
        angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);

        vm.isShowDialog = false;

        vm.showImageDialog = showImageDialog;

        function showImageDialog(){
            vm.isShowDialog = true;
        }

        vm.hideImageDialog = hideImageDialog;
        function hideImageDialog(){
            vm.isShowDialog = false;
        }

        vm.saveImage =saveImage;


        function dataURLtoFile(dataurl, filename) {
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
            while(n--){
                u8arr[n] = bstr.charCodeAt(n);
            }
            return new Blob([window.atob(u8arr)],  {type: 'image/png', encoding: 'utf-8'})
        }


        function dataURLtoFile(dataurl, filename) {
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
            while(n--){
                u8arr[n] = bstr.charCodeAt(n);
            }
            return new File([u8arr], filename, {type:mime});
        }

//Usage example:


        function b64toBlob(b64Data, contentType, sliceSize) {
            contentType = contentType || '';
            sliceSize = sliceSize || 512;

            var byteCharacters = atob(b64Data);
            var byteArrays = [];

            for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
                var slice = byteCharacters.slice(offset, offset + sliceSize);

                var byteNumbers = new Array(slice.length);
                for (var i = 0; i < slice.length; i++) {
                    byteNumbers[i] = slice.charCodeAt(i);
                }

                var byteArray = new Uint8Array(byteNumbers);

                byteArrays.push(byteArray);
            }

            var blob = new Blob(byteArrays, {type: contentType});
            return blob;
        }


        function saveImage(){

            var img_b64 = $scope.myCroppedImage;
            var png = img_b64.split(',')[1];
            var file = b64toBlob(png, 'image/png')
            console.log(file);
            var fd = new FormData();
            fd.append('file', file);
            $http.post("/api/account/upload/icon",  fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .success(function (data, status, headers, config) {
                    vm.hideImageDialog();
                });
        }
    }
})();
