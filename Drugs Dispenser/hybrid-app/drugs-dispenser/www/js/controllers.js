var codigoDispenser = "A1B2C3D4E5F6";

angular.module('starter.controllers', [])
.controller('PulseiraCtrl', function($scope) {


})
.controller('DispenserCtrl', function($scope, $http, $ionicHistory, $ionicPopup) {
  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  //$scope.drugs = Drugs.all();
  // $scope.remove = function(drug) {
  //   Drugs.remove(drug);
  // };
  var drugs = [];

  $http.get('https://drugs-dispenser.appspot.com/_ah/api/dispenser/v1/codigo/' + codigoDispenser).
    success(function(data, status, headers, config) {
      if(data != null) {
        var drug1 = {
          id: data.id,
          codigoMedicamento: data.codigoMedicamento1,
          medicamento: data.descricaoMedicamento1 
        };

        var drug2 = {
          id: data.id,
          codigoMedicamento: data.codigoMedicamento2,
          medicamento: data.descricaoMedicamento2
        };

        var drug3 = {
          id: data.id,
          codigoMedicamento: data.codigoMedicamento3,
          medicamento: data.descricaoMedicamento3
        };

        var drug4 = {
          id: data.id,
          codigoMedicamento: data.codigoMedicamento4,
          medicamento: data.descricaoMedicamento4
        };

        drugs.push(drug1);
        drugs.push(drug2);
        drugs.push(drug3);
        drugs.push(drug4);
      }

      $scope.drugs = drugs;
      $scope.codigoDispenser = codigoDispenser;
    }).
    error(function(data, status, headers, config) {
      alert("Erro ao obter os medicamentos!");
    });

    // An alert dialog
    $scope.showAlert = function(title, text, callback) {
      var alertPopup = $ionicPopup.alert({
        title: title,
        template: text
      });

      if(typeof(callback) === "function") {
        alertPopup.then(function(res) {
          callback();
        });
      }
    };

    $scope.resetTodasDatas = function(codigoDispenser) {
      $http.post('https://drugs-dispenser.appspot.com/_ah/api/agenda/v1/resettodasdatas/' + codigoDispenser).
        success(function(data, status, headers, config) {
          console.log(data);
          if(status == 204) {
            $scope.showAlert("Tudo certo", "Datas resetadas com sucesso!", function() {
              $scope.carregaDados();
            });
          } else {
            console.log(data);
            console.log(status);
            $scope.showAlert("Oops!", "Erro ao resetar as datas dos medicamentos.");
          }
        }).
        error(function(data, status, headers, config) {
          console.log(data);
          console.log(status);
          $scope.showAlert("Oops!", "Erro ao resetar as datas dos medicamentos.");
      });
    };

})

.controller('DispenserDetailCtrl', function($scope, $stateParams, $http, $ionicHistory, $ionicPopup) {

  $scope.carregaDados = function() {
    $http.get('https://drugs-dispenser.appspot.com/_ah/api/agenda/v1/dispenser/' + codigoDispenser + '/' + $stateParams.codigoMedicamento).
      success(function(data, status, headers, config) {
        $scope.drug = data;
      }).
      error(function(data, status, headers, config) {
        alert("Erro ao retornar a agenda do medicamento!");
    });
  };

  // An alert dialog
  $scope.showAlert = function(title, text, callback) {
    var alertPopup = $ionicPopup.alert({
      title: title,
      template: text
    });

    if(typeof(callback) === "function") {
      alertPopup.then(function(res) {
        callback();
      });
    }
    // alertPopup.then(function(res) {
    //   console.log('Thank you for not eating my delicious ice cream cone');
    // });
  };

  $scope.gravarDosagem = function(drug) {
    $http.post('https://drugs-dispenser.appspot.com/_ah/api/agenda/v1/alterardosagem/' + drug.id + '/' + drug.dosagem).
      success(function(data, status, headers, config) {
        console.log(data);
        if(status == 204) {
          $scope.showAlert("Tudo certo", "Dosagem do medicamento atualizada com sucesso!", function() {
            $scope.carregaDados();
            $ionicHistory.goBack();
          });
        } else {
          console.log(data);
          console.log(status);
          $scope.showAlert("Oops!", "Erro ao atualizar a dosagem do medicamento.");
        }
      }).
      error(function(data, status, headers, config) {
        console.log(data);
        console.log(status);
        $scope.showAlert("Oops!", "Erro ao gravar a dosagem do medicamento!");
    });
  };

  $scope.resetDatas = function(drug) {
    $http.post('https://drugs-dispenser.appspot.com/_ah/api/agenda/v1/resetdatas/' + drug.id).
      success(function(data, status, headers, config) {
        if(status == 200) {
          $scope.showAlert("Tudo certo", "Datas resetadas com sucesso!", function() {
            $scope.carregaDados();
            $ionicHistory.goBack();
          });
        } else {
          console.log(data);
          console.log(status);
          $scope.showAlert("Oops!", "Erro ao resetar as datas do medicamento.");
        }
      }).
      error(function(data, status, headers, config) {
        console.log(data);
        console.log(status);
        $scope.showAlert("Oops!", "Erro ao resetar as datas do medicamento.");
    });
  };

  $scope.carregaDados();
});