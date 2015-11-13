'use strict';

var extrairMetadados = require('utils/extrair-metadados');

function request(opts) {
  return m.request(_.merge({
    method: 'GET',
    deserialize: _.identity
  }, opts));
}

module.exports = {
  publicar: function (id, metadados) {
    return request({
      method: 'PUT',
      url: '/editar/api/pagina/servico/' + id,
      extract: extrairMetadados(metadados)
    });
  },

  descartar: function (id, metadados) {
    var url = '/editar/api/pagina/servico/' + id + '/descartar';
    var mimeType = 'application/xml';

    return request({
      method: 'POST',
      url: url,
      config: function (xhr) {
        xhr.setRequestHeader('Accepts', mimeType);
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
      extract: extrairMetadados(metadados)
    });
  }
};
