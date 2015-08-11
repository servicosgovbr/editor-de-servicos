'use strict';

module.exports = {

  controller: function (args) {
    var config = _.merge({
      salvar: _.noop
    }, args);

    this.salvar = config.salvar;
  },

  view: function (ctrl) {
    if (m.route().startsWith('/editar/servico/')) {
      return m('#metadados', [

      m('button#salvar', {
          onclick: _.bind(ctrl.salvar, ctrl)
        }, [
        m('i.fa.fa-floppy-o'),
        m.trust('&nbsp; Salvar')
      ]),

    ]);
    }

    return m('#metadados');
  }

};
