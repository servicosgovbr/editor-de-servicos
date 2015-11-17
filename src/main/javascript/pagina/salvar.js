'use strict';

var safeGet = require('utils/code-checks').safeGet;
var ModeloPagina = require('pagina/modelo');
var slugify = require('slugify');
var onErro = require('utils/erro-ajax');
var atributosCsrf = require('utils/atributos-csrf');
var promise = require('utils/promise');
var validacoes = require('utils/validacoes');

function postarPagina(args) {
  var tipo = safeGet(args, 'tipo');
  var pagina = {
    nome: safeGet(args, 'nome'),
    conteudo: args.conteudo
  };

  var id = slugify(pagina.nome);

  return m.request({
    method: 'POST',
    url: '/editar/api/pagina/' + tipo + '/' + id,
    data: pagina,
    background: true,
    config: function (xhr) {
      xhr.setRequestHeader(atributosCsrf.header, atributosCsrf.token);
    }
  });
}

var lerJson = function (json) {
  return new ModeloPagina(json);
};

function validaNome(pagina) {
  if (validacoes.valida(pagina.nome)) {
    return pagina;
  }
  throw 'Erro na validação do nome da pagina';
}

module.exports = function (tipo, pagina) {
  return promise.resolve(pagina)
    .then(validaNome)
    .then(function (pg) {
      return postarPagina({
        tipo: tipo,
        nome: pg.nome(),
        conteudo: pg.conteudo()
      });
    }).then(lerJson, onErro);
};
