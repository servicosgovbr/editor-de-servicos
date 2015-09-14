'use strict';

var id = require('utils/id');
var v = require('utils/validacoes');
var idUnico = require('utils/id-unico');

var validaIdServico = function (unsafeId) {
  return idUnico(unsafeId) ? undefined : 'erro-nome-servico-existente';
};

var Caso = function (parentId, config) {
  var data = (config || {});
  this.id = id((parentId ? parentId + '-' : '') + 'caso');
  this.padrao = data.padrao;
  this.descricao = v.prop(data.descricao || '', v.textoCurto);
  this.campos = m.prop(data.campos || []);
};

var CanaisDePrestacao = function (config) {
  var data = (config || {});
  this.id = id('canais-de-prestacao');
  this.casoPadrao = m.prop(data.casoPadrao || new Caso(this.id, {
    padrao: true,
    campos: []
  }));
  this.outrosCasos = m.prop(data.outrosCasos || []);
};

var CanalDePrestacao = function (config) {
  var data = (config || {});
  this.id = id('canal-de-prestacao');
  this.tipo = v.prop(data.tipo || '', v.obrigatorio);
  this.descricao = v.prop(data.descricao || '', v.textoLongo);
};

var Documentos = function (config) {
  var data = (config || {});
  this.id = id('documentos');
  this.casoPadrao = m.prop(data.casoPadrao || new Caso(this.id, {
    padrao: true,
    campos: []
  }));
  this.outrosCasos = m.prop(data.outrosCasos || []);
};

var Documento = function (config) {
  var data = (config || {});
  this.id = id('documento');
  this.descricao = v.prop(data.descricao || '', v.textoCurto);
};

var Custo = function (config) {
  var data = (config || {});
  this.id = id('custo');
  this.descricao = v.prop(data.descricao || '', v.textoCurto);
  this.moeda = m.prop(data.moeda || '');
  this.valor = v.prop(data.valor || '', v.numerico);
};

var Custos = function (config) {
  var data = (config || {});
  this.id = id('custos');
  this.casoPadrao = m.prop(data.casoPadrao || new Caso(this.id, {
    padrao: true,
    campos: []
  }));
  this.outrosCasos = m.prop(data.outrosCasos || []);
};

var Etapa = function (config) {
  var data = (config || {});
  this.id = id('etapa');
  this.titulo = v.prop(data.titulo || '', v.textoCurto);
  this.descricao = v.prop(data.descricao || '', v.textoLongo);
  this.documentos = m.prop(data.documentos || new Documentos());
  this.custos = m.prop(data.custos || new Custos());
  this.canaisDePrestacao = m.prop(data.canaisDePrestacao || new CanaisDePrestacao());
};

var Solicitante = function (config) {
  var data = (config || {});
  this.id = id('solicitante');
  this.tipo = v.prop(data.tipo || '', v.obrigatorio, v.textoCurto);
  this.requisitos = v.prop(data.requisitos || '', v.textoLongo);
};

var TempoTotalEstimado = function (config) {
  var data = (config || {});
  this.id = id('tempo-total-estimado');
  this.tipo = m.prop(data.tipo || '');
  this.descricao = v.prop(data.descricao || '', v.textoLongo);
  this.ateMaximo = v.prop(data.ateMaximo || '', v.se(this.tipo, 'ate', v.obrigatorio));
  this.ateTipoMaximo = v.prop(data.ateTipoMaximo || '', v.se(this.tipo, 'ate', v.obrigatorio));
  this.entreMinimo = v.prop(data.entreMinimo || '', v.se(this.tipo, 'entre', v.obrigatorio));
  this.entreMaximo = v.prop(data.entreMaximo || '', v.se(this.tipo, 'entre', v.obrigatorio));
  this.entreTipoMaximo = v.prop(data.entreTipoMaximo || '', v.se(this.tipo, 'entre', v.obrigatorio));
};

var Servico = function (config) {
  var data = (config || {});
  this.id = id('servico');

  var validaIdJaExistente = _.trim(data.nome) ? _.noop : validaIdServico;

  this.nome = v.prop(data.nome || '', v.obrigatorio, v.textoCurto, validaIdJaExistente);
  this.sigla = v.prop(data.sigla || '', v.maximo(15));
  this.nomesPopulares = v.prop(data.nomesPopulares || [], v.cada(v.textoCurto));
  this.descricao = v.prop(data.descricao || '', v.obrigatorio, v.textoLongo);
  this.gratuidade = m.prop(data.gratuidade);
  this.solicitantes = v.prop(data.solicitantes || [], v.minimo(1));
  this.tempoTotalEstimado = m.prop(data.tempoTotalEstimado || new TempoTotalEstimado());
  this.etapas = v.prop(data.etapas || [], v.minimo(1));
  this.orgao = m.prop(data.orgao || '');
  this.segmentosDaSociedade = v.prop(data.segmentosDaSociedade || [], v.minimo(1));
  this.areasDeInteresse = v.prop(data.areasDeInteresse || [], v.minimo(1));
  this.palavrasChave = v.prop(data.palavrasChave || [], v.cada(v.textoCurto), v.minimo(3));
  this.legislacoes = v.prop(data.legislacoes || [], v.minimo(1));
};

module.exports = {
  id: id,
  Caso: Caso,
  CanaisDePrestacao: CanaisDePrestacao,
  CanalDePrestacao: CanalDePrestacao,
  Documentos: Documentos,
  Custo: Custo,
  Documento: Documento,
  Custos: Custos,
  Etapa: Etapa,
  Solicitante: Solicitante,
  Servico: Servico,
  TempoTotalEstimado: TempoTotalEstimado
};
