'use strict';

var slugify = require('slugify');
var limparModelo = require('limpar-modelo');
var extrairMetadados = require('utils/extrair-metadados');
var importarXml = require('xml/importar');
var exportarXml = require('xml/exportar');

function postarServico(nome, xml, metadados) {
  var id = slugify(nome);

  return m.request({

    method: 'POST',
    url: '/editar/api/pagina/servico/' + id,
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

  var promise;
  if (servico.nome()) {
    promise = postarServico(servico.nome(), xml, metadados)
      .then(importarXml, onAjaxError);
  } else {
    promise = m.deferred().resolve(servico).promise;
  }

  return promise.then(servicoProp);
};
