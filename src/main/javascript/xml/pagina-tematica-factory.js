'use strict';

var PaginaTematica = require('pagina/tematica/modelo');

var paginaTematica = function (x) {
  return new PaginaTematica({
    tipo: 'pagina-tematica',
    nome: x.find('> nome').text(),
    conteudo: x.find('> conteudo').text(),
    tamanho: 10000
  });
};

module.exports = {
  domParaPaginaTematica: function (dom) {
    return paginaTematica(jQuery('pagina-tematica', dom));
  }
};
