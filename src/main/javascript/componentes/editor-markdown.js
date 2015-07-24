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
    return m('', [
      m('.editor-barra-ferramentas', [
        m('a', {
          alt: 'Adicionar link',
          title: 'Adicionar link',
          href: ''
        }, [m('i.fa.fa-link')]),

        m('a', {
          alt: 'Adicionar item de lista',
          title: 'Adicionar item de lista',
          href: ''
        }, [m('i.fa.fa-list')])
      ]),

      m('textarea', ctrl.config),
      m('.counter', ['Caracteres restantes: ', m('span', ctrl.caracteres())])
    ]);
  }
};
