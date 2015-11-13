'use strict';

alertify.set({ delay: 10000 });

function sucesso(msg) {
  alertify.success(msg);
}

function erro(msg) {
  alertify.error(msg);
}

function alerta(msg) {
  alertify.log(msg);
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
