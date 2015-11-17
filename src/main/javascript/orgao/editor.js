'use strict';

var Tooltips = require('tooltips');
var EditorPagina = require('pagina/editor');
var api = require('api');

module.exports = {
  view: function (ctrl) {
    return m.component(EditorPagina, {
      tipo: 'orgao',
      tituloNome: 'Selecione o órgão',
      componenteNome: require('orgao/select-orgao'),
      nomeFn: function (url) {
        var urlParam = url();
        return api.obterNomeOrgao(urlParam);
      },
      tamanhoConteudo: 1500,
      tooltips: {
        tipo: Tooltips.tipoPagina,
        nome: Tooltips.escolhaOrgao,
        conteudo: Tooltips.conteudoOrgao
      }
    });
  }
};
