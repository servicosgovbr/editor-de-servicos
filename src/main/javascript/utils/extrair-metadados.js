'use strict';

module.exports = function (metadados) {
  return function (xhr) {
    if (!xhr.getResponseHeader) { // fix para o mock de testes
      xhr.getResponseHeader = _.noop;
    }

    metadados({
      publicado: {
        revisao: xhr.getResponseHeader('X-Git-Commit-Publicado'),
        autor: xhr.getResponseHeader('X-Git-Autor-Publicado'),
        horario: xhr.getResponseHeader('X-Git-Horario-Publicado'),
      },
      editado: {
        revisao: xhr.getResponseHeader('X-Git-Commit-Editado'),
        autor: xhr.getResponseHeader('X-Git-Autor-Editado'),
        horario: xhr.getResponseHeader('X-Git-Horario-Editado'),
      }
    });

    return xhr.responseText;
  };
};
