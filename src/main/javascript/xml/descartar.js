'use strict';

var api = require('api');
var promise = require('utils/promise');
var slugify = require('slugify');
var validacoes = require('utils/validacoes');
var importarXml = require('xml/importar');

function descartar(servico) {
  var idServico = slugify(servico.nome());
  return api.descartar(idServico);
}

function validaNome(servico) {
  if (validacoes.valida(servico.nome)) {
    return servico;
  }
  throw 'Erro na validação do nome do serviço';
}

module.exports = function(servico) {
   return promise.resolved(servico)
            .then(validaNome)
            .then(descartar)
            .then(importarXml);
};

