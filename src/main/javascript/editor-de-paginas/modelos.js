'use strict';

var v = require('../validacoes');

var textoCurto = v.maximo(150);
var textoLongo = v.maximo(500);

var id = (function () {
  var counters = {};
  var gerador = function (base) {
    if (!counters[base]) {
      counters[base] = 0;
    }
    return base + '-' + counters[base]++;
  };

  gerador.reset = function () {
    counters = {};
  };

  return gerador;
})();

var Pagina = function (config) {
  var data = (config || {});
  this.id = id('pagina');
  this.tipo = v.prop(data.tipo || '', v.obrigatorio);
  this.nome = v.prop(data.nome || '', v.obrigatorio, textoCurto);
  this.conteudo = v.prop(data.conteudo || '', textoLongo);
};


module.exports = {
  Pagina: Pagina
};
