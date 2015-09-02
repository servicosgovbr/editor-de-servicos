'use strict';

var primeiroErroPara = _.curry(function (validacoes, valor) {
  return _.reduce(validacoes, function (erro, validador) {
    return erro || validador(valor);
  }, undefined);
});

var validador = function (property, validacoes) {
  var erro = m.prop();
  var wrapper = function () {
    var novoValor = property.apply(property, arguments);
    erro(primeiroErroPara(validacoes, novoValor));
    return novoValor;
  };

  wrapper.erro = erro;
  return wrapper;
};

var prop = function () {
  var valorInicial = _.head(arguments);
  var validacoes = _.tail(arguments);

  return validador(m.prop(valorInicial), validacoes);
};

var maximo = _.curry(function (len, v) {
  if (v && v.length > len) {
    return 'erro-max-' + len;
  }
});

var minimo = _.curry(function (len, v) {
  if (v && v.length < len) {
    return 'erro-min-' + len;
  }
});

var numerico = function (v) {
  if (v && !v.match(/^\d+(\.\d{3})*(,\d+)?$/)) {
    return 'erro-campo-numerico';
  }
};

var obrigatorio = function (v) {
  if (!v) {
    return 'erro-campo-obrigatorio';
  }
};

var cada = function () {
  var validacoes = arguments;

  return function (valores) {
    var erros = _.map(valores, primeiroErroPara(validacoes));
    return !_.isEmpty(erros) ? erros : undefined;
  };
};

function validaCaso(caso, validadorCampo) {
  return {
    descricao: maximo(150, caso.descricao()),
    campos: _.map(caso.campos(), validadorCampo)
  };
}

function validaCampoDeCasos(campo, validadorCampo) {
  return {
    casoPadrao: validaCaso(campo.casoPadrao(), validadorCampo),
    outrosCasos: _.map(campo.outrosCasos(), function (c) {
      return validaCaso(c, validadorCampo);
    })
  };
}

var CanalDePrestacao = {
  descricao: maximo(500),
  tipo: obrigatorio,

  campo: function (canal) {
    return {
      descricao: CanalDePrestacao.descricao(canal.descricao()),
      tipo: CanalDePrestacao.tipo(canal.tipo())
    };
  }
};

var Etapa = {
  canaisDePrestacao: function (canais) {
    return validaCampoDeCasos(canais, CanalDePrestacao.campo);
  }
};

module.exports = {
  Etapa: Etapa,
  CanalDePrestacao: CanalDePrestacao,

  prop: prop,
  cada: cada,
  obrigatorio: obrigatorio,
  maximo: maximo,
  minimo: minimo,
  numerico: numerico
};
