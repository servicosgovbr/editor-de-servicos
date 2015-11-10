'use strict';

var api = require('api');
var promise = require('utils/promise');
var slugify = require('slugify');
var validacoes = require('utils/validacoes');
var importarXml = require('xml/importar');

function descartar(servico, metadados) {
  var idServico = slugify(servico.nome());
  return api.descartar(idServico, metadados);
}

function validaNome(servico) {
  if (validacoes.valida(servico.nome)) {
    return servico;
  }
  throw 'Erro na validação do nome do serviço';
}

module.exports = function (servico, metadados) {
  return promise.resolved(servico)
    .then(validaNome)
    .then(function (s) {
      return descartar(s, metadados);
    })
    .then(importarXml);
};
