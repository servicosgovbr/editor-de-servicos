'use strict';

var maximo = _.curry(function (len, def, v) {
  v = v || def;
  if (v.length > len) {
    return 'erro-max-' + len;
  }
});

var minimo = _.curry(function (len, def, v) {
  v = v || def;
  if (v.length < len) {
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

var Servico = {
  nome: function (nome) {
    return obrigatorio(nome) || maximo(150, '', nome);
  },

  sigla: maximo(15, ''),

  nomesPopulares: function (nomes) {
    nomes = nomes || [];
    return _.map(nomes, function (v, i) {
      return maximo(150, '', v);
    });
  },

  descricao: function (descricao) {
    return obrigatorio(descricao) || maximo(500, '', descricao);
  },

  solicitantes: function (solicitantes) {
    solicitantes = solicitantes || [];
    if (solicitantes.length < 1) {
      return 'erro-min-1';
    }
  },

  etapas: function (etapas) {
    etapas = etapas || [];

    if (etapas.length < 1) {
      return 'erro-min-1';
    }
  },

  palavrasChave: function (palavrasChave) {
    palavrasChave = palavrasChave || [];

    var err = {};
    if (palavrasChave.length < 3) {
      err.msg = minimo(3, [], palavrasChave);
    }

    err.campos = _.map(palavrasChave, function (v, i) {
      return maximo(50, '', v);
    });
    return err;
  },

};

var TempoTotalEstimado = {
  descricao: maximo(500, ''),
  ateMaximo: obrigatorio,
  ateTipoMaximo: obrigatorio,
  entreMinimo: obrigatorio,
  entreMaximo: obrigatorio,
  entreTipoMaximo: obrigatorio
};

var Documento = {
  campo: maximo(150, '')
};

function validaCaso(caso, validadorCampo) {
  return {
    descricao: maximo(150, '', caso.descricao()),
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

var Etapa = {
  descricao: maximo(500, ''),
  titulo: maximo(100, ''),

  documentos: function (documentos) {
    return validaCampoDeCasos(documentos, Documento.campo);
  }
};

var Custo = {
  descricao: maximo(150, ''),
  valor: numerico
};

var Solicitante = {
  tipo: function (solicitante) {
    return obrigatorio(solicitante) || maximo(500, '', solicitante);
  },
  requisitos: maximo(500, '')
};

module.exports = {
  Servico: Servico,
  TempoTotalEstimado: TempoTotalEstimado,
  Solicitante: Solicitante,
  Etapa: Etapa,
  Documento: Documento,
  Custo: Custo,
};
