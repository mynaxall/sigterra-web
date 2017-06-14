(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('PreviewCardletController', PreviewCardletController);



    PreviewCardletController.$inject = ['$scope', '$state', 'UserCardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout', '$location', 'LoginService', '$sce'];

    function PreviewCardletController ($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http, $timeout, $location, LoginService, $sce) {

        var vm = this;

        vm.currentSlide = 0;

        $scope.getCardlet = function(){
            var param1 = $location.search().cardletId;
            $http.get("/api/cardlet/"+param1)
                .success(function(response, status, headers) {
                    $scope.tabNames = response;
                    if($scope.tabNames.tabs[0].tabType == 1){
                        $scope.currentUrl = $scope.createURL($scope.tabNames.tabs[0].site.value);
                        $scope.tabType = 1
                        $scope.currentLink =  $scope.getLink($scope.tabNames.tabs[0].site.value);
                    }
                    else{
                        var buttons = document.getElementsByClassName("carousel-control");

                        for(var j = 0; j < buttons.length; j++){
                            document.getElementsByClassName("carousel-control")[j].className += " disabledLink"
                        }
                        $scope.currentUrl = $scope.createURL($scope.tabNames.tabs[0].items[0].link);
                        $scope.currentLink = $scope.getLink($scope.tabNames.tabs[0].items[0].link);
                        $scope.tabType = $scope.tabNames.tabs[0].tabType
                    }
                });
        };

        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        };

        $scope.createURL = function(link){
            if(link.indexOf('https://') == -1 && link.indexOf('http://') == -1){
               link = "http://"+link
            }else if(link.indexOf('https://') != -1){
                link.replace("https://", "http://")

            }
            return link
        }


        $scope.prevSlide = function(index){
            $scope.currentUrl = undefined
            if( vm.currentSlide == 0){
                vm.currentSlide = $scope.tabNames.tabs[index].items.length -1
            }else{
                vm.currentSlide = vm.currentSlide - 1;
            }
            console.log(vm.currentSlide)
            setTimeout(function() {
                $scope.currentUrl = $scope.createURL($scope.tabNames.tabs[index].items[vm.currentSlide].link);
                $scope.currentLink = $scope.getLink($scope.tabNames.tabs[index].items[vm.currentSlide].link);
            },10);
        };

        $scope.nextSlide = function(index){
            $scope.currentUrl = undefined
            if( vm.currentSlide == $scope.tabNames.tabs[index].items.length -1){
                vm.currentSlide = 0
            }else{
                vm.currentSlide = vm.currentSlide + 1;
            }
            setTimeout(function() {
                $scope.currentUrl = $scope.createURL($scope.tabNames.tabs[index].items[vm.currentSlide].link);
                $scope.currentLink = $scope.getLink($scope.tabNames.tabs[index].items[vm.currentSlide].link);
            },10)
        };

        $scope.getLink = function(link){
            var linkNew = link;
            if(link.indexOf('https://') != -1){
                linkNew = link.replace("https://", "");
            }else if(link.indexOf('http://') != -1){
                linkNew = link.replace("http://", "")
            }
            return linkNew;

        }


        $scope.next = function(){
            console.log("asd")
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        $scope.openCity = function(cardName, cardId, id) {
            $scope.currentUrl = undefined;
            var buttons = document.getElementsByClassName("carousel-control");

            for(var j = 0; j < buttons.length; j++){
                document.getElementsByClassName("carousel-control")[j].className += " disabledLink"
            }

            vm.currentSlide = 0;
            setTimeout(function() {
                if ($scope.tabNames.tabs[id].tabType == 1) {
                    $scope.currentUrl = $scope.createURL($scope.tabNames.tabs[id].site.value);
                    $scope.currentLink = $scope.getLink($scope.tabNames.tabs[id].site.value);
                    $scope.tabType = $scope.tabNames.tabs[id].tabType
                }
                else {
                    $scope.currentUrl = $scope.createURL($scope.tabNames.tabs[id].items[0].link);
                    $scope.currentLink = $scope.getLink($scope.tabNames.tabs[id].items[0].link);
                    $scope.tabType = $scope.tabNames.tabs[id].tabType
                }
            },10)

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
        };


        $scope.showSignature = function(){
            $http.get("/api/signatures")
                .success(function(response, status, headers) {
                    $scope.signatures = response;
                });
        };



        angular.element(document).ready(function () {
            document.getElementsByClassName("tabcontent2")[0].style.display = "block";
            document.getElementsByClassName("tabs2")[0].className += " active";
            document.getElementsByClassName("tabcontent")[0].style.display = "block";;
            document.getElementsByClassName("tabs")[0].className += " active";
        });



        $scope.addColors = function(id, colorMain, colorSecond){

            var currentEl = document.getElementById(id);

            if(currentEl) {
                currentEl.style.background = "#F9F9F9";
                currentEl.style.borderTop = "1px solid #D0D8D9"
                currentEl.style.borderBottom = "1px solid #D0D8D9";
                if (angular.element(document.getElementById(id)).hasClass('active')) {
                    currentEl.style.background = "#FFFFFF";
                    currentEl.style.borderTop = "2px solid #" + colorSecond;
                    currentEl.style.borderBottom = "0px";
                }

                if ($scope.tabNames) {

                    if ($scope.tabNames.tabs.length == 1) {
                        currentEl.style.width = "540px"
                    }
                    if ($scope.tabNames.tabs.length === 2) {
                        currentEl.style.width = "270px"
                    }
                    if ($scope.tabNames.tabs.length === 3) {
                        currentEl.style.width = "180px"
                    }
                    if ($scope.tabNames.tabs.length === 4) {
                        currentEl.style.width = "135px"
                    }
                }
            }
        };


    }


})();
