'use strict';

var v = require('utils/validacoes');
var id = require('utils/id');
var idUnico = require('utils/id-unico');

var validaIdServico = function (unsafeId) {
  return idUnico(unsafeId) ? undefined : 'erro-nome-servico-existente';
};

module.exports = function (config) {
  var data = (config || {});

  var validaIdJaExistente = _.trim(data.nome) ? _.noop : validaIdServico;
  this.id = id('pagina');
  this.tipo = v.prop(data.tipo || '', v.obrigatorio);
  this.nome = v.prop(data.nome || '', v.obrigatorio, v.textoCurto, validaIdJaExistente);
  this.conteudo = m.prop(data.conteudo || '');
  this.tamanhoConteudo = _.bind(function (tamanho) {
    this.conteudo = v.prop(this.conteudo(), v.obrigatorio, v.maximo(tamanho));
  }, this);
};
