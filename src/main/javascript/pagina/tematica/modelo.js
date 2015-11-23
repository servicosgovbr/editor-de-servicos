'use strict';

var safeGet = require('utils/code-checks').safeGet;
var v = require('utils/validacoes');
var id = require('utils/id');

module.exports = function (config) {
  var data = (config || {});
  var tamanho = safeGet(data, 'tamanho');

  var validaIdJaExistente = _.trim(data.nome) ? _.noop : v.idUnico;
  this.id = id('pagina-tematica');
  this.nome = v.prop(data.nome || '', v.obrigatorio, v.textoCurto, validaIdJaExistente);
  this.conteudo = v.prop(data.conteudo || '', v.obrigatorio, v.maximo(tamanho));
};
