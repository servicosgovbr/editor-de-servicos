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

var etapas = function (lista, ctrl) {
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
      ]),
      m('button.remove', {
        onclick: ctrl.remover.bind(ctrl, i)
      }, [m('span')])
    );
  });
};

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.remover = function (i) {
      alertify.confirm('Você tem certeza que deseja remover essa etapa?', function (result) {
        if(result) {
           this.servico().etapas().splice(i, 1);
           m.redraw();
        }
      }.bind(this));
    };
  },

  view: function (ctrl) {
    return m('nav#menu-lateral', [
      m('ul', [
        item('Dados básicos'),
        item('Solicitantes'),
        item('Etapas do Serviço', m('ul', etapas(ctrl.servico().etapas(), ctrl))),
        item('Outras Informações'),
      ]),
    ]);
  }

};
