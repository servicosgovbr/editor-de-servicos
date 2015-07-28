'use strict';

var slugify = require('slugify');
var modelos = require('modelos');

var string = function (s) {
  return jQuery(s).text().trim();
};

var documentos = function (c) {
  var x = jQuery(c);
  return new modelos.Documentos({
    casoPadrao: new modelos.Caso(null, {
      campos: x.find('> documento').get().map(string)
    }),
    outrosCasos: []
  });
};

var custo = function (c) {
  var x = jQuery(c);
  return new modelos.Custo({
    descricao: string(x.find('descricao')),
    valor: string(x.find('valor')),
  });
};

var custos = function (c) {
  var x = jQuery(c);
  return new modelos.Custos({
    casoPadrao: new modelos.Caso(null, {
      campos: x.find('> custo').get().map(custo)
    }),
    outrosCasos: []
  });
};

var canalDePrestacao = function (c) {
  var x = jQuery(c);
  return new modelos.CanalDePrestacao({
    tipo: x.attr('tipo'),
    descricao: string(x.find('> referencia'))
  });
};

var canaisDePrestacao = function (c) {
  var x = jQuery(c);
  return new modelos.CanaisDePrestacao({
    casoPadrao: new modelos.Caso(null, {
      campos: x.find('> canal-de-prestacao').get().map(canalDePrestacao)
    }),
    outrosCasos: []
  });
};

var etapa = function (x) {
  x = jQuery(x);

  return new modelos.Etapa({
    titulo: string(x.find('> titulo')),
    descricao: string(x.find('> descricao')),
    documentos: documentos(x.find('> documentos').first()),
    custos: custos(x.find('> custos').first()),
    canaisDePrestacao: canaisDePrestacao(x.find('> canais-de-prestacao').first()),
  });
};

var nomesPopulares = function (x) {
  return string(x.find('> nomes-populares')).split(',').map(function (s) {
    return s.trim();
  });
};

var solicitantes = function (x) {
  return x.find('> solicitantes > solicitante').get().map(function (s) {
    return new modelos.Solicitante({
      tipo: string(s)
    });
  });
};

var tempoTotalEstimado = function (x) {
  return new modelos.TempoTotalEstimado({
    tipo: x.find('> tempo-total-estimado').attr('tipo'),
    entreMinimo: x.find('> tempo-total-estimado').attr('tipo') === 'entre' ? string(x.find('> tempo-total-estimado minimo')) : '',
    entreMaximo: x.find('> tempo-total-estimado').attr('tipo') === 'entre' ? string(x.find('> tempo-total-estimado maximo')) : '',
    entreTipoMaximo: x.find('> tempo-total-estimado').attr('tipo') === 'entre' ? slugify(x.find('> tempo-total-estimado maximo').attr('tipo')) : '',
    ateMaximo: x.find('> tempo-total-estimado').attr('tipo') === 'até' ? string(x.find('> tempo-total-estimado maximo')) : '',
    ateTipoMaximo: x.find('> tempo-total-estimado').attr('tipo') === 'até' ? slugify(x.find('> tempo-total-estimado maximo').attr('tipo')) : '',
    descricao: string(x.find('> tempo-total-estimado excecoes'))
  });
};

var palavrasChave = function (x) {
  return string(x.find('> palavras-chave')).split(',').map(function (s) {
    return s.trim();
  });
};

var legislacoes = function (x) {
  return x.find('> legislacao-relacionada > link').get().map(function (s) {
    return jQuery(s).attr('href').trim();
  });
};

var servico = function (x) {
  return new modelos.Servico({
    nome: string(x.find('> nome')),
    sigla: '',
    nomesPopulares: nomesPopulares(x),
    descricao: string(x.find('> descricao')),
    solicitantes: solicitantes(x),
    tempoTotalEstimado: tempoTotalEstimado(x),
    etapas: x.find('> etapas > etapa').get().map(etapa),
    orgao: string(x.find('orgao id')),
    segmentosDaSociedade: x.find('segmentos-da-sociedade > segmento-da-sociedade > nome').get().map(string),
    eventosDaLinhaDaVida: x.find('eventos-da-linha-da-vida > evento-da-linha-da-vida > nome').get().map(string),
    areasDeInteresse: x.find('areas-de-interesse > area-de-interesse > area').get().map(string),
    palavrasChave: palavrasChave(x),
    legislacoes: legislacoes(x),
  });
};

module.exports = function (dom) {
  return servico(jQuery('servico', dom));
};
