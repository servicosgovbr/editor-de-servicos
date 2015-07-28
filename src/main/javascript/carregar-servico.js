'use strict';

var slugify = require('slugify');
var modelos = require('modelos');
var importarV2 = require('componentes/importar-xml-v2');
var importarV3 = require('componentes/importar-xml-v3');

var config = function (versao, id) {
  return {
    method: 'GET',

    url: '/editar/api/servico/' + slugify(versao) + '/' + slugify(id),

    config: function (xhr) {
      xhr.setRequestHeader('Accept', 'application/xml');
    },

    deserialize: function (str) {
      return new DOMParser().parseFromString(str, 'application/xml');
    },
  };
};

module.exports = function (id) {
  if (id) {
    return m.request(config('v3', id)).then(importarV3, function () {
      return m.request(config('v2', id)).then(importarV2);
    });
  }

  return m.prop(new modelos.Servico());
};
