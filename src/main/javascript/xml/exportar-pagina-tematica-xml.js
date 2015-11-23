'use strict';

var XML = require('utils/xml');

module.exports = function (paginaTematica) {
  var doc = XML.createDocument('http://servicos.gov.br/v3/schema');

  m.render(doc, m('pagina-tematica', {
    'xmlns': 'http://servicos.gov.br/v3/schema',
    'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance',
  }, [
    m('nome', paginaTematica.nome()),
    m('conteudo', paginaTematica.conteudo())
  ]));
  XML.cdata(doc, 'conteudo');

  return doc;
};
