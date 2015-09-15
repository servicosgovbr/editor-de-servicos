'use strict';

module.exports = function () {

  this.metadados = m.prop({});

  this.erro = m.prop(null);

  this.limparErro = function () {
    this.erro(null);
  };

  this.tentarNovamente = function (fn) {
    this.erro({
      tentarNovamente: fn
    });
  };

};
