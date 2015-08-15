'use strict';

module.exports = {

  controller: function (args) {
    var config = _.merge({
      salvar: _.noop
    }, args);

    this.salvando = m.prop(false);
    this.salvar = function () {
      this.salvando(true);
      return config.salvar().then(_.bind(function (resp) {
        this.salvando(false);
        m.redraw();
        return resp;
      }, this));
    };
  },

  view: function (ctrl) {
    if (m.route().indexOf('/editar/servico/') >= 0) {
      return m('#metadados', [

      m.component(require('componentes/status-conexao')),

      m('button#salvar', {
          onclick: _.bind(ctrl.salvar, ctrl),
          disabled: ctrl.salvando() ? 'disabled' : ''
        }, ctrl.salvando() ? [
          m('i.fa.fa-spin.fa-spinner'),
          m.trust('&nbsp; Salvando...')
        ] : [
          m('i.fa.fa-floppy-o'),
          m.trust('&nbsp; Salvar')
        ]),
      ]);
    }

    return m('#metadados');
  }

};
