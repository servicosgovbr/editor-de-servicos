'use strict';

module.exports = {
  controller: function (args) {
    this.erro = args.erro || _.noop;
  },

  view: function (ctrl) {
    if (!ctrl.erro()) {
      return m('span');
    }

    return m('span.erro', [
      m('span.fa.fa-exclamation-triangle'),

      m.trust('&nbsp; Erro de comunicação com o servidor. '),

      m('a', {
        onclick: ctrl.erro().tentarNovamente
      }, 'Tentar novamente')

    ]);
  }
};
