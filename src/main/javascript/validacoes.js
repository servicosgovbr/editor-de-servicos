'use strict';

var Servico = {
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

  nomesPopulares: function (nomes) {
    nomes = nomes || [];
    return _.compact(_.map(nomes, function (v, i) {
      if (v.length > 150) {
        return {
          i: i,
          msg: 'nome-pop-max-150'
        };
      }
    }));
  },

  descricao: function (descricao) {
    if (!descricao) {
      return 'descricao-obrigatoria';
    }
    if (descricao.length > 500) {
      return 'descricao-max-500';
    }
  },

  palavrasChave: function (palavrasChave) {
    palavrasChave = palavrasChave || [];

    var err = {};
    if (palavrasChave.length < 3) {
      err.msg = 'min-3-palavras-chave';
    }

    err.campos = _.compact(_.map(palavrasChave, function (v, i) {
      if (v.length > 50) {
        return {
          i: i,
          msg: 'palavra-chave-max-50'
        };
      }
    }));

    return err;
  }
};

var TempoTotalEstimado = {
  ateMaximo: function (maximo) {
    if (!maximo) {
      return 'tempo-obrigatorio';
    }

  }
};

module.exports = {
  Servico: Servico,
  TempoTotalEstimado: TempoTotalEstimado
};
