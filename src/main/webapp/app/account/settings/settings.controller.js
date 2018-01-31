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

    SettingsController.$inject = ['Principal', 'Auth', '$scope', '$http', '$timeout', 'PHONE_PATTERN'];

    function SettingsController (Principal, Auth, $scope, $http, $timeout, PHONE_PATTERN) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;
        vm.PHONE_PATTERN = new RegExp(PHONE_PATTERN);

        $scope.disableSave = true;
        $scope.bounds = {};
        $scope.bounds.left = 0;
        $scope.bounds.right = 200;
        $scope.bounds.top = 200;
        $scope.bounds.bottom = 0;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */

        $scope.getAccount = function(){
            $http.get("/api/account")
                .success(function (data, status, headers, config) {
                    vm.settingsAccount = data;
                    vm.settingsAccount.imageUrl = vm.settingsAccount.imageUrl;
                });
        }

        $scope.trimValue = function(val){

            if(val && !val.trim()){
                return true
            }
            return false
        }




        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                $timeout(function() {
                    vm.success = null;
                }, 3000);
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
                    $timeout(function() {
                        vm.successPassword = null;
                    }, 3000);
                }).catch(function () {
                    vm.errorPassword = null;
                    vm.error = 'ERROR';
                });
            }
            vm.password = "";
            vm.confirmPassword = "";
            $scope.disableSave = true;
        }

        vm.clear = clear;
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        $scope.myImage='';
        $scope.myCroppedImage = '';

        $scope.showCroppedImage = false;
        $scope.showSpinner = false;

        var handleFileSelect=function(evt) {
            var file=evt.currentTarget.files[0];
            var reader = new FileReader();
            reader.onload = function (evt) {
                $scope.$apply(function($scope){
                    $scope.myImage=evt.target.result;
                });
            };
            reader.readAsDataURL(file);
        };
        angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);

        vm.isShowDialog = false;

        vm.showImageDialog = showImageDialog;

        function showImageDialog() {
            angular.element('#fileInput').val(null);
            $scope.myImage = "";
            if (vm.settingsAccount.imageUrl) {
                $http.get(vm.settingsAccount.imageUrl, {responseType: "arraybuffer"})
                    .success(function (data) {
                        var str = _arrayBufferToBase64(data);
                        $scope.myImage = 'data:image/JPEG;base64,' + str;
                    });
            }
            $scope.myCroppedImage = '';
            vm.isShowDialog = true;
        }

        function _arrayBufferToBase64(buffer) {
            var binary = '';
            var bytes = new Uint8Array(buffer);
            var len = bytes.byteLength;
            for (var i = 0; i < len; i++) {
                binary += String.fromCharCode(bytes[i]);
            }
            return window.btoa(binary);
        }

        vm.hideImageDialog = hideImageDialog;
        function hideImageDialog(clean){
            if(clean){
                $scope.myImage='';
                $scope.myCroppedImage = '';
            }
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
            vm.hideImageDialog();
            $scope.showSpinner = true;
            var img_b64 = $scope.myCroppedImage;
            var png = img_b64.split(',')[1];
            var file = b64toBlob(png, 'image/png')
            var fd = new FormData();
            fd.append('file', file);
            vm.settingsAccount.imageUrl = "";
            $http.post("/api/account/upload/icon",  fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .success(function (data, status, headers, config) {
                    $scope.showSpinner = false;
                    vm.settingsAccount.imageUrl = data.url;
                    $scope.myImage='';
                    $scope.myCroppedImage = '';
                });


        }
    }
})();
