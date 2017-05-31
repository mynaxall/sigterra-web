(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('ActivationController', ActivationController);

    ActivationController.$inject = ['$stateParams', 'Auth', 'LoginService', '$scope', '$http'];

    function ActivationController ($stateParams, Auth, LoginService, $scope, $http) {
        var vm = this;

        $scope.userCard = function(){
            $http.get("/api/userCardlets")
                .success(function(response, status, headers) {
                    $scope.userCardlets = response;
                    if($scope.userCardlets.length === 0){
                        $scope.saveCardlet();

                    }
                });
        };

        $scope.saveCardlet = function(){
            $http.post("/api/cardletFirst",  $scope.tabNames)
                .success(function (data, status, headers, config) {
                    $scope.userCard();
                });
            Auth.activateAccount({key: $stateParams.key}).then(function () {
                vm.error = null;
                vm.success = 'OK';
            }).catch(function () {
                vm.success = null;
                vm.error = 'ERROR';
            });

        }

        $scope.getUserProfile = function(){
            $http.get("/api/accountActivate?key="+ $stateParams.key)
                .success(function(response, status, headers) {

                    $scope.userAccount = response;
                    $scope.tabNames.tabs[0].userName = {
                        "value": $scope.userAccount.username
                    };
                    $scope.tabNames.tabs[0].userEmail = {
                        "value": $scope.userAccount.email
                    };

                    $scope.tabNames.tabs[0].phone = {
                        "value": $scope.userAccount.phoneNumber
                    };

                    $scope.tabNames.tabs[0].address = {
                        "value": $scope.userAccount.address
                    };

                    $scope.tabNames.tabs[0].company = {
                        "value": $scope.userAccount.companyName
                    };

                    $scope.tabNames.tabs[0].site = {
                        "value": $scope.userAccount.companySite
                    };

                    $scope.tabNames.tabs[0].job = {
                        "value": $scope.userAccount.jobTitle
                    };
                    if($scope.userAccount.imageUrl){
                        $scope.tabNames.tabs[0].photo = $scope.userAccount.imageUrl
                    }else{
                        $scope.tabNames.tabs[0].photo = $location.protocol() + '://' + $location.host() + ':' + $location.port()+"/content/images/avatar_img.png"
                    }


                });
            $scope.userCard();
        };
        $scope.getUserProfile();

        vm.login = LoginService.open;
    }
})();
