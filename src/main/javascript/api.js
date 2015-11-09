'use strict';

var extrairMetadados = require('utils/extrair-metadados');

module.exports = {
  descartar: function (id, metadados) {
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
