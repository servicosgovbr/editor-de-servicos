'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.modificarTipo = function (e) {
      this.servico().tempoTotalEstimado().tipo(e.target.value);
    };
  },

  view: function (ctrl) {
    var unidades = [
      m('option[value=""]', 'Selecione…'),
      m('option[value="minutos"]', 'minutos'),
      m('option[value="horas"]', 'horas'),
      m('option[value="dias-corridos"]', 'dias corridos'),
      m('option[value="dias-uteis"]', 'dias úteis'),
      m('option[value="meses"]', 'meses')
    ];

    return m('fieldset#tempo-total-estimado', [
      m('h3', [
        'Tempo total estimado',
        m.component(require('tooltips').tempoTotalEstimado)
      ]),

      m('select', {
        onchange: ctrl.modificarTipo.bind(ctrl),
        value: ctrl.servico().tempoTotalEstimado().tipo()
      }, [
        m('option[value=""]', 'Selecione…'),
        m('option[value="entre"]', 'Entre'),
        m('option[value="ate"]', 'Até')
      ]),

      ' ',

      m('span.tipo-ate', {
        style: {
          display: ctrl.servico().tempoTotalEstimado().tipo() === 'ate' ? 'inline' : 'none'
        }
      }, [
        m('input.ate-maximo[type="text"]', {
          value: ctrl.servico().tempoTotalEstimado().ateMaximo(),
          onchange: m.withAttr('value', ctrl.servico().tempoTotalEstimado().ateMaximo)
        }),
        ' ',
        m('select', {
          onchange: m.withAttr('value', ctrl.servico().tempoTotalEstimado().ateTipoMaximo),
          value: ctrl.servico().tempoTotalEstimado().ateTipoMaximo()
        }, unidades),
      ]),

      m('span.tipo-entre', {
        style: {
          display: ctrl.servico().tempoTotalEstimado().tipo() === 'entre' ? 'inline' : 'none'
        }
      }, [
        m('input.entre-minimo[type="text"]', {
          value: ctrl.servico().tempoTotalEstimado().entreMinimo(),
          onchange: m.withAttr('value', ctrl.servico().tempoTotalEstimado().entreMinimo)
        }),
        m('span', ' e '),
        m('input.entre-maximo[type="text"]', {
          value: ctrl.servico().tempoTotalEstimado().entreMaximo(),
          onchange: m.withAttr('value', ctrl.servico().tempoTotalEstimado().entreMaximo)
        }),
        ' ',
        m('select', {
          onchange: m.withAttr('value', ctrl.servico().tempoTotalEstimado().entreTipoMaximo),
          value: ctrl.servico().tempoTotalEstimado().entreTipoMaximo()
        }, unidades)
      ]),

      m.component(require('componentes/editor-markdown'), {
        rows: 5,
        oninput: function (e) {
          ctrl.servico().tempoTotalEstimado().descricao(e.target.value);
        },
        value: ctrl.servico().tempoTotalEstimado().descricao()
      })
    ]);
  }
};
