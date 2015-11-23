'use strict';

var api = require('api');
var promise = require('utils/promise');
var slugify = require('slugify');
var validacoes = require('utils/validacoes');

function publicar(tipo, modelo, idUnsafe, metadados) {
  var id = slugify(idUnsafe);
  return api.publicar(tipo, id, metadados).then(function () {
    return modelo;
  });
}

function validar(modelo) {
  if (validacoes.valida(modelo)) {
    return modelo;
  } else {
    throw 'Erros de validação';
  }
}

function fluxoPublicar(tipo, modelo, id, metadados) {
  return promise.resolved(modelo)
    .then(validar)
    .then(function (s) {
      return publicar(tipo, s, id, metadados);
    });
}

module.exports = {
  publicarServico: function (servico, metadados) {
    return fluxoPublicar('servico', servico, servico.nome(), metadados);
  },
  publicarPaginaTematica: function (pagina, metadados) {
    return fluxoPublicar('pagina-tematica', pagina, pagina.nome(), metadados);
  },
  publicarOrgao: function (pagina, metadados) {
    return fluxoPublicar('orgao', pagina, pagina.url(), metadados);
  }
};
