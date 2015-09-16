'use strict';

var slugify = require('slugify');
var onErro = require('utils/erro-ajax');

function postarOrgao(nome, orgao) {
  var id = slugify(nome);

  return m.request({
    method: 'POST',
    url: '/editar/orgao/' + id,
    data: orgao,
    background: true
  });

}

module.exports = function (orgao) {
  return postarOrgao(orgao.nome(), {
    nome: orgao.nome(),
    conteudo: orgao.conteudo()
  }).then(_.identity, onErro);
};
