'use strict';

var erros = {
  'nome-obrigatorio': 'Nome do serviço é obrigatório',
  'nome-max-150': 'Nome pode conter no máximo 150 caracteres',
  'sigla-max-15': 'Sigla não pode ter mais do que 15 caracteres',
  'nome-pop-max-150': 'Nome popular não pode passar de 150 caracteres',
  'min-3-palavras-chave': 'Deve haver no mínimo 3 palavras chave',
  'palavra-chave-max-50': 'Palavra chave não pode passar de 50 caracteres'
};

module.exports = function (key) {
  return erros[key];
};
