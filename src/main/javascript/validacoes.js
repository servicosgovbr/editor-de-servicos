'use strict';

function nomePopular(v, i) {
  if (v.length > 150) {
    return {
      i: i,
      err: 'nome-pop-max-150'
    };
  }
}

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
  },

  nomesPopulares: function(nomes) {
    nomes = nomes || [];
    var result = _.compact(_.map(nomes, nomePopular));
    return result.length === 0
      ? undefined
      : result;
  }
};
