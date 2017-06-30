(function() {
    'use strict';

    angular
        .module('sigterraWebApp')
        .controller('EditCardletListController', EditCardletListController);

    EditCardletListController.$inject = ['$scope', '$state', 'CardletList', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$http', '$timeout', '$location'];

    function EditCardletListController ($scope, $state, CardletList, ParseLinks, AlertService, pagingParams, paginationcardletConstants ,$http, $timeout, $location) {
        var vm = this;

        $scope.showCropDialog = false;
        $scope.firstBusinessCardId ="";
        $scope.myImage='';
        $scope.myCroppedImage = '';
        $scope.showConent = true;
        $scope.noSocialChanges = false;

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

        $scope.handleFileSelect=function(evt) {
            var file=evt.currentTarget.files[0];
            var reader = new FileReader();
            reader.onload = function (evt) {
                $scope.$apply(function($scope){
                    $scope.myImage=evt.target.result;
                });
            };
            reader.readAsDataURL(file);
        };

        $scope.closeCropDialog = function(){
            $scope.socialLinks = {twitter: "", facebook: "", google: "", linkedin: ""};
            $scope.showCropDialog = false;
            $scope.showSocialDialog = false;
            $scope.myImage = "";
            angular.element(document.querySelector('#fileInput')).val(null);
        }


        $scope.showSocialDialog = false;

        $scope.socialLinks = {twitter: "", facebook: "", google: "", linkedin: ""};

        $scope.openSocialDialog = function(index, links){
            console.log(index)
            $scope.tabIndex = index;
            $scope.showSocialDialog = true;
            $scope.socialLinks = links;
        }

        $scope.saveSocialLinks = function(){
            $scope.tabNames.tabs[$scope.tabIndex].socialLinks = $scope.socialLinks;
            $scope.showSocialDialog = false;
        }


        $scope.openCropDialog = function(tabId, itemId, itemImgPosition){
            $scope.showCropDialog = true;
            $scope.tabImageId = tabId;
            $scope.itemImageId = itemId;
            $scope.imageItemMame =itemImgPosition;
            $scope.myCroppedImage = '';
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
        };


        $scope.saveImage = function(){
            $scope.showSpinner = true;
            $scope.myImage = ""

            var img_b64 = $scope.myCroppedImage;
            var png = img_b64.split(',')[1];
            var file = b64toBlob(png, 'image/png')
            var fd = new FormData();
            fd.append('file', file);
            var url = "";

            console.log($scope.itemImageId)
            console.log($scope.imageItemMame)
            console.log($scope.tabImageId)
            if($scope.itemImageId != null){
                 url =  $scope.tabNames.tabs[$scope.tabImageId].items[$scope.itemImageId][$scope.imageItemMame];

            }else{
                 url = $scope.tabNames.tabs[$scope.tabImageId].photo;

            }
            var filename = url.substring(url.lastIndexOf('/')+1);
            if(filename.indexOf(".")!= -1){
                filename = "name"
            }

            $http.post("/api/cardlet/upload/icon/"+filename,  fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .success(function (data, status, headers, config) {
                    $scope.imageUrl = data.url;
                    if($scope.itemImageId != null){
                        $scope.tabNames.tabs[$scope.tabImageId].items[$scope.itemImageId][$scope.imageItemMame] = $scope.imageUrl;
                    }else {
                        setTabImage();
                    }
                    $scope.showCropDialog = false;
                    $scope.showSpinner = false;
                });
            angular.element(document.querySelector('#fileInput')).val(null);
        };

        function setTabImage(){
            $scope.tabNames.tabs[$scope.tabImageId].photo = $scope.imageUrl;
        }

        $scope.autoExpand = function(e) {
            var element = typeof e === 'object' ? e.target : document.getElementById(e);
            var scrollHeight = element.scrollHeight ; // replace 60 by the sum of padding-top and padding-bottom
            element.style.height =  scrollHeight + "px";
        };


        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;


        $scope.getCardlet = function(){
            var param1 = $location.search().cardletId;
            $http.get("/api/cardlet/"+param1)
                .success(function(response, status, headers) {
                    $scope.tabNames = response;
                    for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                        if ($scope.tabNames.tabs[i].tabType == '1'){
                            $scope.firstBusinessCardId = i;
                            break
                        }else{
                            $scope.firstBusinessCardId = '';
                        }
                    }
                    setTimeout(function () {
                        document.getElementsByClassName("tabcontent2")[0].style.display = "block";
                        document.getElementsByClassName("tabs2")[0].className += " active";
                        document.getElementsByClassName("tabcontent")[0].style.display = "block";
                        document.getElementsByClassName("tabs")[0].className += " active";
                    }, 500)
                });
            $scope.signatureLink = $location.protocol() + '://' + $location.host() + ':' + $location.port()+'/#/previewCardlet?cardletId='+ param1;
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

            console.log($scope.isEmptyName())
            console.log(!$scope.isDisabledTabs)



            if(!$scope.isDisabledTabs && $scope.isEmptyName()) {

                var i, tabcontent, tablinks, tabs;
                tabcontent = document.getElementsByClassName("tabcontent");
                for (i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].style.display = "none";
                }
                tablinks = document.getElementsByClassName("tablinks");
                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");

                }
                tabs = document.getElementsByClassName("tabs");

                for (i = 0; i < tabs.length; i++) {
                    tabs[i].className = tabs[i].className.replace(" active", "");

                }
                document.getElementById(cityName).style.display = "block";
                document.getElementById(tabId).className += " active";

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


        $scope.showSignature = function(){
            $http.get("/api/signatures")
                .success(function(response, status, headers) {

                    $scope.signatures = response;
                });
        }


        $scope.userCard= function(){
            $http.get("/api/userCardlets")
                .success(function(response, status, headers) {

                    $scope.signatures = response;
                });
        }

        $scope.userCard();




        $scope.getTabTypes = function(){
            $http.get("/api/tab-types-by-type/1")
                .success(function(response, status, headers) {
                    $scope.tabTypes = response;

                });
        }

        $scope.getItemTypes = function(){
            $http.get("/api/tab-types-by-type/2")
                .success(function(response, status, headers) {
                    $scope.itemTypes = response;

                });
        }


        $scope.getInfoTypes = function(){
            $http.get("/api/tab-types-by-type/3")
                .success(function(response, status, headers) {
                    $scope.infoTypes = response;
                });
        }

        $scope.getItemTypes();
        $scope.getTabTypes();
        $scope.getInfoTypes();


        $scope.addInfo = function() {
            if ($scope.tabNames.tabs.length <= 3) {
                var newTab = {"name":"Text Items "+$scope.tabNames.tabs.length,
                    "position": $scope.tabNames.tabs.length,
                    "tabType": 3,
                    "layout":{
                        "tabId": $scope.infoTypes[0].id,
                        "url": $scope.infoTypes[0].path,
                        "mainColor": "FFFFFF",
                        "secondaryColor": "4BABE2"
                    },
                    "items": [
                        {
                            //"name": "1 item",
                            "index": "2",
                            "position": "0",
                            "image": "/content/images/portfolio_img_01.png",
                            "image2": "/content/images/portfolio_img_02.png",
                            "image3": "/content/images/portfolio_img_03.png"
                        }
                    ]

                }
                setTimeout(function(){
                    $scope.openCity('settings'+newTab.name+newTab.position, newTab.position, newTab.name+newTab.position, 'card'+newTab.position+newTab.name)}, 500)
                $scope.tabNames.tabs.push(newTab);
            }
            if($scope.tabNames.tabs.length == 4){
                $scope.isNewTab = false;
            }
        }

        $scope.addItem = function() {
            if ($scope.tabNames.tabs.length <= 3) {
                var newTab = {"name":"Image Items "+$scope.tabNames.tabs.length,
                    "position": $scope.tabNames.tabs.length,
                    "tabType": 2,
                    "layout":{
                        "tabId": $scope.itemTypes[0].id,
                        "url": $scope.itemTypes[0].path,
                        "mainColor": "FFFFFF",
                        "secondaryColor": "4BABE2"
                    },
                    "items": [
                        {
                            //"name": "1 item",
                            "index": "2",
                            "position": "0",
                            "image": "/content/images/portfolio_img_01.png",
                            "image2": "/content/images/portfolio_img_02.png",
                            "image3": "/content/images/portfolio_img_03.png"
                        }
                    ]

                }
                setTimeout(function(){
                    $scope.openCity('settings'+newTab.name+newTab.position, newTab.position, newTab.name+newTab.position, 'card'+newTab.position+newTab.name)}, 500)
                $scope.tabNames.tabs.push(newTab);
            }
        }

        $scope.addItems = function(tabId, index) {
            if($scope.tabNames.tabs[tabId].items.length <= 9){
                var newItem =  {
                    //"name":  ($scope.tabNames.tabs[tabId].items.length+1)+" item",
                    "index": index+2,
                    "position": $scope.tabNames.tabs[tabId].items.length,
                    "image": "/content/images/portfolio_img_01.png",
                    "image2": "/content/images/portfolio_img_02.png",
                    "image3": "/content/images/portfolio_img_03.png",
                }
                $scope.tabNames.tabs[tabId].items.push(newItem);
                $scope.changeAccordionActivity(index+2)

                $scope.slides2 = $scope.tabNames.tabs[tabId].items;

            }
        }


        $scope.getUserProfile = function(){
            $http.get("/api/account")
                .success(function(response, status, headers) {
                    $scope.userAccount = response;
                    $scope.tabNames.tabs[0].userName = {
                        "value": $scope.userAccount.username
                    }
                    $scope.tabNames.tabs[0].userEmail = {
                        "value": $scope.userAccount.email
                    }

                    $scope.tabNames.tabs[0].phone = {
                        "value": $scope.userAccount.phoneNumber
                    }

                    $scope.tabNames.tabs[0].address = {
                        "value": $scope.userAccount.address
                    }

                    $scope.tabNames.tabs[0].company = {
                        "value": $scope.userAccount.companyName
                    }

                    $scope.tabNames.tabs[0].site = {
                        "value": $scope.userAccount.companySite
                    }

                    $scope.tabNames.tabs[0].job = {
                        "value": $scope.userAccount.jobTitle
                    }


                });
        }
        $scope.getUserProfile();


        $scope.addTab = function() {
            if ($scope.tabNames.tabs.length <= 3) {
                var newTab = {"name":"Business Card "+$scope.tabNames.tabs.length,
                    "position": $scope.tabNames.tabs.length,
                    "tabType": 1,
                    "layout":{
                        "tabId": $scope.tabTypes[0].id,
                        "url": $scope.tabTypes[0].path,
                        "mainColor": "FFFFFF",
                        "secondaryColor": "4BABE2"
                    },
                    "userName": {
                    "value":  $scope.userAccount.username
                    },

                    "userEmail":{
                        "value": $scope.userAccount.email
                    },
                    "phone":{
                        "value": $scope.userAccount.phoneNumber
                    },
                    "address":{
                        "value": $scope.userAccount.address
                    },
                    "company":{
                        "value": $scope.userAccount.companyName
                    },
                    "site":{
                        "value": $scope.userAccount.companySite
                    },
                    "job":{
                        "value": $scope.userAccount.jobTitle
                    },
                    "photo":  "/content/images/avatar_img.png"

                    }
                if($scope.userAccount.imageUrl){
                    newTab.photo = $scope.userAccount.imageUrl
                }
                $scope.tabNames.tabs.push(newTab);
                if($scope.firstBusinessCardId === '' || $scope.firstBusinessCardId < 0 || $scope.tabNames.tabs[$scope.firstBusinessCardId].tabType != '1'){
                    $scope.firstBusinessCardId = newTab.position;
                }
                setTimeout(function(){
                    $scope.openCity('settings'+newTab.name+newTab.position, newTab.position, newTab.name+newTab.position, 'card'+newTab.position+newTab.name)}, 500)
            }
        }

        $scope.deleteItems = function(tabId, index){

            console.log("asdasd")
            if($scope.tabNames.tabs[tabId].items.length > 1){

                if($scope.tabNames.removeItems == null){
                    $scope.tabNames.removeItems = [];
                }
                if($scope.tabNames.tabs[tabId].items[index-2].id) {
                    $scope.tabNames.removeItems.push($scope.tabNames.tabs[tabId].items[index - 2].id);
                }

                $scope.tabNames.tabs[tabId].items.splice((index-2), 1);
                for (var i = 0; i < $scope.tabNames.tabs[tabId].items.length; i++) {
                    $scope.tabNames.tabs[tabId].items[i].index = i + 2;
                    $scope.tabNames.tabs[tabId].items[i].position = i;
                }
            }
        }

        $scope.showSpinner = false;

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

            setTimeout(function() {
                document.getElementsByClassName("tabcontent2")[0].style.display = "block";
                document.getElementsByClassName("tabs2")[0].className += " active";
                document.getElementsByClassName("tabcontent")[0].style.display = "block";
                document.getElementsByClassName("tabs")[0].className += " active";
                for (var i = 0; i < tabs.length; i++) {
                    document.getElementsByClassName("tablinks")[i].className = document.getElementsByClassName("tablinks")[i].className.replace(" disabledLink", "");
                }
            },100);

        };


        $scope.showDelteTabDialog = false;

        $scope.showDeleteDialog = function(id){
            $scope.showDelteTabDialog = true;
            $scope.tabToDeleteID = id
        };


        $scope.closeDialog = function(){
            $scope.showDelteTabDialog = false;
        };



        $scope.removeTab = function() {
            var index = $scope.tabToDeleteID;
            if($scope.tabNames.tabs.length == 2){
                $scope.firstBusinessCardId = 0;
            }
            if($scope.tabNames.tabs.length >1) {

                if (($scope.tabNames.tabs[index].tabType === 2 || $scope.tabNames.tabs[index].tabType === 3) && $scope.tabNames.tabs[index].id) {
                    if ($scope.tabNames.removeTabs == null) {
                        $scope.tabNames.removeTabs = [];
                    }
                    $scope.tabNames.removeTabs.push($scope.tabNames.tabs[index].id);
                }
                if ($scope.tabNames.tabs[index].tabType === 1 && $scope.tabNames.tabs[index].id) {
                    if ($scope.tabNames.removeBusiness == null) {
                        $scope.tabNames.removeBusiness = [];
                    }
                    $scope.tabNames.removeBusiness.push($scope.tabNames.tabs[index].id);
                }
                $scope.tabNames.tabs.splice(index, 1);
                for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                    $scope.tabNames.tabs[i].position = i;
                }

                if($scope.tabNames.tabs.length >1) {
                    for (var i = 0; i < $scope.tabNames.tabs.length; i++) {
                        if ($scope.tabNames.tabs[i].tabType == '1') {
                            $scope.firstBusinessCardId = i;
                            break
                        }
                    }
                }
                setTimeout(function(){

                    var tabs2 = document.getElementsByClassName("tabs2");
                    var tabs = document.getElementsByClassName("tabs");

                    for (var i = 0; i < tabs.length; i++) {
                        if(angular.element(tabs[i]).hasClass('active')){
                            document.getElementsByClassName("tabcontent")[i].style.display = "none";;
                            tabs[i].className = tabs[i].className.replace(" active", "");
                        }

                    }

                    for (var i = 0; i < tabs2.length; i++) {
                        if(angular.element(tabs2[i]).hasClass('active')){
                            document.getElementsByClassName("tabcontent2")[i].style.display = "none";;
                            tabs2[i].className = tabs2[i].className.replace(" active", "");
                        }

                    }
                    document.getElementsByClassName("tabcontent2")[0].style.display = "block";
                    document.getElementsByClassName("tabs2")[0].className += " active";
                    document.getElementsByClassName("tabcontent")[0].style.display = "block";;
                    document.getElementsByClassName("tabs")[0].className += " active";

                }, 500);
            }
            $scope.showDelteTabDialog = false;
        }

        $scope.chooseType = function(id, url, tabId) {
            vm.currentSlide = 1;
            $scope.tabNames.tabs[id].layout.url = url;
            $scope.tabNames.tabs[id].layout.tabId = tabId;

        }


        $scope.isDisabledTabs = false;

        $scope.disableTabs = function(){
            $scope.isDisabledTabs = true;
        }

        $scope.enableTabs = function(){
            console.log("enableTabs")
            $scope.isDisabledTabs = false;
        }

        $scope.tabWidth = function( id , colorId){

            var cyrrentEl = document.getElementById(id);
            if($scope.tabNames) {

                if($scope.tabNames.tabs.length == 1){
                    cyrrentEl.style.width = "646px"
                }

                if ($scope.tabNames.tabs.length === 2) {
                    cyrrentEl.style.width = "323px"
                }
                if ($scope.tabNames.tabs.length === 3) {
                    cyrrentEl.style.width = "215px"
                }
                if ($scope.tabNames.tabs.length === 4) {
                    cyrrentEl.style.width = "175px"
                }
                if(colorId){
                    cyrrentEl.style.color = "#"+colorId;
                }
            }
        };


        $scope.itemPosition = function(index){
            return index + 1;
        }


        $scope.addColors = function(id, colorMain, colorSecond){

            var cyrrentEl = document.getElementById(id);

            if(cyrrentEl) {

                if ($scope.tabNames) {

                    if ($scope.tabNames.tabs.length == 1) {
                        cyrrentEl.style.width = "540px"
                    }

                    if ($scope.tabNames.tabs.length === 2) {
                        cyrrentEl.style.width = "270px"
                    }
                    if ($scope.tabNames.tabs.length === 3) {
                        cyrrentEl.style.width = "180px"
                    }
                    if ($scope.tabNames.tabs.length === 4) {
                        cyrrentEl.style.width = "135px"
                    }
                }
                setTimeout(function() {
                    cyrrentEl.style.background = "#F9F9F9";
                    cyrrentEl.style.borderTop = "1px solid #D0D8D9"
                    cyrrentEl.style.borderBottom = "1px solid #D0D8D9";
                    if (angular.element(document.getElementById(id)).hasClass('active')) {
                        cyrrentEl.style.background = "#FFFFFF";
                        cyrrentEl.style.borderTop = "2px solid #" + colorSecond
                        cyrrentEl.style.borderBottom = "0px";
                    }
                }, 700)
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
            console.log(data)
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
    }


})();
