'use strict';

var referencia = require('referencia');

var selectTipo = function (prop, tte) {
  return m.component(require('componentes/select2'), {
    prop: prop,
    data: referencia.unidadesDeTempo,
    tte: tte
  });
};

var selectAteEntre = function (prop, tte) {
  return m.component(require('componentes/select2'), {
    prop: prop,
    tte: tte,
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
  });
};

module.exports = {
  view: function (ctrl, args) {
    var tte = args.servico().tempoTotalEstimado;

    return m('fieldset#tempo-total-estimado', [
      m('h3', [
        'Tempo estimado para realizar esse serviço',
        m.component(require('tooltips').tempoTotalEstimado)
      ]),

      selectAteEntre(tte().tipo, tte()),

      m('span.tipo-ate', {
        style: {
          display: tte().tipo() === 'ate' ? 'inline' : 'none'
        }
      }, [
        m('.input-container.inline.margin-right', {
          class: tte().ateMaximo.erro()
        }, [m('input.ate-maximo[type="number"]', {
          value: tte().ateMaximo(),
          onchange: m.withAttr('value', tte().ateMaximo)
        })]),

        selectTipo(tte().ateTipoMaximo)
      ]),

      m('span.tipo-entre', {
        style: {
          display: tte().tipo() === 'entre' ? 'inline' : 'none'
        }
      }, [
        m('.input-container.inline', {
          class: tte().entreMinimo.erro()
        }, [m('input.entre-minimo[type="number"]', {
          value: tte().entreMinimo(),
          onchange: m.withAttr('value', tte().entreMinimo)
        })]),

        m('span', ' e '),

        m('.input-container.inline.margin-right', {
          class: tte().entreMaximo.erro()
        }, [m('input.entre-maximo[type="number"]', {
          value: tte().entreMaximo(),
          onchange: m.withAttr('value', tte().entreMaximo)
        })]),

        selectTipo(tte().entreTipoMaximo)
      ]),

      m('label.titulo.opcional', 'Comentários sobre exceções ou informações adicionais ao tempo estimado'),

      m.component(require('componentes/editor-markdown'), {
        rows: 3,
        onchange: m.withAttr('value', tte().descricao),
        value: tte().descricao(),
        erro: tte().descricao.erro()
      })
    ]);
  }
};
