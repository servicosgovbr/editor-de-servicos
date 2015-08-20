'use strict';

var erros = {
  'nome-obrigatorio': 'Nome do serviço é obrigatório',
  'nome-max-150': 'Nome do serviço pode conter no máximo 150 caracteres'
};

function e(key) {
  return erros[key];
}

module.exports = {
  nome: function (nome) {
    if (!nome) {
      return e('nome-obrigatorio');
    }
    if (nome.length > 150) {
      return e('nome-max-150');
    }
  }
};
