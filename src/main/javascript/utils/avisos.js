'use strict';


function sucesso(msg) {
  alertify.success(msg);
}

function erro(msg) {
  alertify.error(msg);
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
