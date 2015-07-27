'use strict';

var slugify = require('slugify');
var modelos = require('modelos');
var importarV2 = require('componentes/importar-xml-v2');

module.exports = function (versao, id) {
  if (versao && id) {
    return m.request({
        method: 'GET',

        url: '/editar/api/servico/' + slugify(versao) + '/' + slugify(id),

        config: function (xhr) {
          xhr.setRequestHeader('Accepts', 'application/xml');
        },

        deserialize: function (str) {
          return new DOMParser().parseFromString(str, 'application/xml');
        },
      })
      .then(importarV2);
  }

  return m.prop(new modelos.Servico());
};
