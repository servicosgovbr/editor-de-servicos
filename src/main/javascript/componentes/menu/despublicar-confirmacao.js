'use strict';

var mensagem = 'Ao despublicar o serviço, a versão que está no Portal de Serviços será perdida. Apenas a versão em edição estará disponível para ser publicada novamente. Deseja despublicar o serviço?';

module.exports = function (despublicarFn) {
  return function () {
    alertify.labels.cancel = 'Cancelar';
    alertify.labels.ok = 'Despublicar';
    alertify.confirm(mensagem, function (ok) {
      if (ok) {
        despublicarFn();
      }
    });
  };
};
