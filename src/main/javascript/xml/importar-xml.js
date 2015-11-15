'use strict';

var api = require('api');
var domParaServico = require('xml/servico-factory').domParaServico;

module.exports = function(url) {
  return api.importarXml(url)
    .then(domParaServico);
};