(function() {
    'use strict';

    angular
        .module('basketballOauth2Jhipster3App')
        .controller('EquipoDetailController', EquipoDetailController);

    EquipoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Equipo', 'Player'];

    function EquipoDetailController($scope, $rootScope, $stateParams, entity, Equipo, Player) {
        var vm = this;
        vm.equipo = entity;
        vm.load = function (id) {
            Equipo.get({id: id}, function(result) {
                vm.equipo = result;
            });
        };
        var unsubscribe = $rootScope.$on('basketballOauth2Jhipster3App:equipoUpdate', function(event, result) {
            vm.equipo = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
