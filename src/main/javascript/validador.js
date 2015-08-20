'use strict';

module.exports = {
  nome: function (nome) {
    if (!nome) {
      return 'nome-obrigatorio';
    }
    if (nome.length > 150) {
      return 'nome-max-150';
    }
    return;
  },
  sigla: function (sigla) {
    sigla = sigla || '';
    if (sigla.length > 15) {
      return 'sigla-max-15';
    }
    return;
  }
};
