'use strict';

var slugify = require('slugify');
var importar = require('xml/importar-v3');
var erro = require('utils/erro-ajax');
var extrairMetadados = require('utils/extrair-metadados');

var config = function (versao, id, metadados) {
  return {
    method: 'GET',

    url: '/editar/api/servico/' + slugify(versao) + '/' + slugify(id),

    config: function (xhr) {
      xhr.setRequestHeader('Accept', 'application/xml');
    },

    extract: extrairMetadados(metadados),

    deserialize: function (str) {
      return new DOMParser().parseFromString(str, 'application/xml');
    },
  };
};

var carregarV3 = function (cabecalho, xml) {
  cabecalho.limparErro();
  return importar(xml);
};

var carregar = function (id, cabecalho) {
  return m.request(config('v3', id, cabecalho.metadados))
    .then(_.bind(carregarV3, this, cabecalho), erro);
};

module.exports = carregar;
