'use strict';

var erros = {
  'nome-obrigatorio': 'Nome do serviço é obrigatório',
  'nome-max-150': 'Nome do serviço pode conter no máximo 150 caracteres'
};

module.exports = function (key) {
  return erros[key];
};
