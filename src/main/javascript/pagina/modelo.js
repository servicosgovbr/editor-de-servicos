'use strict';

var v = require('utils/validacoes');
var id = require('utils/id');

module.exports = function (config) {
  var data = (config || {});
  this.id = id('pagina');
  this.tipo = v.prop(data.tipo || '', v.obrigatorio);
  this.nome = v.prop(data.nome || '', v.obrigatorio, v.textoCurto);
  this.conteudo = v.prop(data.conteudo || '', v.obrigatorio, v.maximo(1500));
};
