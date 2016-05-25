(function() {
    'use strict';

    angular
        .module('basketballOauth2Jhipster3App')
        .controller('EquipoDialogController', EquipoDialogController);

    EquipoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Equipo', 'Player'];

    function EquipoDialogController ($scope, $stateParams, $uibModalInstance, entity, Equipo, Player) {
        var vm = this;
        vm.equipo = entity;
        vm.players = Player.query();
        vm.load = function(id) {
            Equipo.get({id : id}, function(result) {
                vm.equipo = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('basketballOauth2Jhipster3App:equipoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.equipo.id !== null) {
                Equipo.update(vm.equipo, onSaveSuccess, onSaveError);
            } else {
                Equipo.save(vm.equipo, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fechaCreacion = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
