'use strict';

var safeGet = require('utils/code-checks').safeGet;
var v = require('utils/validacoes');
var id = require('utils/id');
var api = require('api');

function urlToNome(url, nomeProp) {
  if (url) {
    api.obterNomeOrgao(url)
      .then(nomeProp);
  }
}

function createUrl(initial, nomeProp) {
  var validaIdJaExistente = _.trim(initial) ? _.noop : v.idUnico;
  var urlProp = v.prop(initial, v.obrigatorio, validaIdJaExistente);
  urlToNome(initial, nomeProp);
  var url = function () {
    urlToNome(_.head(arguments), nomeProp);
    return urlProp.apply(this, arguments);
  };
  url.erro = urlProp.erro;

  return url;
}

module.exports = function (config) {
  var data = (config || {});
  var tamanho = safeGet(data, 'tamanho');

  this.id = id('orgao');
  this.nome = m.prop(data.nome || '');
  this.url = createUrl(data.url || '', this.nome);
  this.conteudo = v.prop(data.conteudo || '', v.obrigatorio, v.maximo(tamanho));
  this.contato = v.prop(data.contato || '', v.obrigatorio);
};
