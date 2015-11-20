'use strict';

var Orgao = require('pagina/orgao/modelo');

var orgao = function (x) {
  return new Orgao({
    url: x.find('> url').text(),
    nome: x.find('> nome').text(),
    conteudo: x.find('> conteudo').text(),
    contato: x.find('> contato').text(),
    tamanho: 10000
  });
};

module.exports = {
  domParaOrgao: function (dom) {
    return orgao(jQuery('orgao', dom));
  }
};
