'use strict';

var erro = require('utils/erro-ajax');
var extrairMetadados = require('utils/extrair-metadados');
var atributosCsrf = require('utils/atributos-csrf');

function request(opts) {
  return m.request(_.merge({
      deserialize: _.identity
    }, opts))
    .then(_.identity, erro);
}

function configCsrf(xhr) {
  xhr.setRequestHeader(atributosCsrf.header, atributosCsrf.token);
}

module.exports = {
  publicar: function (id, metadados) {
    return request({
      method: 'PUT',
      url: '/editar/api/pagina/servico/' + id,
      extract: extrairMetadados(metadados),
      config: configCsrf
    });
  },

  descartar: function (id, metadados) {
    var url = '/editar/api/pagina/servico/' + id + '/descartar';
    var mimeType = 'application/xml';

    return request({
      method: 'POST',
      url: url,
      config: function (xhr) {
        xhr.setRequestHeader('Accept', mimeType);
        configCsrf(xhr);
      },
      extract: extrairMetadados(metadados),
      deserialize: function (str) {
        return new DOMParser().parseFromString(str, 'application/xml');
      },
    });
  },

  despublicar: function (id, metadados) {
    var url = '/editar/api/pagina/servico/' + id + '/despublicar';
    return request({
      method: 'POST',
      url: url,
      extract: extrairMetadados(metadados),
      config: configCsrf
    });
  },

  importarXml: function (urlParam) {
    return request({
      method: 'GET',
      url: '/editar/api/importar-xml',
      config: function (xhr) {
        xhr.setRequestHeader('Accept', 'application/xml');
      },
      data: {
        url: urlParam
      }
    }).then(function (str) {
      //retorno com erro não usa xml, por isso não usamos função "deserialize", e fazemos isso aqui
      return new DOMParser().parseFromString(str, 'application/xml');
    });
  }
};
