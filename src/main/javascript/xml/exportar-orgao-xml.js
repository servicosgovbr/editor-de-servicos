'use strict';

var XML = require('utils/xml');

module.exports = function (orgao) {
  var doc = XML.createDocument('http://servicos.gov.br/v3/schema');

  m.render(doc, m('orgao', {
    'xmlns': 'http://servicos.gov.br/v3/schema',
    'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance',
  }, [
    m('url', orgao.url()),
    m('nome', orgao.nome()),
    m('conteudo', orgao.conteudo()),
    m('contato', orgao.contato()),
  ]));
  XML.cdata(doc, 'conteudo');
  XML.cdata(doc, 'contato');

  return doc;
};
