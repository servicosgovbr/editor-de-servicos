'use strict';

var extrairMetadados = require('utils/extrair-metadados');

module.exports = {
  publicar: function(id, metadados) {
    metadados = metadados || m.prop({});

    return m.request({
      method: 'PUT',
      url: '/editar/api/pagina/servico/' + id,
      extract: extrairMetadados(metadados),
      config: function (xhr) {
        xhr.setRequestHeader('Accepts', 'text/plain');
      }
    });
  },

  descartar: function (id, metadados) {
    metadados = metadados || m.prop({});
    var url = '/editar/api/pagina/servico/' + id + '/descartar';
    var mimeType = 'application/xml';

    return m.request({
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
  }
};
