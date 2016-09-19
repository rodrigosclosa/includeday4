// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'starter.controllers', 'starter.services'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }

    if (window.plugins && window.plugins.OneSignal) {
      console.log("Achei o plugin do OneSignal");
      // Enable to debug issues.
      //window.plugins.OneSignal.setLogLevel({logLevel: 4, visualLevel: 4});
      
      var notificationOpenedCallback = function(jsonData) {
        console.log('didReceiveRemoteNotificationCallBack: ' + JSON.stringify(jsonData));
      };

      window.plugins.OneSignal.init("d87de829-2bfc-4248-a864-845fc611ccc8",
                                    {googleProjectNumber: "740073579738"},
                                    notificationOpenedCallback);
      
      // Show an alert box if a notification comes in when the user is in your app.
      window.plugins.OneSignal.enableInAppAlertNotification(true);
      //window.plugins.OneSignal.enableNotificationsWhenActive(true);

      //Mocado para enviar o push correto pro Device
      window.plugins.OneSignal.sendTag("username", "59800379000227850019");
    }
  });
})

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider

  // setup an abstract state for the tabs directive
  .state('tab', {
    url: '/tab',
    abstract: true,
    templateUrl: 'templates/tabs.html'
  })

  // Each tab has its own nav history stack:
  .state('tab.dispenser', {
      url: '/dispenser',
      views: {
        'tab-dispenser': {
          templateUrl: 'templates/tab-dispenser.html',
          controller: 'DispenserCtrl'
        }
      }
    })
    .state('tab.dispenser-detail', {
      url: '/dispenser/:codigoMedicamento',
      views: {
        'tab-dispenser': {
          templateUrl: 'templates/dispenser-detail.html',
          controller: 'DispenserDetailCtrl'
        }
      }
    })

  .state('tab.pulseira', {
      url: '/pulseira',
      views: {
        'tab-pulseira': {
          templateUrl: 'templates/tab-pulseira.html',
          controller: 'PulseiraCtrl'
        }
      }
    });

  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/tab/dispenser');

});
