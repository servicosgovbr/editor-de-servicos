'use strict';

var modelos = require('modelos');

var isBlank = _.compose(_.isEmpty, _.trim);
var dontContains = _.negate(_.contains);

var allBlank = function () {
  return dontContains(_.map(arguments, isBlank), false);
};

var allEmpty = function () {
  return dontContains(_.map(arguments, _.isEmpty), false);
};

var limparSolicitante = function (solicitante) {
  if (allBlank(solicitante.tipo(), solicitante.requisitos())) {
    return null;
  }
  return new modelos.Solicitante({
    tipo: _.trim(solicitante.tipo()),
    requisitos: _.trim(solicitante.requisitos())
  });
};

var limparCaso = function (caso, fnLimpar) {
  var padrao = caso.padrao;
  var descricao = _.trim(caso.descricao());
  var campos = _.compact(caso.campos().map(fnLimpar));

  if (allEmpty(descricao, campos)) {
    return null;
  }
  return new modelos.Caso(null, {
    padrao: padrao,
    descricao: descricao,
    campos: campos
  });
};

var limparCasos = function (obj, fnLimpar) {
  var casoPadrao = limparCaso(obj.casoPadrao(), fnLimpar);
  var outrosCasos = _.compact(obj.outrosCasos().map(function (caso) {
    return limparCaso(caso, fnLimpar);
  }));

  if (!casoPadrao && _.isEmpty(outrosCasos)) {
    return null;
  }
  return {
    casoPadrao: casoPadrao,
    outrosCasos: outrosCasos
  };
};

var limparDocumentos = function (documentos) {
  var config = limparCasos(documentos, _.trim);
  return config ? new modelos.Documentos(config) : null;
};

var limparCusto = function (custo) {
  var descricao = _.trim(custo.descricao());
  var moeda = _.trim(custo.moeda()).toUpperCase();
  var valor = _.trim(custo.valor());

  if (allBlank(descricao, valor) && (isBlank(moeda) || moeda === 'R$')) {
    return null;
  }
  return new modelos.Custo({
    descricao: descricao,
    moeda: moeda,
    valor: valor
  });
};

var limparCustos = function (custos) {
  var config = limparCasos(custos, limparCusto);
  return config ? new modelos.Custos(config) : null;
};

var limparCanalDePrestacao = function (canal) {
  var tipo = _.trim(canal.tipo());
  var descricao = _.trim(canal.descricao());

  if (allBlank(tipo, descricao)) {
    return null;
  }
  return new modelos.CanalDePrestacao({
    tipo: tipo,
    descricao: descricao
  });
};

var limparCanaisDePrestacao = function (canaisDePrestacao) {
  var config = limparCasos(canaisDePrestacao, limparCanalDePrestacao);
  return config ? new modelos.CanaisDePrestacao(config) : null;
};

var limparEtapa = function (etapa) {
  var titulo = _.trim(etapa.titulo());
  var descricao = _.trim(etapa.descricao());
  var docs = limparDocumentos(etapa.documentos());
  var custos = limparCustos(etapa.custos());
  var canaisDePrestacao = limparCanaisDePrestacao(etapa.canaisDePrestacao());

  if (allBlank(titulo, descricao, docs, custos, canaisDePrestacao)) {
    return null;
  }
  return new modelos.Etapa({
    titulo: titulo,
    descricao: descricao,
    documentos: docs,
    custos: custos,
    canaisDePrestacao: canaisDePrestacao
  });
};

module.exports = function (servico) {
  var tte = servico.tempoTotalEstimado();

  return new modelos.Servico({
    nome: _.trim(servico.nome()),
    sigla: _.trim(servico.sigla()),
    nomesPopulares: _.compact(servico.nomesPopulares().map(_.trim)),
    descricao: _.trim(servico.descricao()),
    gratuidade: _.trim(servico.gratuidade()),
    solicitantes: _.compact(servico.solicitantes().map(limparSolicitante)),
    tempoTotalEstimado: new modelos.TempoTotalEstimado({
      tipo: _.trim(tte.tipo()),
      entreMinimo: _.trim(tte.entreMinimo()),
      ateMaximo: _.trim(tte.ateMaximo()),
      ateTipoMaximo: _.trim(tte.ateTipoMaximo()),
      entreMaximo: _.trim(tte.entreMaximo()),
      entreTipoMaximo: _.trim(tte.entreTipoMaximo()),
      descricao: _.trim(tte.descricao()),
    }),
    etapas: _.compact(servico.etapas().map(limparEtapa)),
    orgao: _.trim(servico.orgao()),
    segmentosDaSociedade: servico.segmentosDaSociedade(),
    eventosDaLinhaDaVida: servico.eventosDaLinhaDaVida(),
    areasDeInteresse: servico.areasDeInteresse(),
    palavrasChave: _.compact(servico.palavrasChave().map(_.trim)),
    legislacoes: _.compact(servico.legislacoes().map(_.trim)),
  });
};
