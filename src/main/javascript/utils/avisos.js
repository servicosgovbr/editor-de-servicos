'use strict';

function sucesso(msg) {
  alertify.success(msg, 0);
}

function erro(msg) {
  alertify.error(msg, 0);
}

function alerta(msg) {
  alertify.log(msg, '', 0);
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

function alertFn(msg) {
  return function () {
    alerta(msg);
  };
}

module.exports = {
  sucesso: sucesso,
  erro: erro,
  alerta: alerta,
  sucessoFn: sucessoFn,
  erroFn: erroFn,
  alertFn: alertFn
};
