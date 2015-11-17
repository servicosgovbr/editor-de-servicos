'use strict';

var safeGet = require('utils/code-checks').safeGet;
var ModeloPagina = require('pagina/modelo');

var slugify = require('slugify');
var onErro = require('utils/erro-ajax');
var atributosCsrf = require('utils/atributos-csrf');

function postarPagina(args) {
  var tipo = safeGet(args, 'tipo');
  var pagina = {
    nome: safeGet(args, 'nome'),
    conteudo: safeGet(args, 'conteudo')
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

module.exports = function (tipo, pagina) {
  return postarPagina({
    tipo: tipo,
    nome: pagina.nome(),
    conteudo: pagina.conteudo()
  }).then(lerJson, onErro);
};
