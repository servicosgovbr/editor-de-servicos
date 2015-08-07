'use strict';

var slugify = require('slugify');
var scrollTo = require('utils/scroll-to');

var item = function (texto, extra) {
  return m('li', {
    config: scrollTo('#' + slugify(texto))
  }, [
    m('span.check.ok'),
    texto,
    extra
  ]);
};

var etapas = function (lista) {
  return lista.map(function (e, i) {
    return m('li', {
        key: e.id
      }, {
        style: {
          fontFamily: '"open_sansregular"',
          textTransform: 'none',
          marginLeft: '2em'
        }
      },
      m('a', {
        config: scrollTo('#' + e.id)
      }, [
        m('span.check.ok', {
          style: {
            marginRight: '1em'
          }
        }),
        e.titulo() ? i + 1 + '. ' + e.titulo() : m('i', i + 1 + '. ' + '(sem título)')
      ])
    );
  });
};

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('nav#menu-lateral', [
      m('ul', [
        item('Dados básicos'),
        item('Solicitantes'),
        item('Etapas do Serviço', m('ul', etapas(ctrl.servico().etapas()))),
        item('Outras Informações'),
      ]),
    ]);
  }

};
