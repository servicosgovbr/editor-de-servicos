'use strict';

module.exports = {

  controller: function (args) {},

  view: function () {
    return m('.row botoes-rodape', [
        m('a.acao-servico right', ['Editar esta página', m('i.fa fa-edit')]),
        m('a.acao-servico', ['Imprimir esta página', m('i.fa fa-print')])
    ]);
  }
};
