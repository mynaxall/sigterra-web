(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('CardletListController', CardletListController);

    CardletListController.$inject = ['$scope', '$state', 'Cardlet', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http'];

    function CardletListController ($scope, $state, Cardlet, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;

        loadAll();

        function loadAll () {
            Cardlet.query({
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.cardlets = data;

                angular.forEach( vm.cardlets, function(item){
                    $scope.businesses = [];
                    $scope.items =[];
                    $scope.itemsData
                    $http.get("/api/businesses/caedlet/"+item.id)
                        .success(function(response, status, headers) {
                            console.log(response);
                            $scope.businesses = response;
                        });
                    $http.get("/api/items/caedlet/"+item.id)
                        .success(function(response, status, headers) {

                                console.log(response);
                                $scope.items = response;
                                var ba = $scope.businesses;
                                $scope.tabs =  ba.concat($scope.items);
                                console.log("tabs")
                                console.log($scope.tabs)
                                vm.page = pagingParams.page;
                            angular.forEach( $scope.items, function(item) {
                                $http.get("/api/itemsData/caedlet/" + item.id)
                                    .success(function (response, status, headers) {
                                        console.log(response);
                                        $scope.itemsDatas = response;

                                    });
                            });
                        });

                });


            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
        $scope.openCity = function(evt, cityName) {
            var i, tabcontent, tablinks;
            tabcontent = document.getElementsByClassName("tabcontent");
            for (i = 0; i < tabcontent.length; i++) {
                tabcontent[i].style.display = "none";
            }
            tablinks = document.getElementsByClassName("tablinks");
            for (i = 0; i < tablinks.length; i++) {
                tablinks[i].className = tablinks[i].className.replace(" active", "");
            }
            document.getElementById(cityName).style.display = "block";
            evt.currentTarget.className += " active";
        }

        $scope.showSignature = function(){
            $http.get("/api/signatures")
                .success(function(response, status, headers) {
                    console.log(response);
                    $scope.signatures = response;
                });
        }
    }


})();
