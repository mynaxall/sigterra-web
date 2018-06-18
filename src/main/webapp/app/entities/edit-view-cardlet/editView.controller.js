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

    EditViewController.$inject = ['$scope', '$state', 'CardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout', '$location', 'orderByFilter', 'PHONE_PATTERN', 'TOOLBAR_OPTIONS', 'ImageService', 'Carousel', 'EditViewerService', 'LINK_PATTERN'];

    function EditViewController($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http, $timeout, $location, orderByFilter, PHONE_PATTERN, TOOLBAR_OPTIONS, ImageService, Carousel, EditViewerService, LINK_PATTERN) {
        var vm = this;
        vm.success = false;
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
        vm.LINK_PATTERN = new RegExp(LINK_PATTERN);
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
        vm.activeWidget = 1;
        vm.bg = "/content/images/background.jpg";
        $scope.cardletView = {};
        $scope.isLogo = false;
        $scope.time = Date.now();
        $scope.cardletId = '';
        $scope.header = {
            'name' : '',
            'title' : '',
            'text' : 'Request For Demo',
            'logoUrl': '/content/images/page_logo.png',
            'photoUrl': '/content/images/User.png'};
        $scope.selection = [];
        $scope.background = {
            'text' : 'Conversational Commerce is the biggest opportunity for brands to act like people in digital channels & build customer engagement and loyalty',
            'textColor' : 'true',
            'imageUrl':'/content/images/viewpage/backgrounds/bg_theme08.png'
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

        $scope.imageIndex = null;
        $scope.imageError = false;

        $scope.widgets = {
            "cardletContentLibraryWidget": [],
            "cardletId": 0,
            "cardletQuickBitesWidget": [],
            "cardletTestimonialWidget": []
        };
        $scope.showDelteDialog = false;
        $scope.delteMsg = '';
        $scope.deleteId = '';
        $scope.isWidget = false;


        $scope.activetTestimonial = 0;
        $scope.activeContentLibrary = 0;
        $scope.activeQuickbite = 0;

        $scope.testimonials = [];
        $scope.contentLibrary = [];
        $scope.model = {};
        $scope.model.campuses = [];
        $scope.model.campuses1 = [];
        $scope.campuses1 = [];
        $scope.quickbites = [];

        $scope.myImage='';
        $scope.myCroppedImage = '';
        vm.saveImage = saveImage;
        vm.showImageDialog = showImageDialog;
        vm.hideImageDialog = hideImageDialog;
        vm.hideIconPopUpDialog = hideIconPopUpDialog;
        vm.showIconPopUpDialog = showIconPopUpDialog;
        vm.showWidgetImageDialog = showWidgetImageDialog;
        $scope.iconvalue = "";


        $scope.setActive = function (index) {
            vm.activeTab = index;
        }

        $scope.setActiveWidget = function (index) {
            vm.activeWidget = index;
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
        $scope.cardletId = $location.search().cardletId;

        $scope.getCardlet = function(){
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
                setValues(response);
            }).catch(function (response) {
            });
            $scope.signatureLink = $location.protocol() + '://' + $location.host() + ':' + $location.port()+'/#/previewCardlet?cardletId='+ window.btoa($scope.cardletId);
        };

        function setValues(response) {
            $scope.cardletView = response;
            if($scope.cardletView.header){
            if(!$scope.cardletView.header.logoUrl){
            $scope.cardletView.header.logoUrl = '/content/images/page_logo.png'
            }
            if(!$scope.cardletView.header.photoUrl){
            $scope.cardletView.header.photoUrl = '/content/images/User.png'
            }

            console.log($scope.cardletView.header)
                $scope.header = $scope.cardletView.header;
                if(!$scope.header.ctaColor){
                    $scope.header.ctaColor = "f0ad4e";
                }
            }else {
                $scope.header.ctaColor = "f0ad4e";
            }
            if($scope.cardletView.background){
                $scope.background = $scope.cardletView.background;

                if(!$scope.cardletView.background.imageUrl){
                 $scope.cardletView.background.imageUrl = '/content/images/viewpage/backgrounds/bg_theme08.png'
              }
            }
             if($scope.cardletView.links){
               $scope.links = $scope.cardletView.links;
               $scope.selection = [];

               if($scope.links.logoUrl1) {
                   //$scope.selection.push($scope.links.logoUrl1);
                   $scope.selection[0]= $scope.links.logoUrl1;

               }
               if($scope.links.logoUrl2) {
                        $scope.selection[1] =       $scope.links.logoUrl2 ;

                   //$scope.selection.push($scope.links.logoUrl2);
               }
               if($scope.links.logoUrl3) {
                            $scope.selection[2] =    $scope.links.logoUrl3;

                   //$scope.selection.push($scope.links.logoUrl3);
               }
             }


            if($scope.cardletView.footer){
                $scope.footer = $scope.cardletView.footer;
            }
        }

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

            EditViewerService.getWidgets($scope.cardletId).then(function (response) {
                $scope.widgets = response;

                $scope.getNsetWidgets();
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


        function showImageDialog(isLogo, type) {
            $scope.isWidget = false;
            $scope.isLogo = isLogo;
            angular.element('#fileInput').val(null);
            $scope.myImage = "";
            var imageUrl;
            if ($scope.header.logoUrl && $scope.isLogo) {
                imageUrl = $scope.header.logoUrl;
            }else if( $scope.header.photoUrl) {
                imageUrl = $scope.header.photoUrl;
            }
            if(imageUrl && !imageUrl.startsWith("/")){
                $http.get(imageUrl + "?"+ $scope.time, {responseType: "arraybuffer"})
                    .success(function (data) {
                        var str = _arrayBufferToBase64(data);
                        $scope.myImage = 'data:image/JPEG;base64,' + str;
                    });
            }
            $scope.myCroppedImage = '';
            vm.isShowDialog = true;
        }

        function showWidgetImageDialog(isWidget, index) {
            $scope.isWidget = isWidget;
            $scope.contentLibraryImageIndex = index;
            angular.element('#fileInput').val(null);
            $scope.myImage = "";
            vm.isShowDialog = true;
            var imageUrl;
            if ($scope.contentLibrary[index].coverImageUrl) {
                imageUrl = $scope.contentLibrary[index].coverImageUrl;
            }
            if(imageUrl && !imageUrl.startsWith("/")){
                $http.get(imageUrl, {responseType: "arraybuffer"})
                    .success(function (data) {
                        var str = _arrayBufferToBase64(data);
                        $scope.myImage = 'data:image/JPEG;base64,' + str;
                    });
            }
            $scope.myCroppedImage = '';
            vm.isShowDialog = true;
        }

        function showIconPopUpDialog(type, idx) {
           $scope.listIcons.listFilesPaths.forEach(function (listIcon, i) {
               listIcon.checked = false;
           });
           $scope.iconvalue = idx;
           $scope.gender = '';
            $scope.myImage = "";
           vm.isShowDialog1 = true;
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
            $scope.isWidget = false;
            vm.isShowDialog = false;
        }

        function hideIconPopUpDialog(clean){
            vm.isShowDialog1 = false;
        }

        function saveImage(){
            var url = '';
            if($scope.isWidget){
                url = '/api/cardlet/' + $scope.cardletId + '/widgets/images/upload'
            } else if($scope.isLogo){
                url = '/api/cardlet/pageview/logo-image/' + $scope.cardletId;
            }else{
                url = '/api/cardlet/pageview/photo-image/' + $scope.cardletId;
            }
            $scope.showSpinner = true;
            var img_b64 = $scope.myCroppedImage;
            var png = img_b64.split(',')[1];
            var file = b64toBlob(png, 'image/png')
            var fd = new FormData();

            if($scope.isWidget) {
                fd.append('coverImage', file);
            }else{
                fd.append('file', file);
            }
            $http.post(url,  fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
                .success(function (data, status, headers, config) {
                    if($scope.isWidget){
                        $scope.contentLibrary[$scope.contentLibraryImageIndex].coverImageUrl = data.coverImageUrl;
                    }else if($scope.isLogo){
                        $scope.header.logoUrl = data.url
                    }else{
                        $scope.header.photoUrl = data.url
                    }
                    $scope.showSpinner = false;
                    $scope.myImage='';
                    $scope.myCroppedImage = '';
                    vm.hideImageDialog();
                })
                .error(function (error) {
                    vm.hideImageDialog();
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

        $scope.save = function (isSave) {
            $scope.showSpinner = true;
            $scope.cardletView.header = $scope.header;
            $scope.cardletView.background = $scope.background;
            $scope.cardletView.links = $scope.links;
            $scope.cardletView.footer = $scope.footer;
            $scope.cardletView.cardletId = $scope.cardletId;
            $scope.widgets.cardletTestimonialWidget = $scope.testimonials;
            $scope.widgets.cardletQuickBitesWidget = Array.prototype.concat.apply([], $scope.campuses1);
            $scope.widgets.cardletQuickBitesWidget = $scope.widgets.cardletQuickBitesWidget.filter(function(item){
                            return item.title.trim().length>0 && item.description.trim().length>0

                    });
            $scope.widgets.cardletContentLibraryWidget = $scope.contentLibrary;
            $scope.widgets.cardletId = $scope.cardletId;
            EditViewerService.updateViewer($scope.cardletView ).then(function (response) {

                setValues(response);
                $scope.time = Date.now();

                EditViewerService.updateWidgets($scope.widgets).then(function (response) {
                    $scope.showSpinner = false;
                    $scope.widgets = response;
                    $scope.setWidgets();
                    if(isSave) {
                        vm.success = true;
                        $timeout(function() {
                            vm.success = false;
                        }, 3000);
                    }else{
                        $location.path('/edit-cardlet-list').search({cardletId: $scope.cardletId});
                    }
                }).catch(function (response) {
                    $scope.showSpinner = false;
                });

            }).catch(function (response) {
                $scope.showSpinner = false;
            });

        }

        $scope.select = function (index) {
            $scope.background.imageUrl = $scope.bgArray.listFilesPaths[index].url;
        }

        $scope.toggleSelection = function toggleSelection(position, img) {
        var idx = $scope.selection.indexOf(img);
               $scope.selection[$scope.iconvalue] = img ;
           setSelction();
       };

        $scope.deleteIcon = function(id){

        $scope.links.logoUrl1 = '';
        $scope.links.logoUrl2 = '';
        $scope.links.logoUrl3 = '';

        if(id === 0){
        $scope.links.name1 ="";
        $scope.links.url1 ="";
        $scope.selection[0] = "";
           $scope.links.logoUrl1 = "";
           $scope.links.logoUrl2 = $scope.selection[1];
           $scope.links.logoUrl3 = $scope.selection[2];
        }
        if(id === 1){
        $scope.selection[1] = "";

        $scope.links.name2 ="";
        $scope.links.url2 ="";
        $scope.links.logoUrl2 = "";

        $scope.links.logoUrl1 = $scope.selection[0];
        $scope.links.logoUrl3 = $scope.selection[2];
        }
        if(id === 2){
        $scope.selection[2] = "";
        $scope.links.name3 ="";
        $scope.links.url3 ="";
        $scope.links.logoUrl1 = $scope.selection[0];
        $scope.links.logoUrl2 = $scope.selection[1];
        $scope.links.logoUrl3 = "";
        }
       }

        function setSelction() {
            $scope.links.logoUrl1 = '';
            $scope.links.logoUrl2 = '';
            $scope.links.logoUrl3 = '';

            if($scope.selection[0]){
                $scope.links.logoUrl1 = $scope.selection[0];
            }
            if($scope.selection[1]){
                $scope.links.logoUrl2 = $scope.selection[1];
            }
            if($scope.selection[2]){
                $scope.links.logoUrl3 = $scope.selection[2];
            }
        }

        $scope.gerPreviewLink = function (id) {
            return ($location.protocol() + '://' + $location.host() + ':' + $location.port() + '/#/previewCardlet?cardletId=' + setId(id))
        }

        function setId(id) {
            return window.btoa(window.btoa(window.btoa(window.btoa(id))));
        }

        $scope.addTestimonial = function () {
            var newTestimonial = {
                'name': '',
                'coName': '',
                'designation': '',
                'description': ''

            };
            $scope.testimonials.push(newTestimonial);
            $scope.toLastTestimonial();
        }

        $scope.deleteWidget = function () {
            $scope.showSpinner = true;
            if($scope.isDelteTestimonial){
                $scope.deleteTestimonial();
            }
            else if($scope.isDeleteQuickbite){
              $scope.deleteQuickbite();
            }
            else{
                $scope.deleteContentLibrary();
            }
        }


        $scope.deleteTestimonial = function () {

            if($scope.testimonials[$scope.deleteId].id) {
                EditViewerService.deleteWidget($scope.testimonials[$scope.deleteId].id).then(function (response) {
                    $scope.testimonials.splice($scope.deleteId, 1);
                    $scope.toLastTestimonial();
                    $scope.showSpinner = false;
                }).catch(function (response) {
                    $scope.showSpinner = false;
                });
            }else{
                $scope.testimonials.splice($scope.deleteId, 1);
                $scope.toLastTestimonial();
                $scope.showSpinner = false;
            }
            $scope.showDelteDialog = false;
        };

        $scope.toLastTestimonial = function () {
            var index = $scope.testimonials.length - 1;
            $scope.activetTestimonial = index;

        }

        $scope.setActiveTestimonial = function (index) {
            $scope.activetTestimonial = index;
        }

        $scope.prevTimonialSlide = function(index){
            $scope.currentUrl = undefined;
            if( $scope.activetTestimonial == 0){
                $scope.activetTestimonial = $scope.testimonials.length -1
            }else{
                $scope.activetTestimonial = parseInt($scope.activetTestimonial) - 1;
            }

        };

        $scope.nextTimonialSlide = function(index){
            $scope.currentUrl = undefined
            if( $scope.activetTestimonial == $scope.testimonials.length -1){
                $scope.activetTestimonial = 0
            }else{
                $scope.activetTestimonial = parseInt($scope.activetTestimonial) + 1;
            }

        };
        $scope.setWidgets = function () {
            $scope.testimonials = $scope.widgets.cardletTestimonialWidget;
            $scope.contentLibrary = $scope.widgets.cardletContentLibraryWidget;
            $scope.quickbites = Array.prototype.concat.apply([], $scope.campuses1);
            $scope.quickbites = $scope.widgets.cardletQuickBitesWidget;
            $scope.campuses1 = chunkArray($scope.widgets.cardletQuickBitesWidget, 3);
            angular.forEach($scope.campuses1, function(value, key){
            if(value.length == 2){
                  $scope.campuses1[key].push({"title":"", "description":""});
            }
            else if(value.length == 1){
                for(i=0; i<=2; i++){
                 $scope.campuses1[key].push({"title":"", "description":""});
                }
              }
           });
        }
        $scope.getNsetWidgets = function () {
            $scope.testimonials = $scope.widgets.cardletTestimonialWidget;
            $scope.quickbites = $scope.widgets.cardletQuickBitesWidget;
            var resultArray = chunkArray($scope.quickbites, 3)
            $scope.campuses1 = resultArray;
           angular.forEach($scope.campuses1, function(value, key){
            if(value.length == 2){
                  $scope.campuses1[key].push({"title":"", "description":""});
            }
            else if(value.length == 1){
                for(i=0; i<=2; i++){
                 $scope.campuses1[key].push({"title":"", "description":""});
                }
              }
           });
            $scope.contentLibrary = $scope.widgets.cardletContentLibraryWidget;
        }

        function chunkArray(myArray, chunk_size){
            var i, j, resArray=[];
            for (i = 0, j = myArray.length; i < j; i += chunk_size) {
              resArray.push(myArray.slice(i, i + chunk_size));
            }
            return resArray;
        }

        $scope.addQuickbites = function () {
        var test = new Array();
        for(i=1;i<=3;i++){
            test.push({"title":"title"+i, "description":"description"+i});
            }
        $scope.campuses1.push(test);
        $scope.quickbites = Array.prototype.concat.apply([], $scope.campuses1);
        $scope.toLastQuickbite();
        }

        $scope.setActiveQuckbite = function (index) {
                   $scope.activeQuickbite = index;
                }

        $scope.showDeleteQuickbiteDialog = function (prId, chId, isQuickbite) {
            $scope.isDeleteQuickbite = isQuickbite;
            $scope.deleteMsg = "Do you want to delete this Quickbite?";
            $scope.showDelteDialog = true;
            $scope.deleteprId = prId;
            $scope.deletechId = chId;
        }
        $scope.deleteQuickbite = function () {
           if($scope.campuses1[$scope.deleteprId][$scope.deletechId].id) {

               EditViewerService.deleteQuickbite($scope.campuses1[$scope.deleteprId][$scope.deletechId].id).then(function (response) {
               $scope.campuses1[$scope.deleteprId][$scope.deletechId].title = "";
               $scope.campuses1[$scope.deleteprId][$scope.deletechId].description = "";
                  $scope.toLastQuickbite();
                   $scope.showSpinner = false;
               }).catch(function (response) {
                   $scope.showSpinner = false;
               });
           }else{
              console.log($scope.campuses1[$scope.deleteprId][$scope.deletechId])
              $scope.campuses1[$scope.deleteprId][$scope.deletechId].title = "";
              $scope.campuses1[$scope.deleteprId][$scope.deletechId].description = "";
                 $scope.toLastQuickbite();
                 $scope.showSpinner = false;
           }
           $scope.showDelteDialog = false;
       };

        $scope.prevQuickbiteSlide = function(index){
           $scope.currentUrl = undefined;
           if( $scope.activeQuickbite == 0){
               $scope.activeQuickbite = $scope.campuses1.length -1
           }else{
               $scope.activeQuickbite = parseInt($scope.activeQuickbite) - 1;
           }

        };

        $scope.nextQuickbiteSlide = function(index){

           $scope.currentUrl = undefined
           if( $scope.activeQuickbite == $scope.campuses1.length -1){
           $scope.activeQuickbite = 0
           }else{
               $scope.activeQuickbite = parseInt($scope.activeQuickbite) + 1;
           }

        };

        $scope.toLastQuickbite = function () {
        var index = $scope.campuses1.length - 1;
        $scope.activeQuickbite = index;

        };


        $scope.isInvalid = function() {
            for(var i = 0; i < $scope.testimonials.length; i++) {
                var testimonial = $scope.testimonials[i];
                if(!testimonial.name ||!testimonial.description){
                    return true
                }
            }
            for(var j = 0; j < $scope.contentLibrary.length; j++) {
                var value = $scope.contentLibrary[j];
                if(!value.title || !value.uploadFileUrl ||!value.coverImageUrl){
                    return true
                }
            }

            return false;
        }

        $scope.showDeleteDialog = function (id, isTestimonial) {
            $scope.isDelteTestimonial = isTestimonial;
            $scope.deleteMsg = "Do you want to delete this testimonial?";
            $scope.showDelteDialog = true;
            $scope.deleteId = id;
        }

        $scope.closeDialog = function () {
            $scope.showDelteDialog = false;
        }


        $scope.addContentLibrary = function () {
            var newContentLibrary = {
                'title': '',
                'uploadFileUrl': '',
                'coverImageUrl': ''

            };
            $scope.contentLibrary.push(newContentLibrary);
            $scope.toLastContentLibrary();
        }


        $scope.toLastContentLibrary = function () {
            var index = $scope.contentLibrary.length - 1;
            $scope.activeContentLibrary = index;

        }

        $scope.deleteContentLibrary = function () {

            if($scope.contentLibrary[$scope.deleteId].id) {
                EditViewerService.deleteContentLibrary($scope.contentLibrary[$scope.deleteId].id).then(function (response) {
                    $scope.contentLibrary.splice($scope.deleteId, 1);
                    $scope.toLastContentLibrary();
                    $scope.showSpinner = false;
                }).catch(function (response) {
                    $scope.showSpinner = false;
                });
            }else{
                $scope.contentLibrary.splice($scope.deleteId, 1);
                $scope.toLastContentLibrary();
                $scope.showSpinner = false;
            }
            $scope.showDelteDialog = false;
        };


        $scope.prevContentLibrarySlide = function(){
            if( $scope.activeContentLibrary == 0){
                $scope.activeContentLibrary = $scope.contentLibrary.length -1
            }else{
                $scope.activeContentLibrary = parseInt($scope.activeContentLibrary) - 1;
            }

        };

        $scope.nextContentLibrarySlide = function(){

            if( $scope.activeContentLibrary == $scope.contentLibrary.length -1){
                $scope.activeContentLibrary = 0
            }else{
                $scope.activeContentLibrary = parseInt($scope.activeContentLibrary) + 1;
            }

        };

        $scope.setActiveContentLibrary = function (index) {
            $scope.activeContentLibrary = index;
        }


        $scope.fileNameChanged = function (files, index){

            $scope.showSpinner = true;
            if(files.length > 0 && files[0].size >= 50*1024*1024){
                $scope.imageError = true;
                $scope.imageIndex = index;
                $scope.showSpinner = false;
                return;
            } else if (files.length > 0) {
                var file=  getBase64(files[0]);
            }
            $scope.imageError = false;
            var fd = new FormData();

            fd.append('uploadFile', file);


            EditViewerService.uploadFile($scope.cardletId,  fd).then(function (response) {
                $scope.showSpinner = false;
                $scope.contentLibrary[index].uploadFileUrl = response.data.uploadFileUrl;

            }).catch(function (response) {
                $scope.showSpinner = false;
            });
        };

        function getBase64(file) {
            var reader = new FileReader();
            reader.onload = function () {
                var base64str = reader.result;
                var base64 = base64str.split(',')[1];
                var file = b64toBlob(base64, 'application/pdf');

            };
            return file;
        };
    }

})();
