'use strict';

var erro = require('utils/erro-ajax');

module.exports = function (xhr) {
  if (xhr.status === 406 || xhr.status === 403) {
    erro('Acesso negado. Você não possui permissão para realizar esse tipo de operação.');
  }
};
