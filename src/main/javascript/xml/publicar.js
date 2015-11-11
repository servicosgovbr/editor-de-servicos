'use strict';

var api = require('api');
var promise = require('utils/promise');
var slugify = require('slugify');
var validacoes = require('utils/validacoes');

function publicar(servico, metadados) {
  var idServico = slugify(servico.nome());
  return api.publicar(idServico, metadados).then(function () {
    return servico;
  });
}

function validar(servico) {
  if (validacoes.valida(servico)) {
    return servico;
  } else {
    throw 'Erros na validação dos campos de serviço';
  }
}

module.exports = function (servico, metadados) {
  return promise.resolved(servico)
    .then(validar)
    .then(function (s) {
      return publicar(s, metadados);
    });
};
