'use strict';

var id = require('utils/id');
var v = require('utils/validacoes');

module.exports = function (config) {
  var data = (config || {});
  this.id = id('solicitante');
  this.tipo = v.prop(data.tipo || '', v.obrigatorio, v.textoCurto);
  this.requisitos = v.prop(data.requisitos || '', v.textoLongo);
};
