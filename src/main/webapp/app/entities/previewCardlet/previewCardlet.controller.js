(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('PreviewCardletController', PreviewCardletController);



    PreviewCardletController.$inject = ['$scope', '$state', 'UserCardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout', '$location', 'LoginService'];

    function PreviewCardletController ($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http, $timeout, $location, LoginService) {



        $scope.getCardlet = function(){
            var param1 = $location.search().cardletId;
            $http.get("/api/cardlet/"+param1)
                .success(function(response, status, headers) {
                    $scope.tabNames = response;
                });
        };



        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }


        $scope.openCity = function(cardName, cardId) {


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



        $scope.addColors = function(id, colorMain, colorSecond){

            var cyrrentEl = document.getElementById(id);
            if(cyrrentEl) {
                cyrrentEl.style.background = "#F9F9F9";
                if (angular.element(document.getElementById(id)).hasClass('active')) {
                    cyrrentEl.style.background = "#FFFFFF" ;
                    cyrrentEl.style.borderTop = "2px solid #4BABE2"
                }
            }
            if($scope.tabNames.tabs.length ===2){
                cyrrentEl.style.width = "270px"
            }
            if($scope.tabNames.tabs.length ===3){
                cyrrentEl.style.width = "180px"
            }
            if($scope.tabNames.tabs.length ===4){
                cyrrentEl.style.width = "135px"
            }
        }

        $scope.myInterval = 3000;

    }


})();
