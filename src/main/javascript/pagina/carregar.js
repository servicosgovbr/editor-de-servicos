'use strict';

var slugify = require('slugify');
var ModeloPagina = require('pagina/modelo');
var erro = require('utils/erro-ajax');
var extrairMetadados = require('utils/extrair-metadados');

var config = function (id, metadados) {
  return {
    method: 'GET',

    url: '/editar/api/orgao/' + slugify(id),

    config: function (xhr) {
      xhr.setRequestHeader('Accept', 'application/json');
    },

    extract: extrairMetadados(metadados),

    deserialize: function (str) {
      return JSON.parse(str);
    },
  };
};

var importar = function (cabecalho, json) {
  cabecalho.limparErro();
  return new ModeloPagina(json);
};

var carregar = function (id, cabecalho) {
  return m.request(config(id, cabecalho.metadados))
    .then(_.bind(importar, this, cabecalho), erro);
};

module.exports = carregar;
