'use strict';

var validacoes = require('validacoes');

var id = (function () {
  var counters = {};
  var gerador = function (base) {
    if (!counters[base]) {
      counters[base] = 0;
    }
    return base + '-' + counters[base]++;
  };

  gerador.reset = function () {
    counters = {};
  };

  return gerador;
})();

var Cabecalho = function () {
  this.metadados = m.prop({});
  this.erro = m.prop(null);

  this.limparErro = function () {
    this.erro(null);
  };

  this.tentarNovamente = function (fn) {
    this.erro({
      tentarNovamente: fn
    });
  };
};

var Caso = function (parentId, config) {
  var data = (config || {});
  this.id = id((parentId ? parentId + '-' : '') + 'caso');
  this.padrao = data.padrao;
  this.descricao = m.prop(data.descricao || '');
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
  this.tipo = m.prop(data.tipo || '');
  this.descricao = m.prop(data.descricao || '');
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

var Custo = function (config) {
  var data = (config || {});
  this.id = id('custo');
  this.descricao = m.prop(data.descricao || '');
  this.moeda = m.prop(data.moeda || '');
  this.valor = m.prop(data.valor || '');
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
  this.titulo = m.prop(data.titulo || '');
  this.descricao = m.prop(data.descricao || '');
  this.documentos = m.prop(data.documentos || new Documentos());
  this.custos = m.prop(data.custos || new Custos());
  this.canaisDePrestacao = m.prop(data.canaisDePrestacao || new CanaisDePrestacao());
};

var Solicitante = function (config) {
  var data = (config || {});
  this.id = id('solicitante');
  this.tipo = m.prop(data.tipo || '');
  this.requisitos = m.prop(data.requisitos || '');
};

var TempoTotalEstimado = function (config) {
  var data = (config || {});
  this.id = id('tempo-total-estimado');
  this.tipo = m.prop(data.tipo || '');
  this.entreMinimo = m.prop(data.entreMinimo || '');
  this.ateMaximo = m.prop(data.ateMaximo || '');
  this.ateTipoMaximo = m.prop(data.ateTipoMaximo || '');
  this.entreMaximo = m.prop(data.entreMaximo || '');
  this.entreTipoMaximo = m.prop(data.entreTipoMaximo || '');
  this.descricao = m.prop(data.descricao || '');
};

var Servico = function (config) {
  var data = (config || {});
  this.id = id('servico');
  this.nome = m.prop(data.nome || '');
  this.sigla = m.prop(data.sigla || '');
  this.nomesPopulares = m.prop(data.nomesPopulares || []);
  this.descricao = m.prop(data.descricao || '');
  this.gratuidade = m.prop(data.gratuidade);
  this.solicitantes = m.prop(data.solicitantes || []);
  this.tempoTotalEstimado = m.prop(data.tempoTotalEstimado || new TempoTotalEstimado());
  this.etapas = m.prop(data.etapas || []);
  this.orgao = m.prop(data.orgao || '');
  this.segmentosDaSociedade = m.prop(data.segmentosDaSociedade || []);
  this.eventosDaLinhaDaVida = m.prop(data.eventosDaLinhaDaVida || []);
  this.areasDeInteresse = m.prop(data.areasDeInteresse || []);
  this.palavrasChave = m.prop(data.palavrasChave || []);
  this.legislacoes = m.prop(data.legislacoes || []);

  this.validador = new m.validator(validacoes.Servico);
  this.validar = function () {
    this.validador = new m.validator(validacoes.Servico);
    this.validador.validate(this);
  };
};

module.exports = {
  id: id,
  Cabecalho: Cabecalho,
  Caso: Caso,
  CanaisDePrestacao: CanaisDePrestacao,
  CanalDePrestacao: CanalDePrestacao,
  Documentos: Documentos,
  Custo: Custo,
  Custos: Custos,
  Etapa: Etapa,
  Solicitante: Solicitante,
  Servico: Servico,
  TempoTotalEstimado: TempoTotalEstimado
};
