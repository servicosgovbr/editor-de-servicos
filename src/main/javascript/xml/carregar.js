'use strict';

var slugify = require('slugify');
var domParaServico = require('xml/servico-factory').domParaServico;
var erro = require('utils/erro-ajax');
var extrairMetadados = require('utils/extrair-metadados');

var config = function (id, metadados) {
  return {
    method: 'GET',

    url: '/editar/api/pagina/servico/' + slugify(id),

    config: function (xhr) {
      xhr.setRequestHeader('Accept', 'application/xml');
    },

    extract: extrairMetadados(metadados),

    deserialize: function (str) {
      return new DOMParser().parseFromString(str, 'application/xml');
    },
  };
};

var carregar = function (cabecalho, xml) {
  cabecalho.limparErro();
  return domParaServico(xml);
};

module.exports = function (id, cabecalho) {
  return m.request(config(id, cabecalho.metadados))
    .then(_.bind(carregar, this, cabecalho), erro);
};
