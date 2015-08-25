'use strict';

var erros = {
  'nome-obrigatorio': 'Nome do serviço é obrigatório',
  'nome-max-150': 'Nome pode conter no máximo 150 caracteres',
  'sigla-max-15': 'Sigla não pode ter mais do que 15 caracteres',
  'nome-pop-max-150': 'Nome popular não pode passar de 150 caracteres',
  'min-3-palavras-chave': 'Deve haver no mínimo 3 palavras chave',
  'palavra-chave-max-50': 'Palavra chave não pode passar de 50 caracteres',
  'descricao-obrigatoria': 'A descrição do serviço é obrigatória',
  'descricao-max-500': 'Descrição não pode ultrapassar 500 caracteres',
  'tempo-obrigatorio': 'Preenchimento do tempo é obrigatório',
  'tipo-periodo-obrigatório': 'Obrigatório a seleção da unidade de tempo',
};

module.exports = function (key) {
  return erros[key];
};
