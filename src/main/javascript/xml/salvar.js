'use strict';

var slugify = require('slugify');
var limparModelo = require('limpar-modelo');
var extrairMetadados = require('utils/extrair-metadados');
var importarXml = require('xml/importar-v3');
var exportarXml = require('xml/exportar');

function postarServico(nome, xml, metadados) {
  return m.request({

    method: 'POST',
    url: '/editar/v3/servico/' + nome,
    data: xml,
    background: true,

    config: function (xhr) {
      xhr.setRequestHeader('Accepts', 'application/xml');
      xhr.setRequestHeader('Content-Type', 'application/xml');
    },

    serialize: function (svc) {
      return new XMLSerializer().serializeToString(svc);
    },

    extract: extrairMetadados(metadados),

    deserialize: function (str) {
      return new DOMParser().parseFromString(str, 'application/xml');
    },

  });
}

module.exports = function (servicoProp, metadados) {
  var servico = limparModelo(servicoProp());
  var xml = exportarXml(servico);
  var onAjaxError = require('utils/erro-ajax');

  return postarServico(slugify(servico.nome()), xml, metadados)
    .then(importarXml, onAjaxError)
    .then(servicoProp);
};
