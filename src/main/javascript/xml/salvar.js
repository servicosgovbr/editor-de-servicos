'use strict';

var extrairMetadados = require('utils/extrair-metadados');

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

    extract: extrairMetadados(metadados),

    deserialize: function (str) {
      return new DOMParser().parseFromString(str, 'application/xml');
    },

  });

};
