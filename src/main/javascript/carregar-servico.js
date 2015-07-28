'use strict';

var slugify = require('slugify');
var modelos = require('modelos');
var importarV2 = require('componentes/importar-xml-v2');
var importarV3 = require('componentes/importar-xml-v3');

var config = function (versao, id, metadados) {
  return {
    method: 'GET',

    url: '/editar/api/servico/' + slugify(versao) + '/' + slugify(id),

    config: function (xhr) {
      xhr.setRequestHeader('Accept', 'application/xml');
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
  };
};

module.exports = function (id, metadados) {
  if (id) {
    return m.request(config('v3', id, metadados)).then(importarV3, function () {
      return m.request(config('v2', id, metadados)).then(importarV2);
    });
  }

  return m.prop(new modelos.Servico());
};
