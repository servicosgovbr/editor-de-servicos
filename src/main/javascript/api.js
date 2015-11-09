'use strict';

module.exports = {
  descartar: function(id) {
    var url = '/editar/api/pagina/servico/' + id + '/descartar';
    var mimeType = 'application/xml';

    return m.request({
      method: 'POST',
      url: url,
      config: function (xhr) {
        xhr.setRequestHeader('Accepts', mimeType);
      },
      deserialize: function (str) {
        return new DOMParser().parseFromString(str, 'application/xml');
      },
    });
  }
};