'use strict';


function sucesso(msg) {
  alertify.success(msg, 4000);
}

function erro(msg) {
  alertify.error(msg, 4000);
}

function sucessoFn(msg) {
  return function () {
    sucesso(msg);
  };
}

function erroFn(msg) {
  return function () {
    erro(msg);
  };
}

module.exports = {
  sucesso: sucesso,
  erro: erro,
  sucessoFn: sucessoFn,
  erroFn: erroFn
};
