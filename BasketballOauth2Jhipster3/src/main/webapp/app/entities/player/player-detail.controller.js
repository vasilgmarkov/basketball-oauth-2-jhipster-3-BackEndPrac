(function() {
    'use strict';

    angular
        .module('basketballOauth2Jhipster3App')
        .controller('PlayerDetailController', PlayerDetailController);

    PlayerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Player', 'Equipo'];

    function PlayerDetailController($scope, $rootScope, $stateParams, entity, Player, Equipo) {
        var vm = this;
        vm.player = entity;
        vm.load = function (id) {
            Player.get({id: id}, function(result) {
                vm.player = result;
            });
        };
        var unsubscribe = $rootScope.$on('basketballOauth2Jhipster3App:playerUpdate', function(event, result) {
            vm.player = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
