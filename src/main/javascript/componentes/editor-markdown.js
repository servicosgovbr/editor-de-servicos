'use strict';

module.exports = {

  controller: function (args) {
    this.config = _.extend(args || {}, {
      onkeyup: m.withAttr('value', function (txt) {
        this.caracteres(500 - txt.length);
      }.bind(this))
    });

    this.caracteres = m.prop(_.isString(this.config.value) ? 500 - this.config.value.length : 500);
  },

  view: function (ctrl) {
    return m('.editor-markdown.input-container', {
      class: ctrl.config.erro
    }, [
      m('textarea', ctrl.config),
      m('footer', [
        m('span.markdown-suportado', [
          'Este campo pode ser editado com ',
          m('a[href="/editar/ajuda-markdown"][target=_blank]', ['Markdown'])
        ]),

        m('span.counter', [
          'caracteres restantes: ',
          m('span', {
            class: ctrl.caracteres() > 0 ? 'ok' : 'nok'
          }, ctrl.caracteres())
        ])
      ])
    ]);
  }
};
