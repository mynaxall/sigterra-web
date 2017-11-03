(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('UserCardletController', UserCardletController);



    UserCardletController.$inject = ['$scope', '$state', 'UserCardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout', '$location'];

    function UserCardletController ($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http, $timeout, $location) {

        var vm = this;
        vm.currentSlide = 0;
        vm.showCarousel = true;
        $scope.showLink = true;

        $scope.getCardlet = function(){
            var param1 = $location.search().cardletId;
            $http.get("/api/cardlet/"+window.atob(param1))
                .success(function(response, status, headers) {
                    $scope.tabNames = response;
                });
        };


        $scope.openCity = function(cardName, cardId) {
            vm.currentSlide = 0

            var i, tabcontent2, tablinks2, tabs2;
            tabcontent2 = document.getElementsByClassName("tabcontent2");
            for (i = 0; i < tabcontent2.length; i++) {
                tabcontent2[i].style.display = "none";
            }
            tablinks2 = document.getElementsByClassName("tablinks2");
            for (i = 0; i < tablinks2.length; i++) {
                tablinks2[i].className = tablinks2[i].className.replace(" active", "");

            }
            tabs2 = document.getElementsByClassName("tabs2");

            for (i = 0; i < tabs2.length; i++) {
                tabs2[i].className = tabs2[i].className.replace(" active", "");

            }

            document.getElementById(cardName).style.display = "block";
            document.getElementById(cardId).className += " active";
        }


        $scope.showSignature = function(){
            $http.get("/api/signatures")
                .success(function(response, status, headers) {
                    $scope.signatures = response;
                });
        }


        angular.element(document).ready(function () {
            document.getElementsByClassName("tabcontent2")[0].style.display = "block";
            document.getElementsByClassName("tabs2")[0].className += " active";
            document.getElementsByClassName("tabcontent")[0].style.display = "block";;
            document.getElementsByClassName("tabs")[0].className += " active";
        });

        $scope.time = Date.now()

        $scope.addColors = function(id, colorMain, colorSecond, linkId){

            var cyrrentEl = document.getElementById(id);

            var link = document.getElementById(linkId);

            if(cyrrentEl) {
                if($scope.tabNames) {

                    if ($scope.tabNames.tabs.length == 1) {
                        cyrrentEl.style.width = "540px";
                        link.style.width = "535px";
                        link.style.maxWidth = "535px"
                    }

                    if ($scope.tabNames.tabs.length === 2) {
                        cyrrentEl.style.width = "270px";
                        link.style.width = "265px";
                        link.style.maxWidth = "265px";
                    }
                    if ($scope.tabNames.tabs.length === 3) {
                        cyrrentEl.style.width = "180px";
                        link.style.width = "170px";
                        link.style.maxWidth = "170px";
                    }
                    if ($scope.tabNames.tabs.length === 4) {
                        cyrrentEl.style.width = "135px";
                        link.style.width = "130px";
                        link.style.maxWidth = "130px";
                    }
                }
                cyrrentEl.style.background = "#F9F9F9";
                cyrrentEl.style.borderTop = "1px solid #D0D8D9"
                cyrrentEl.style.borderBottom = "1px solid #D0D8D9";
                link.style.color = "#7F8C8C";
                if (angular.element(document.getElementById(id)).hasClass('active')) {
                    cyrrentEl.style.background = "#FFFFFF";
                    cyrrentEl.style.borderTop = "2px solid #" + colorSecond;
                    link.style.color = "#"+colorSecond;
                    cyrrentEl.style.borderBottom = "0px";
                }
                setTimeout(function() {
                    if (angular.element(document.getElementById(id)).hasClass('active')) {
                        cyrrentEl.style.background = "#FFFFFF";
                        cyrrentEl.style.borderTop = "2px solid #" + colorSecond;
                        link.style.color = "#"+colorSecond;
                        cyrrentEl.style.borderBottom = "0px";
                    }
                }, 700)

            }
        }

        $scope.myInterval = 3000;


    }


})();
