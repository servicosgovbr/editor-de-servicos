'use strict';

var erros = {
  'nome-obrigatorio': 'Nome do serviço é obrigatório',
  'nome-max-150': 'Nome pode conter no máximo 150 caracteres',
  'sigla-max-15': 'Sigla não pode ter mais do que 15 caracteres',
  'nome-pop-max-150': 'Nome popular não pode passar de 150 caracteres'
};

module.exports = function (key) {
  return erros[key];
};
