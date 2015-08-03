'use strict';

module.exports = {

  controller: function (args) {
    this.config = _.extend((args || {}), {
      style: {
        maxWidth: '100%',
        width: '100%'
      },

      onkeyup: m.withAttr('value', function (txt) {
        this.caracteres(500 - txt.length);
      }.bind(this))
    });

    this.caracteres = m.prop(500);
  },

  view: function (ctrl) {
    return m('.editor-markdown', [
      m('textarea', ctrl.config),
      m('footer', [
        m('span.markdown-suportado', [
          'Este campo suporta ',
          m('a[href="/editar/ajuda-markdown"][target=_blank]', ['Markdown'])
        ]),

        m('span.counter', ['Caracteres restantes: ', m('span', ctrl.caracteres())])
      ])
    ]);
  }
};
