'use strict';

var slugify = require('slugify');
var limparModelo = require('limpar-modelo');
var extrairMetadados = require('utils/extrair-metadados');
var mensagemErro = require('mensagens-erro');
var importarXml = require('xml/importar-v3');
var exportarXml = require('xml/exportar');

function postarServico(nome, xml, metadados) {
  return m.request({

    method: 'POST',
    url: '/editar/v3/servico/' + nome,
    data: xml,
    background: true,

    config: function (xhr) {
      xhr.setRequestHeader('Accepts', 'application/xml');
      xhr.setRequestHeader('Content-Type', 'application/xml');
    },

    serialize: function (svc) {
      return new XMLSerializer().serializeToString(svc);
    },

    extract: extrairMetadados(metadados),

    deserialize: function (str) {
      return new DOMParser().parseFromString(str, 'application/xml');
    },

  });
}

function validarServico(servico) {
  servico.validar();
  var validador = servico.validador();

  if (validador.hasErrors()) {

    var erros = [];
    erros.push(mensagemErro(validador.hasError('nome')));
    erros.push(mensagemErro(validador.hasError('sigla')));
    erros.push(mensagemErro(validador.hasError('descricao')));

    validador.hasError('nomesPopulares').map(function (e) {
      var msg = mensagemErro(e.err);
      msg = e.i + ': ' + msg;
      erros.push(msg);
    });

    erros.push(mensagemErro(validador.hasError('descricao')));

    var palavrasChave = validador.hasError('palavrasChave');
    erros.push(mensagemErro(palavrasChave.msg));
    palavrasChave.campos.map(function (e) {
      var msg = mensagemErro(e.msg);
      msg = e.i + ': ' + msg;
      erros.push(msg);
    });

    validador.clearErrors();
    return _.compact(erros);
  }
  return [];
}

module.exports = function (servicoProp, metadados) {
  var servico = limparModelo(servicoProp());

  var erros = validarServico(servico);
  if (erros.length > 0) {
    return m.deferred().reject(erros.join('\n')).promise;
  }

  var xml = exportarXml(servico);
  var onAjaxError = require('utils/erro-ajax');

  return postarServico(slugify(servico.nome()), xml, metadados)
    .then(importarXml, onAjaxError)
    .then(servicoProp);
};
