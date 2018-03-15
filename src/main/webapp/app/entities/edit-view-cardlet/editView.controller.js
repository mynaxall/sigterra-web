(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('EditViewController', EditViewController)
        .filter('tel', function () {
            return function (tel) {
                if (!tel) { return ''; }

                var value = tel.toString().trim().replace(/^\+/, '');

                if (value.match(/[^0-9]/)) {
                    return tel;
                }

                var country, city, number;

                switch (value.length) {
                    case 9: // +1PPP####### -> C (PPP) ###-####
                        country = 1;
                        city = value.slice(0, 3);
                        number = value.slice(3);
                        break;
                    default:
                        return tel;
                }

                if (country == 1) {
                    country = "";
                }

                number = number.slice(0, 3) + '-' + number.slice(3);

                return (country + " (" + city + ") " + number).trim();
            };
        });

    EditViewController.$inject = ['$scope', '$state', 'CardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout', '$location', 'orderByFilter', 'PHONE_PATTERN', 'TOOLBAR_OPTIONS', 'ImageService', 'Carousel'];

    function EditViewController($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http, $timeout, $location, orderByFilter, PHONE_PATTERN, TOOLBAR_OPTIONS, ImageService, Carousel) {
        var vm = this;
        $scope.showError = false;
        $scope.Carousel = Carousel;
        $scope.time = Date.now();
        $scope.showEditorNavigation = false;
        $scope.showCropDialog = false;
        $scope.firstBusinessCardId ="";
        $scope.myImage='';
        $scope.showConent = true;
        $scope.noSocialChanges = false;
        $scope.urlError = 'Invalid URL string. It should start from "http://" or "https://"';
        $scope.myInterval = 0;
        vm.showCarousel = true;
        $scope.showLink = false;
        $scope.dataFetching = false;
        vm.PHONE_PATTERN = new RegExp(PHONE_PATTERN);
        vm.toolbarOptions = angular.fromJson(TOOLBAR_OPTIONS);
        vm.showExctractButton = false;
        $scope.myCroppedImage = '';
        $scope.bounds = {};
        $scope.bounds.left = 0;
        $scope.bounds.right = 200;
        $scope.bounds.top = 200;
        $scope.bounds.bottom = 0;
        $scope.imageSize = {width: 200, height: 200};
        $scope.secondaryColor = "";
        vm.activeTab = 2;
        vm.bg = "/content/images/background.jpg";
        vm.bgSlides = ["/content/images/background.jpg", "/content/images/background2.jpg", "/content/images/background3jpg"];


        $scope.setActive = function (index) {
            vm.activeTab = index;
        }

        $scope.showSocialDialog = false;

        $scope.socialLinks = {twitter: "", facebook: "", google: "", linkedin: ""};


        function getImage(url) {
            $http.get(url, {responseType: "arraybuffer"})
                .success(function (data) {
                    var str = _arrayBufferToBase64(data);
                    $scope.myImage = 'data:image/JPEG;base64,' + str;
                });

        }

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        $scope.showSpinner = true;

        $scope.getCardlet = function(){
            console.log("asdas" +
                "dasdasd")
            var param1 = $location.search().cardletId;
            $http.get("/api/userCardlet/"+param1)
                .success(function(response, status, headers) {
                    $scope.showError = false;
                    $scope.tabNames = response;
                    for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                        if ($scope.tabNames.tabs[i].tabType == '1'){
                            $scope.firstBusinessCardId = i;
                            break
                        }else{
                            $scope.firstBusinessCardId = '';
                        }
                    }

                    $timeout(function () {
                        document.getElementsByClassName("tabcontent2")[0].style.display = "block";
                        document.getElementsByClassName("tabs2")[0].className += " active";
                        $scope.showSpinner = false;
                    }, 500)
                }).error(function (response) {
                $scope.showError = true;
                $scope.showSpinner = false;
            });
            $scope.signatureLink = $location.protocol() + '://' + $location.host() + ':' + $location.port()+'/#/previewCardlet?cardletId='+ window.btoa(param1);
        };

        loadAll();


        function loadAll () {
            CardletList.query({
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
                vm.queryCount = vm.totalItems;
                $scope.cardlets = data;

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


        $scope.openCity = function(cityName, tabId, cardName, cardId) {

            if(!$scope.isDisabledTabs && $scope.isEmptyName()) {

                var i, tablinks, tabs;

                tablinks = document.getElementsByClassName("tablinks");
                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");

                }
                tabs = document.getElementsByClassName("tabs");

                for (i = 0; i < tabs.length; i++) {
                    tabs[i].className = tabs[i].className.replace(" active", "");

                }

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

                vm.currentSlide = 0;
                document.getElementById(cardName).style.display = "block";
                document.getElementById(cardId).className += " active";
            }
        }


        $scope.accordionActive = 2;

        vm.currentSlide = 0;

        $scope.changeAccordionActivity = function(id){
            if($scope.accordionActive != id){
                $scope.accordionActive = id;
                vm.currentSlide = id -2;
                if(vm.currentSlide < 0){
                    vm.currentSlide = 1;
                }

            }else{
                $scope.accordionActive = 0
            }

        }

        $scope.positionChecking = false;

        $scope.positionCheck = function(){
            $scope.isDisabledTabs = true;
            $scope.positionChecking = true;

            for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                $scope.tabNames.tabs[i].position = i;
            }

            var tabs2 = document.getElementsByClassName("tabs2");
            var tabs = document.getElementsByClassName("tabs");

            for (var i = 0; i < tabs.length; i++) {
                document.getElementsByClassName("tablinks")[i].className += " disabledLink";

                if(angular.element(tabs[i]).hasClass('active')){
                    document.getElementsByClassName("tabcontent")[i].style.display = "none";;
                    tabs[i].className = tabs[i].className.replace(" active", "");
                }
                if(angular.element(tabs2[i]).hasClass('active')){
                    document.getElementsByClassName("tabcontent2")[i].style.display = "none";;
                    tabs2[i].className = tabs2[i].className.replace(" active", "");
                }

            }
            for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                if ($scope.tabNames.tabs[i].tabType == '1') {
                    $scope.firstBusinessCardId = i;
                    break
                } else {
                    $scope.firstBusinessCardId = '';
                }
            }

            $scope.positionChecking = false;
            vm.currentSlide = 1;
            $timeout(function() {
                document.getElementsByClassName("tabcontent2")[0].style.display = "block";
                document.getElementsByClassName("tabs2")[0].className += " active";
                document.getElementsByClassName("tabcontent")[0].style.display = "block";
                document.getElementsByClassName("tabs")[0].className += " active";
                for (var i = 0; i < tabs.length; i++) {
                    document.getElementsByClassName("tablinks")[i].className = document.getElementsByClassName("tablinks")[i].className.replace(" disabledLink", "");
                }
                vm.currentSlide = 0

                $scope.isDisabledTabs = false;
            },100);

        };

        $scope.getFonts = function(bold, italic, underline){
            var textDecoration = "normal";

            var fontFamily = "Roboto-Regular";

            if(bold && italic){
                fontFamily = "Roboto-Bold-Italic";
            }
            else if(italic){
                fontFamily = "Roboto-Italic";
            }
            else if(bold){
                fontFamily = "Roboto-Bold";
            }

            if(underline){
                textDecoration = "underline";
            }

            return  {
                "font-family" : fontFamily,
                "text-decoration" : textDecoration,

            }
        }



        $scope.isDisabledTabs = false;

        $scope.disableTabs = function(){
            $scope.isDisabledTabs = true;
        }

        $scope.enableTabs = function(){
            $scope.isDisabledTabs = false;
        }

        $scope.tabWidth = function( id , colorId){

            var cyrrentEl = document.getElementById(id);
            if($scope.tabNames) {

                if($scope.tabNames.tabs.length == 1){
                    cyrrentEl.style.width = "698px"
                }

                if ($scope.tabNames.tabs.length === 2) {
                    cyrrentEl.style.width = "349px"
                }
                if ($scope.tabNames.tabs.length === 3) {
                    cyrrentEl.style.width = "232px"
                }
                if ($scope.tabNames.tabs.length === 4) {
                    cyrrentEl.style.width = "188px"
                }
                if(colorId){
                    cyrrentEl.style.color = "#"+colorId;
                }
            }
        };


        $scope.itemPosition = function(index){
            return index + 1;
        }


        $scope.addColors = function(id, colorMain, colorSecond, linkId){

            var cyrrentEl = document.getElementById(id);
            var link = document.getElementById(linkId);

            if(cyrrentEl) {

                if ($scope.tabNames) {

                    if ($scope.tabNames.tabs.length == 1) {
                        cyrrentEl.style.width = "538px";
                        link.style.width = "535px";
                        link.style.maxWidth = "535px"
                    }

                    if ($scope.tabNames.tabs.length === 2) {
                        cyrrentEl.style.width = "268px";
                        link.style.width = "265px";
                        link.style.maxWidth = "265px";
                    }
                    if ($scope.tabNames.tabs.length === 3) {
                        cyrrentEl.style.width = "178px";
                        link.style.width = "170px";
                        link.style.maxWidth = "170px";
                    }
                    if ($scope.tabNames.tabs.length === 4) {
                        cyrrentEl.style.width = "133px";
                        link.style.width = "130px";
                        link.style.maxWidth = "130px";
                    }
                }
            }
        }



        $scope.isEmptyName =function(){
            if($scope.tabNames) {
                for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                    if (!$scope.tabNames.tabs[i].name || $scope.tabNames.tabs[i].name == '') {
                        return false;
                    }
                }
            }
            return true;
        }

        $scope.checkName = function(data){
            if(!data || data === ""){
                return "Your tab name is required."
            }
        }

        $scope.disableSaveBtn = false;

        $scope.saveCardlet = function(){

            if($scope.isEmptyName()) {
                $scope.disableSaveBtn = true;
                $http.post("/api/editCardlet", $scope.tabNames)
                    .success(function (data, status, headers, config) {
                        $location.path('/user-cardlets')
                    }).error(function (response) {
                    $scope.disableSaveBtn = false;
                });
            }
        }

        $scope.accordion = 1;

        $scope.accordionActivete = function(Id){
            if($scope.accordion != Id){
                $scope.accordion = Id;
            }else{
                $scope.accordion = 0
            }
        }

        $scope.prevSlideBg = function(){
            $scope.currentUrl = undefined;
            if( vm.currentSlideBg == 0){
                console.log( vm.currentSlideBg)
                vm.currentSlideBg = vm.bgSlides.length -1;
                console.log( vm.currentSlideBg)
            }else{
                console.log( vm.currentSlideBg)
                vm.currentSlideBg = parseInt(vm.currentSlideBg) - 1;
                console.log( vm.currentSlideBg)
            }
        };

        $scope.nextSlideBg = function(){
            console.log(vm.currentSlideBg)
            $scope.currentUrl = undefined
            if( vm.currentSlideBg == vm.bgSlides.length -1){
                console.log( vm.currentSlideBg)
                vm.currentSlideBg = 0
                console.log( vm.currentSlideBg)
            }else{
                console.log( vm.currentSlideBg)
                vm.currentSlideBg = parseInt(vm.currentSlideBg) + 1;
            }
            console.log(vm.currentSlideBg)
        };


        $scope.prevSlide = function(index){
            $scope.currentUrl = undefined;
            if( vm.currentSlide == 0){
                vm.currentSlide = $scope.tabNames.tabs[index].items.length -1
            }else{
                vm.currentSlide = parseInt(vm.currentSlide) - 1;
            }

        };

        $scope.nextSlide = function(index){
            $scope.currentUrl = undefined
            if( vm.currentSlide == $scope.tabNames.tabs[index].items.length -1){
                vm.currentSlide = 0
            }else{
                vm.currentSlide = parseInt(vm.currentSlide) + 1;
            }


            $timeout(function() {vm.showSpinner = false; },4000)
        };


        $scope.getLink = function(link){
            if(link && link.startsWith('http')){
                return link;
            }else{
                return 'http://'+link;
            }
        };

        $scope.fetchData = function (tabId, itemId) {
            $scope.dataFetching = true;
            $http.post("/api/resolve?url="+  $scope.tabNames.tabs[tabId].items[itemId].link)
                .success(function (data, status, headers, config) {
                    if(data.description) {
                        $scope.tabNames.tabs[tabId].items[itemId].description.value = data.description;
                    }else{
                        $scope.tabNames.tabs[tabId].items[itemId].description.value = "";
                    }
                    if(data.title) {
                        $scope.tabNames.tabs[tabId].items[itemId].name.value = data.title;
                    }else {
                        $scope.tabNames.tabs[tabId].items[itemId].name.value = ""
                    }
                    var image = data.imageUrl;
                    if(data.imageUrl) {
                        $http.get(image)
                            .success(function (data, status, headers, config) {
                                $scope.tabNames.tabs[tabId].items[itemId].image = image;
                            }).error(function (response) {
                            $scope.tabNames.tabs[tabId].items[itemId].image = "/content/images/portfolio_img_01.png";
                        });
                    }else{
                        $scope.tabNames.tabs[tabId].items[itemId].image = "/content/images/portfolio_img_01.png";
                    }
                    $scope.dataFetching = false;
                }).error(function (response) {

            });
        }

        $scope.cropWidth = function () {


            if ($scope.myCroppedImage) {
                console.log("ss")
                $scope.imageSize = ImageService.imageSize($scope.bounds)
            }
            return $scope.imageSize;
        }

        $scope.scrollToFooter = function (id) {
            var el = document.getElementById(id);
            console.log(el)
            el.scrollIntoView();
        }


    }


})();
