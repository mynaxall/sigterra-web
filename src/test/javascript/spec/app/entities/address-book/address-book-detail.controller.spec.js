'use strict';

describe('Controller Tests', function() {

    describe('AddressBook Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAddressBook, MockUser, MockCardlet;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAddressBook = jasmine.createSpy('MockAddressBook');
            MockUser = jasmine.createSpy('MockUser');
            MockCardlet = jasmine.createSpy('MockCardlet');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AddressBook': MockAddressBook,
                'User': MockUser,
                'Cardlet': MockCardlet
            };
            createController = function() {
                $injector.get('$controller')("AddressBookDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sigterraWebApp:addressBookUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
