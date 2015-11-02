'use strict';

var slugify = require('slugify');
var extrairMetadados = require('utils/extrair-metadados');
var importarXml = require('xml/importar');
var exportarXml = require('xml/exportar');
var validacoes = require('utils/validacoes');
var promiseUtil = require('utils/promise');
var limparModelo = require('limpar-modelo');

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

function validaNome(servico) {
  if (validacoes.valida(servico.nome)) {
    return servico;
  }
  throw 'Erro na validação do nome do serviço';
}

module.exports = function (servico, metadados) {
  var onAjaxError = require('utils/erro-ajax');

  return promiseUtil.resolved(servico)
    .then(limparModelo)
    .then(validaNome)
    .then(exportarXml)
    .then(function (xml) {
      return postarServico(servico.nome(), xml, metadados);
    })
    .then(importarXml, onAjaxError);
};
