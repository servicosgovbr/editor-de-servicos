'use strict';

var mensagem = 'Ao despublicar a página, a versão que está no Portal de Serviços será perdida. Apenas a versão em edição estará disponível para ser publicada novamente. Deseja despublicar a página?';

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
