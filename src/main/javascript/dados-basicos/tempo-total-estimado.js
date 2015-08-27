'use strict';

var referencia = require('referencia');

var selectTipo = function (prop) {
  return m.component(require('componentes/select2'), {
    prop: prop,
    data: referencia.unidadesTempo
  });
};

module.exports = {

  controller: function (args) {
    this.tempoTotalEstimado = args.servico().tempoTotalEstimado;
  },

  view: function (ctrl) {
    ctrl.tempoTotalEstimado().validar();

    return m('fieldset#tempo-total-estimado', [
      m('h3', [
        'Tempo estimado para realizar esse serviço',
        m.component(require('tooltips').tempoTotalEstimado)
      ]),

      m.component(require('componentes/select2'), {
        prop: ctrl.tempoTotalEstimado().tipo,
        data: [
          {
            id: 'ate',
            text: 'Até'
          },
          {
            id: 'entre',
            text: 'Entre'
          }
        ]
      }),

      m('span.tipo-ate', {
        style: {
          display: ctrl.tempoTotalEstimado().tipo() === 'ate' ? 'inline' : 'none'
        }
      }, [
        m('input.ate-maximo[type="number"]', {
          value: ctrl.tempoTotalEstimado().ateMaximo(),
          onchange: m.withAttr('value', ctrl.tempoTotalEstimado().ateMaximo)
        }),

        selectTipo(ctrl.tempoTotalEstimado().ateTipoMaximo),
      ]),

      m('span.tipo-entre', {
        style: {
          display: ctrl.tempoTotalEstimado().tipo() === 'entre' ? 'inline' : 'none'
        }
      }, [
        m('input.entre-minimo[type="number"]', {
          value: ctrl.tempoTotalEstimado().entreMinimo(),
          onchange: m.withAttr('value', ctrl.tempoTotalEstimado().entreMinimo)
        }),

        m('span', ' e '),

        m('input.entre-maximo[type="number"]', {
          value: ctrl.tempoTotalEstimado().entreMaximo(),
          onchange: m.withAttr('value', ctrl.tempoTotalEstimado().entreMaximo)
        }),

        selectTipo(ctrl.tempoTotalEstimado().entreTipoMaximo)
      ]),

      m('label.titulo.opcional', 'Comentários sobre exceções ou informações adicionais ao tempo estimado'),

      m.component(require('componentes/editor-markdown'), {
        rows: 5,
        oninput: function (e) {
          ctrl.tempoTotalEstimado().descricao(e.target.value);
        },
        value: ctrl.tempoTotalEstimado().descricao(),
        erro: function () {
          return ctrl.tempoTotalEstimado().validador.hasError('descricao');
        }
      })
    ]);
  }
};
