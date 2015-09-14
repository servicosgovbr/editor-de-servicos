'use strict';

var v = require('utils/validacoes');

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
  this.nome = v.prop(data.nome || '', v.obrigatorio, v.textoCurto);
  this.conteudo = v.prop(data.conteudo || '', v.textoLongo);
};


module.exports = {
  Pagina: Pagina
};
