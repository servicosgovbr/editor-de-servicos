'use strict';

var slugify = require('slugify');
var modelos = require('modelos');
var importarV1 = require('xml/importar-v1');
var importarV3 = require('xml/importar-v3');

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

module.exports = function (id, cabecalho) {
  if (id) {
    var carregar = function () {
      return m.request(config('v3', id, cabecalho.metadados)).then(function (xml) {
        cabecalho.limparErro();
        return importarV3(xml);
      }, function () {
        return m.request(config('v1', id, cabecalho.metadados)).then(function (xml) {
          cabecalho.limparErro();
          return importarV1(xml);
        }, function () {
          cabecalho.tentarNovamente(carregar);
          return new modelos.Servico();
        });
      });
    };
    return carregar();
  }

  return m.prop(new modelos.Servico());
};
