'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;
    this.converter = new window.showdown.Converter();
  },

  view: function (ctrl) {

    var nomesPopularesView = function () {
      if (!_.isEmpty(ctrl.servico.nomesPopulares())) {
        return m('p', [
                    'Você também pode conhecer este serviço como: ',
                    ctrl.servico.nomesPopulares().join(', '),
                    '.'
                ]);
      }
      return m.component(require('servico/visualizar/view-vazia'));
    };


    return m('#servico-descricao', [
        m('h4', nomesPopularesView()),
        m('h3.subtitulo-servico', 'O que é?'),
        m('.markdown', m.trust(ctrl.converter.makeHtml(ctrl.servico.descricao())))
    ]);
  }
};
