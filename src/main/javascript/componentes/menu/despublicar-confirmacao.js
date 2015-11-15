'use strict';

module.exports = function (despublicarFn) {
  return function () {
    alertify.labels.cancel = 'Cancelar';
    alertify.labels.ok = 'Despublicar';
    alertify.confirm('Tem certeza que deseja despublicar o serviço?', despublicarFn);
  };
};
