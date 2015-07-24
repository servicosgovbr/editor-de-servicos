'use strict';

exports.id = (function () {
  var counters = {};
  return function (base) {
    if (!counters[base]) {
      counters[base] = 0;
    }
    return base + '-' + counters[base]++;
  };
})();

exports.Caso = function (parentId, config) {
  var data = (config || {});
  this.id = exports.id((parentId ? parentId + '-' : '') + 'caso');
  this.descricao = m.prop(data.descricao || '');
  this.campos = m.prop(data.campos || []);
};

exports.CanaisDePrestacao = function (config) {
  var data = (config || {});
  this.id = exports.id('canais-de-prestacao');
  this.casoPadrao = m.prop(data.casoPadrao || new exports.Caso(this.id, {
    descricao: 'Para todos os casos',
    campos: []
  }));
  this.outrosCasos = m.prop(data.outrosCasos || []);
};

exports.CanalDePrestacao = function (config) {
  var data = (config || {});
  this.id = exports.id('canal-de-prestacao');
  this.tipo = m.prop(data.tipo || '');
  this.descricao = m.prop(data.tipo || '');
};

exports.Documentos = function (config) {
  var data = (config || {});
  this.id = exports.id('documentos');
  this.casoPadrao = m.prop(data.casoPadrao || new exports.Caso(this.id, {
    descricao: 'Para todos os casos',
    campos: []
  }));
  this.outrosCasos = m.prop(data.outrosCasos || []);
};

exports.Custo = function (config) {
  var data = (config || {});
  this.id = exports.id('custo');
  this.descricao = m.prop(data.descricao || '');
  this.moeda = m.prop(data.moeda || '');
  this.valor = m.prop(data.valor || '');
};

exports.Custos = function (config) {
  var data = (config || {});
  this.id = exports.id('custos');
  this.casoPadrao = m.prop(data.casoPadrao || new exports.Caso(this.id, {
    descricao: 'Para todos os casos',
    campos: []
  }));
  this.outrosCasos = m.prop(data.outrosCasos || []);
};

exports.Etapa = function (config) {
  var data = (config || {});
  this.id = exports.id('etapa');
  this.titulo = m.prop(data.titulo || '');
  this.descricao = m.prop(data.descricao || '');
  this.documentos = m.prop(data.documentos || new exports.Documentos());
  this.custos = m.prop(data.custos || new exports.Custos());
  this.canaisDePrestacao = m.prop(data.canaisDePrestacao || new exports.CanaisDePrestacao());
};

exports.Solicitante = function (config) {
  var data = (config || {});
  this.id = exports.id('solicitante');
  this.descricao = m.prop(data.descricao || '');
  this.requisitos = m.prop(data.requisitos || '');
};

exports.Servico = function (config) {
  var data = (config || {});
  this.id = exports.id('servico');
  this.nome = m.prop(data.nome || '');
  this.nomesPopulares = m.prop(data.nomesPopulares || []);
  this.descricao = m.prop(data.descricao || '');
  this.gratuidade = m.prop(data.gratuidade || false);
  this.solicitantes = m.prop(data.solicitantes || []);
  this.tempoTotalEstimado = m.prop(data.tempoTotalEstimado || new exports.TempoTotalEstimado());
  this.etapas = m.prop(data.etapas || []);
  this.orgao = m.prop(data.orgao || '');
  this.segmentosDaSociedade = m.prop(data.segmentosDaSociedade || []);
  this.eventosDaLinhaDaVida = m.prop(data.eventosDaLinhaDaVida || []);
  this.areasDeInteresse = m.prop(data.areasDeInteresse || []);
  this.palavrasChave = m.prop(data.palavrasChave || []);
  this.legislacoes = m.prop(data.legislacoes || []);
};

exports.TempoTotalEstimado = function (config) {
  var data = (config || {});
  this.id = exports.id('tempo-total-estimado');
  this.tipo = m.prop(data.tipo || '');
  this.entreMinimo = m.prop(data.entreMinimo || '');
  this.ateMaximo = m.prop(data.ateMaximo || '');
  this.ateTipoMaximo = m.prop(data.ateTipoMaximo || '');
  this.entreMaximo = m.prop(data.entreMaximo || '');
  this.entreTipoMaximo = m.prop(data.entreTipoMaximo || '');
  this.descricao = m.prop(data.descricao || '');
};
