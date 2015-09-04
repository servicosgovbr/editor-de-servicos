'use strict';

var primeiroErroPara = _.curry(function (validacoes, valor) {
  return _.reduce(validacoes, function (erro, validador) {
    return erro || validador(valor);
  }, undefined);
});

var validador = function (property, validacoes) {
  var erro = m.prop();

  var valida = function (valor) {
    return erro(primeiroErroPara(validacoes, valor || property()));
  };

  var wrapper = function () {
    var novoValor = property.apply(property, arguments);
    if (!_.isEmpty(arguments)) {
      valida(novoValor);
    }

    return novoValor;
  };

  wrapper.erro = erro;
  wrapper.valida = valida;
  return wrapper;
};

var prop = function () {
  var valorInicial = _.head(arguments);
  var validacoes = _.tail(arguments);

  return validador(m.prop(valorInicial), validacoes);
};

var valida = function (obj) {
  switch (typeof obj) {
  case 'object':
    return _.every(_.map(obj, valida));

  case 'function':
    if (_.isFunction(obj.valida)) {
      var erro = obj.valida();
      return (_.isUndefined(erro) || _.compact(erro).length === 0) && valida(obj());
    }

    return valida(obj());
  }

  return true;
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
    return !_.isEmpty(_.compact(erros)) ? erros : undefined;
  };
};

var se = function () {
  var propTeste = arguments[0];
  var valorTeste = arguments[1];
  var validacoes = _.drop(arguments, 2);

  return function (valor) {
    if (propTeste.apply(propTeste) === valorTeste) {
      return primeiroErroPara(validacoes, valor);
    }
  };
};

module.exports = {
  valida: valida,
  prop: prop,
  cada: cada,
  se: se,
  obrigatorio: obrigatorio,
  maximo: maximo,
  minimo: minimo,
  numerico: numerico
};
