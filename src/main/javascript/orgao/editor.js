'use strict';

var Tooltips = require('tooltips');
var EditorPagina = require('pagina/editor');

module.exports = {

  view: function (ctrl) {

    return m.component(EditorPagina, {
      tipo: 'orgao',
      tituloNome: 'Selecione o Órgão',
      componenteNome: require('orgao/select-orgao'),
      tamanhoConteudo: 1500,

      tooltips: {
        tipo: Tooltips.tipoPagina,
        nome: Tooltips.escolhaOrgao,
        conteudo: Tooltips.conteudoOrgao
      }
    });

  }

};
