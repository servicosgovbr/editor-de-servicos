'use strict';

var slugify = require('slugify');
var modelos = require('modelos');

var string = function (s) {
  return jQuery(s).text().trim();
};

var documentos = function (x) {
  x = jQuery(x);

  return new modelos.Documentos({
    casoPadrao: new modelos.Caso(null, {
      campos: x.find('> documento').get().map(string)
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
    custos: null,
    canaisDePrestacao: null,
  });
};

var servico = function (x) {
  return new modelos.Servico({
    nome: string(x.find('> nome')),
    sigla: '',
    nomesPopulares: string(x.find('> nomes-populares')).split(',').map(function (s) {
      return s.trim();
    }),
    descricao: string(x.find('> descricao')),
    solicitantes: x.find('> solicitantes > solicitante').get().map(function (s) {
      return new modelos.Solicitante({
        descricao: string(s)
      });
    }),
    tempoTotalEstimado: new modelos.TempoTotalEstimado({
      tipo: x.find('> tempo-total-estimado').attr('tipo').trim(),
      entreMinimo: x.find('> tempo-total-estimado').attr('tipo').trim() === 'entre' ? string(x.find('> tempo-total-estimado minimo')) : '',
      entreMaximo: x.find('> tempo-total-estimado').attr('tipo').trim() === 'entre' ? string(x.find('> tempo-total-estimado maximo')) : '',
      entreTipoMaximo: x.find('> tempo-total-estimado').attr('tipo').trim() === 'entre' ? slugify(x.find('> tempo-total-estimado maximo').attr('tipo')) : '',
      ateMaximo: x.find('> tempo-total-estimado').attr('tipo').trim() === 'até' ? string(x.find('> tempo-total-estimado maximo')) : '',
      ateTipoMaximo: x.find('> tempo-total-estimado').attr('tipo').trim() === 'até' ? slugify(x.find('> tempo-total-estimado maximo').attr('tipo')) : '',
      descricao: string(x.find('> tempo-total-estimado excecoes'))
    }),
    etapas: x.find('> etapas > etapa').get().map(etapa),
    orgao: string(x.find('orgao id')),
    segmentosDaSociedade: x.find('segmentos-da-sociedade > segmento-da-sociedade > nome').get().map(string),
    eventosDaLinhaDaVida: x.find('eventos-da-linha-da-vida > evento-da-linha-da-vida > nome').get().map(string),
    areasDeInteresse: x.find('areas-de-interesse > area-de-interesse > area').get().map(string),
    palavrasChave: string(x.find('> palavras-chave')).split(',').map(function (s) {
      return s.trim();
    }),
    legislacoes: x.find('> legislacao-relacionada > link').get().map(function (s) {
      return jQuery(s).attr('href').trim();
    }),
  });
};

module.exports = function (dom) {
  return servico(jQuery('servico', dom));
};
