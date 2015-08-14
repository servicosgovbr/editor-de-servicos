'use strict';

var trimProp = function (prop) {
  prop(_.trim(prop()));
};

var limparSolicitante = function (solicitante) {
  [solicitante.tipo, solicitante.requisitos].forEach(trimProp);
  return (solicitante.tipo() || solicitante.requisitos()) ? solicitante : null;
};

var limparCaso = function (caso, fnLimpar) {
  caso.descricao(_.trim(caso.descricao()));
  caso.campos(_.compact(caso.campos().map(fnLimpar)));
  return (caso.descricao() || !_.isEmpty(caso.campos())) ? caso : null;
};

var limparCasos = function (obj, fnLimpar) {
  obj.casoPadrao(limparCaso(obj.casoPadrao(), fnLimpar));
  obj.outrosCasos(_.compact(obj.outrosCasos().map(function (caso) {
    return limparCaso(caso, fnLimpar);
  })));
  return obj.casoPadrao() || !_.isEmpty(obj.outrosCasos()) ? obj : null;
};

var limparDocumentos = function (documentos) {
  return limparCasos(documentos, _.trim);
};

var limparCusto = function (custo) {
  custo.descricao(_.trim(custo.descricao()));
  custo.moeda(_.trim(custo.moeda()).toUpperCase());
  custo.valor(_.trim(custo.valor()));

  return (custo.descricao() || (custo.moeda() && custo.moeda() !== 'R$') || custo.valor()) ? custo : null;
};

var limparCustos = function (custos) {
  return limparCasos(custos, limparCusto);
};

var limparCanalDePrestacao = function (canal) {
  canal.tipo(_.trim(canal.tipo()));
  canal.descricao(_.trim(canal.descricao()));

  return (canal.tipo() || canal.descricao()) ? canal : null;
};

var limparCanaisDePrestacao = function (canaisDePrestacao) {
  return limparCasos(canaisDePrestacao, limparCanalDePrestacao);
};

var limparEtapa = function (etapa) {
  [etapa.titulo, etapa.descricao].forEach(trimProp);

  etapa.documentos(limparDocumentos(etapa.documentos()));
  etapa.custos(limparCustos(etapa.custos()));
  etapa.canaisDePrestacao(limparCanaisDePrestacao(etapa.canaisDePrestacao()));

  return (etapa.titulo() || etapa.descricao() || !_.isEmpty(etapa.documentos()) || !_.isEmpty(etapa.custos()) || !_.isEmpty(etapa.canaisDePrestacao())) ? etapa : null;
};

module.exports = function (servico) {
  [servico.nome, servico.sigla, servico.descricao].forEach(trimProp);

  servico.nomesPopulares(_.compact(servico.nomesPopulares().map(_.trim)));
  servico.solicitantes(_.compact(servico.solicitantes().map(limparSolicitante)));
  servico.etapas(_.compact(servico.etapas().map(limparEtapa)));


  var tte = servico.tempoTotalEstimado();
  [tte.tipo, tte.entreMinimo, tte.entreMaximo, tte.entreTipoMaximo, tte.ateMaximo, tte.ateTipoMaximo].forEach(trimProp);

  servico.palavrasChave(_.compact(servico.palavrasChave().map(_.trim)));
  servico.legislacoes(_.compact(servico.legislacoes().map(_.trim)));

  return servico;
};
