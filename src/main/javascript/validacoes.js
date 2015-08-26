'use strict';

var maximo = _.curry(function (len, def, v) {
  v = v || def;
  if (v.length > len) {
    return 'max-' + len;
  }
});

var minimo = _.curry(function (len, def, v) {
  v = v || def;
  if (v.length < len) {
    return 'min-' + len;
  }
});

var obrigatorio = function (v) {
  if (!v) {
    return 'campo-obrigatorio';
  }
};

var Servico = {
  nome: function (nome) {
    return obrigatorio(nome) || maximo(150, '', nome);
  },

  sigla: maximo(15, ''),

  nomesPopulares: function (nomes) {
    nomes = nomes || [];
    return _.compact(_.map(nomes, function (v, i) {
      var e = maximo(150, '', v);
      if (e) {
        return {
          i: i,
          msg: e
        };
      }
    }));
  },

  descricao: function (descricao) {
    return obrigatorio(descricao) || maximo(500, '', descricao);
  },

  palavrasChave: function (palavrasChave) {
    palavrasChave = palavrasChave || [];

    var err = {};
    if (palavrasChave.length < 3) {
      err.msg = minimo(3, [], palavrasChave);
    }

    err.campos = _.compact(_.map(palavrasChave, function (v, i) {
      var e = maximo(50, '', v);
      if (e) {
        return {
          i: i,
          msg: e
        };
      }
    }));
    return err;
  }
};


var TempoTotalEstimado = {
  descricao: maximo(500, ''),
  ateMaximo: obrigatorio,
  ateTipoMaximo: obrigatorio,
  entreMinimo: obrigatorio,
  entreMaximo: obrigatorio,
  entreTipoMaximo: obrigatorio
};

module.exports = {
  Servico: Servico,
  TempoTotalEstimado: TempoTotalEstimado
};
