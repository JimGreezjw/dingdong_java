var ddpatientApp = angular.module('ddpatientApp', ['ionic', 'ddpatientFilters', 'ddpatientControllers', 'ddChatController', 'ddpatientServices', 'ddpatientRoutes', 'ddpatientDirectives']);
ddpatientApp
    .run(function ($ionicPlatform) {
        $ionicPlatform.ready(function () {

            if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
                cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
                cordova.plugins.Keyboard.disableScroll(true);

            }
            if (window.StatusBar) {
                // org.apache.cordova.statusbar required
                StatusBar.styleDefault();
            }
        });
    })
    .config(ddpatientRoutes);


