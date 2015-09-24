'use strict';

var safeGet = require('utils/code-checks').safeGet;

var slugify = require('slugify');
var ModeloPagina = require('pagina/modelo');
var erro = require('utils/erro-ajax');
var extrairMetadados = require('utils/extrair-metadados');

var config = function (args, metadados) {
  var tipo = safeGet(args, 'tipo');
  var unsafeId = safeGet(args, 'id');
  var id = slugify(unsafeId);

  return {

    method: 'GET',

    url: '/editar/api/pagina/' + tipo + '/' + id,

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

module.exports = function (args, cabecalho) {
  return m.request(config(args, cabecalho.metadados))
    .then(_.bind(importar, this, cabecalho), erro);
};
