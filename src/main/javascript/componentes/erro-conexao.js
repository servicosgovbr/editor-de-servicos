'use strict';

module.exports = {
  controller: function (args) {
    this.erro = args.erro;
  },

  view: function (ctrl) {
    if (!ctrl.erro()) {
      return m('');
    }

    return m('span.erro', [
      m('span.fa.fa-exclamation-triangle', 'Erro de conexão'),
      m('button', {
        onclick: ctrl.erro().tentarNovamente
      }, 'Tentar novamente')
    ]);
  }
};
