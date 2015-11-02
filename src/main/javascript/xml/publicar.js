'use strict';

var promise = require('utils/promise');
var slugify = require('slugify');
var validacoes = require('utils/validacoes');

function publicar(servico) {
  var idServico = slugify(servico.nome());

  return m.request({
    method: 'PUT',
    url: '/editar/api/pagina/servico/' + idServico
  }).then(function() {
    return servico;
  });
}

function validar(servico) {
  if (validacoes.valida(servico)) {
    return servico;
  } else {
    throw 'Erros na validação dos campos de serviço';
  }
}

module.exports = function(servico) {
   return promise.resolved(servico)
            .then(validar)
            .then(publicar);
};

