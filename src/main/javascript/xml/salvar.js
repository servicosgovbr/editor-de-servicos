'use strict';

module.exports = function (nome, xml, metadados) {

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

    extract: function (xhr) {
      metadados({
        autor: xhr.getResponseHeader && xhr.getResponseHeader('X-Git-Author'),
        revisao: xhr.getResponseHeader && xhr.getResponseHeader('X-Git-Revision'),
        horario: xhr.getResponseHeader && new Date(xhr.getResponseHeader('Last-Modified'))
      });

      return xhr.responseText;
    },

    deserialize: function (str) {
      return new DOMParser().parseFromString(str, 'application/xml');
    },

  });

};
