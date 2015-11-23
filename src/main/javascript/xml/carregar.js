'use strict';

var domParaServico = require('xml/servico-factory').domParaServico;
var domParaPaginaTematica = require('xml/pagina-tematica-factory').domParaPaginaTematica;
var domParaOrgao = require('xml/orgao-factory').domParaOrgao;
var api = require('api');

function carregarServico(id, cabecalho) {
  return api.carregar('servico', id, cabecalho.metadados)
    .then(function (xml) {
      cabecalho.limparErro();
      return domParaServico(xml);
    });
}

function carregarPaginaTematica(id, cabecalho) {
  return api.carregar('pagina-tematica', id, cabecalho.metadados)
    .then(function (xml) {
      cabecalho.limparErro();
      return domParaPaginaTematica(xml);
    });
}

function carregarOrgao(id, cabecalho) {
  return api.carregar('orgao', id, cabecalho.metadados)
    .then(function (xml) {
      cabecalho.limparErro();
      return domParaOrgao(xml);
    });
}

module.exports = {
  carregarServico: carregarServico,
  carregarPaginaTematica: carregarPaginaTematica,
  carregarOrgao: carregarOrgao
};
