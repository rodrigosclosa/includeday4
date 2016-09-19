angular.module('starter.services', [])

.factory('Drugs', function() {
  var drugs = [];

  return {
    all: function() {
      return drugs;
    },
    remove: function(chat) {
      drugs.splice(drugs.indexOf(chat), 1);
    },
    get: function(chatId) {
      for (var i = 0; i < drugs.length; i++) {
        if (drugs[i].id === parseInt(chatId)) {
          return drugs[i];
        }
      }
      return null;
    }
  };
});
