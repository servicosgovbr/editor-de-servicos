'use strict';

module.exports = {

  controller: function (args) {
    var maximo = args.maximo || 500;
    this.config = _.extend(args || {}, {
      onkeyup: m.withAttr('value', function (txt) {
        this.caracteres(maximo - txt.length);
      }.bind(this))

    });
    this.caracteres = m.prop(_.isString(this.config.value) ? maximo - this.config.value.length : maximo);
  },

  view: function (ctrl, args) {
    var config = _.extend(ctrl.config, args);

    return m('.editor-markdown.input-container', {
      class: args.erro
    }, [
      m('textarea', config),
      m('div.footer', [
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
