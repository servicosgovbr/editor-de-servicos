'use strict';

var Tooltips = require('tooltips');
var EditorPagina = require('pagina/editor');

module.exports = {
  view: function () {

    return m.component(EditorPagina, {

      tipo: 'area-de-interesse',

      componenteNome: require('area-de-interesse/componentes/select'),

      tamanhoConteudo: 1500,

      tooltips: {
        tipo: Tooltips.tipoPagina,
        nome: Tooltips.escolhaAreaInteresse,
        conteudo: Tooltips.conteudoAreaInteresse
      }

    });

  }

};
