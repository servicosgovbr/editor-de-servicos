'use strict';

var api = require('api');
var promise = require('utils/promise');
var slugify = require('slugify');
var validacoes = require('utils/validacoes');
var domParaServico = require('xml/servico-factory').domParaServico;
var domParaPaginaTematica = require('xml/pagina-tematica-factory').domParaPaginaTematica;
var domParaOrgao = require('xml/orgao-factory').domParaOrgao;

function descartar(tipo, modelo, idUnsafe, metadados) {
  var id = slugify(idUnsafe);
  return api.descartar(tipo, id, metadados);
}

function validaNome(modelo) {
  if (validacoes.valida(modelo.nome)) {
    return modelo;
  }
  throw 'Erro na validação do nome';
}

function fluxoDescarte(tipo, modelo, id, metadados) {
  return promise.resolved(modelo)
    .then(validaNome)
    .then(function (s) {
      return descartar(tipo, s, id, metadados);
    });
}

module.exports = {
  descartarServico: function (servico, metadados) {
    return fluxoDescarte('servico', servico, servico.nome(), metadados)
      .then(domParaServico);
  },
  descartarPaginaTematica: function (pagina, metadados) {
    return fluxoDescarte('pagina-tematica', pagina, pagina.nome(), metadados)
      .then(domParaPaginaTematica);
  },
  descartarOrgao: function (pagina, metadados) {
    return fluxoDescarte('orgao', pagina, pagina.url(), metadados)
      .then(domParaOrgao);
  }
};
