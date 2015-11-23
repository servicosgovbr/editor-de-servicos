'use strict';

var domParaServico = require('xml/servico-factory').domParaServico;
var domParaPaginaTematica = require('xml/pagina-tematica-factory').domParaPaginaTematica;
var domParaOrgao = require('xml/orgao-factory').domParaOrgao;
var exportarXmlServico = require('xml/exportar-servico-xml');
var exportarXmlPaginaTematica = require('xml/exportar-pagina-tematica-xml');
var exportarXmlOrgao = require('xml/exportar-orgao-xml');
var validacoes = require('utils/validacoes');
var promiseUtil = require('utils/promise');
var limparModelo = require('limpar-modelo');
var api = require('api');

var valida = _.curry(function (nome, doc) {
  if (validacoes.valida(_.get(doc, nome))) {
    return doc;
  }
  throw 'Erro na validação do nome';
});

var validaNome = valida('nome');
var validaUrl = valida('url');

function salvarServico(servico, metadados) {
  return promiseUtil.resolved(servico)
    .then(limparModelo)
    .then(validaNome)
    .then(exportarXmlServico)
    .then(function (xml) {
      return api.salvar('servico', servico.nome(), xml, metadados);
    })
    .then(domParaServico);
}

function salvarPaginaTematica(pagina, metadados) {
  return promiseUtil.resolved(pagina)
    .then(validaNome)
    .then(exportarXmlPaginaTematica)
    .then(function (xml) {
      return api.salvar('pagina-tematica', pagina.nome(), xml, metadados);
    })
    .then(domParaPaginaTematica);
}

function salvarOrgao(orgao, metadados) {
  return promiseUtil.resolved(orgao)
    .then(validaUrl)
    .then(exportarXmlOrgao)
    .then(function (xml) {
      return api.salvar('orgao', orgao.url(), xml, metadados);
    })
    .then(domParaOrgao);
}

module.exports = {
  salvarServico: salvarServico,
  salvarPaginaTematica: salvarPaginaTematica,
  salvarOrgao: salvarOrgao
};
