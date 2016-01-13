'use strict';

var tratamentoAcessoNegado = require('utils/tratamento-acesso-negado');
var tratamentoConcorrenciaEdicao = require('utils/tratamento-concorrencia-edicao');

function fazMockSeRodarEmTestes(xhr) {
  if (!xhr.getResponseHeader) {
    xhr.getResponseHeader = _.noop;
  }
}

module.exports = function (metadados) {
  return function (xhr) {
    fazMockSeRodarEmTestes(xhr);

    metadados({
      publicado: {
        revisao: xhr.getResponseHeader('X-Git-Commit-Publicado'),
        autor: xhr.getResponseHeader('X-Git-Autor-Publicado'),
        horario: xhr.getResponseHeader('X-Git-Horario-Publicado')
      },
      editado: {
        revisao: xhr.getResponseHeader('X-Git-Commit-Editado'),
        autor: xhr.getResponseHeader('X-Git-Autor-Editado'),
        horario: xhr.getResponseHeader('X-Git-Horario-Editado')
      }
    });

    tratamentoAcessoNegado(xhr);
    tratamentoConcorrenciaEdicao(xhr);

    if (xhr.status === 406 || xhr.status === 403) {
      return 'acesso_negado';
    } else {
      return xhr.responseText;
    }
  };

};
