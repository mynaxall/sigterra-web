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

    EditViewController.$inject = ['$scope', '$state', 'CardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout', '$location', 'orderByFilter', 'PHONE_PATTERN', 'TOOLBAR_OPTIONS', 'ImageService', 'Carousel', 'EditViewerService'];

    function EditViewController($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http, $timeout, $location, orderByFilter, PHONE_PATTERN, TOOLBAR_OPTIONS, ImageService, Carousel, EditViewerService) {
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
        vm.activeTab = 1;
        vm.bg = "/content/images/background.jpg";
        $scope.cardletView = {};
        $scope.isLogo = false;
        $scope.cardletId = '';
        $scope.header = {
            'name' : '',
            'title' : '',
            'text' : 'Request For Demo',
            'ctaColor' : 'f0ad4e'
        };
        $scope.selection = [];
        $scope.background = {
            'text' : 'Conversational Commerce is the biggest opportunity for brands to act like people in digital channels & build customer engagement and loyalty',
            'textColor' : 'true'
        };
        $scope.links = {
            'title' : 'CapGemini\'s Experts',
            'name1' : '',
            'name2' : '',
            'name3' : ''
        };
        $scope.footer = {};
        $scope.bgArray = {};
        $scope.listIcons = {};

        $scope.myImage='';
        $scope.myCroppedImage = '';
        vm.saveImage = saveImage;
        vm.showImageDialog = showImageDialog;
        vm.hideImageDialog = hideImageDialog;



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
            console.log("asdasdasdasd")
            $scope.cardletId = $location.search().cardletId;
            $http.get("/api/userCardlet/"+ $scope.cardletId)
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
            EditViewerService.getPreview($scope.cardletId).then(function (response) {
                $scope.cardletView = response;
                if($scope.cardletView.header){
                    $scope.header = $scope.cardletView.header;
                }
                if($scope.cardletView.background){
                    $scope.background = $scope.cardletView.background;
                }
                if($scope.cardletView.links){
                    $scope.links = $scope.cardletView.links;
                    if($scope.links.logoUrl1) {
                        $scope.selection.push($scope.links.logoUrl1);
                    }
                    if($scope.links.logoUrl2) {
                        $scope.selection.push($scope.links.logoUrl2);
                    }
                    if($scope.links.logoUrl3) {
                        $scope.selection.push($scope.links.logoUrl3);
                    }
                }

                if($scope.cardletView.footer){
                    $scope.footer = $scope.cardletView.footer;
                }


                console.log(response)
            }).catch(function (response) {
            });
            $scope.signatureLink = $location.protocol() + '://' + $location.host() + ':' + $location.port()+'/#/previewCardlet?cardletId='+ window.btoa($scope.cardletId);
        };

        loadAll();

        function loadAll () {
            EditViewerService.getBackgrounds().then(function (response) {
                $scope.bgArray = response;

            }).catch(function (response) {
            });

            EditViewerService.getListIcons().then(function (response) {
                $scope.listIcons = response;
            }).catch(function (response) {
            });

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
        $scope.accordion = 1;

        $scope.accordionActivete = function(Id){
            if($scope.accordion != Id){
                $scope.accordion = Id;
            }else{
                $scope.accordion = 0
            }
        }

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
                $scope.imageSize = ImageService.imageSize($scope.bounds)
            }
            return $scope.imageSize;
        }

        $scope.scrollToFooter = function (id) {
            var el = document.getElementById(id);
            el.scrollIntoView();
        }


        function showImageDialog(type) {
            $scope.isLogo = type;
            angular.element('#fileInput').val(null);
            $scope.myImage = "";
            if ($scope.header.imageUrl) {
                $http.get($scope.header.imageUrl, {responseType: "arraybuffer"})
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

        function hideImageDialog(clean){
            if(clean){
                $scope.myImage='';
                $scope.myCroppedImage = '';
            }
            vm.isShowDialog = false;
        }

        function saveImage(){
            var url = '';
            if($scope.isLogo){
                url = '/api/cardlet/pageview/logo-image/' + $scope.cardletId;
            }else{
                url = '/api/cardlet/pageview/photo-image/' + $scope.cardletId;
            }
            vm.hideImageDialog();
            $scope.showSpinner = true;
            var img_b64 = $scope.myCroppedImage;
            var png = img_b64.split(',')[1];
            var file = b64toBlob(png, 'image/png')
            var fd = new FormData();
            fd.append('file', file);
            $http.post(url,  fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
                .success(function (data, status, headers, config) {
                    if($scope.isLogo){
                        $scope.header.logoUrl = data.url
                    }else{
                        $scope.header.photoUrl = data.url
                    }
                    $scope.showSpinner = false;
                    $scope.myImage='';
                    $scope.myCroppedImage = '';
                });
        }

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
        };

        $scope.save = function () {
            $scope.showSpinner = true;
            $scope.cardletView.header = $scope.header;
            $scope.cardletView.background = $scope.background;
            $scope.cardletView.links = $scope.links;
            $scope.cardletView.footer = $scope.footer;
            $scope.cardletView.cardletId = $scope.cardletId;
            EditViewerService.updateViewer($scope.cardletView ).then(function (response) {
                $scope.showSpinner = false;
                $location.path('/user-cardlets')
            }).catch(function (response) {
                $scope.showSpinner = false;
            });
        }

        $scope.select = function (index) {
            $scope.background.imageUrl = $scope.bgArray.listFilesPaths[index].url;
        }

        $scope.toggleSelection = function toggleSelection(img) {
            console.log(img)
            var idx = $scope.selection.indexOf(img);
            $scope.links.logoUrl1 = '';
            $scope.links.logoUrl2 = '';
            $scope.links.logoUrl3 = '';

            if (idx > -1) {
                $scope.selection.splice(idx, 1);
            }
            else {
                $scope.selection.push(img);
            }

            console.log($scope.selection[0])
            if($scope.selection[0]){
                $scope.links.logoUrl1 = $scope.selection[0];
            }
            if($scope.selection[1]){
                $scope.links.logoUrl2 = $scope.selection[1];
            }
            if($scope.selection[2]){
                $scope.links.logoUrl3 = $scope.selection[2];
            }

        };
    }



})();
