var TempoTotalEstimado = {

  controller: function (args) {
    this.tempoTotalEstimado = args.tempoTotalEstimado;

    this.modificarTipo = function(e) {
      this.tempoTotalEstimado.tipo(e.target.value);
    };
  },

  view: function (ctrl) {
    var unidades = [
      m("option[value='']", "Selecione…"),
      m("option[value='minutos']", "minutos"),
      m("option[value='horas']", "horas"),
      m("option[value='dias corridos']", "dias corridos"),
      m("option[value='dias úteis']", "dias úteis"),
      m("option[value='meses']", "meses")
    ];

    return m('fieldset#tempo-total-estimado', [
      m('h3', 'Tempo total estimado'),

      m("select.inline", {
        onchange: ctrl.modificarTipo.bind(ctrl)
      }, [
        m("option[value='']", "Selecione…"),
        m("option[value='entre']", "Entre"),
        m("option[value='até']", "Até")
      ]),

      ' ',

      m('span.ateTipo', {
        style: {
          display: ctrl.tempoTotalEstimado.tipo() == 'até' ? 'inline' : 'none'
        }
      }, [
        m("input.inline[type='text']", {
          value: ctrl.tempoTotalEstimado.ateMaximo(),
          onchange: m.withAttr('value', ctrl.tempoTotalEstimado.ateMaximo)
        }),
        " ",
        m("select.inline", {
          onchange: m.withAttr('value', ctrl.tempoTotalEstimado.ateTipoMaximo)
        }, unidades),
      ]),

      m('span.entreTipo', {
        style: {
          display: ctrl.tempoTotalEstimado.tipo() == 'entre' ? 'inline' : 'none'
        }
      }, [
        m("input.inline[type='text']", {
          value: ctrl.tempoTotalEstimado.entreMinimo(),
          onchange: m.withAttr('value', ctrl.tempoTotalEstimado.entreMinimo)
        }),
        " ",
        m("select.inline", {
          onchange: m.withAttr('value', ctrl.tempoTotalEstimado.entreTipoMinimo)
        }, unidades),
        " e ",
        m("input.inline[type='text']", {
          value: ctrl.tempoTotalEstimado.entreMaximo(),
          onchange: m.withAttr('value', ctrl.tempoTotalEstimado.entreMaximo)
        }),
        " ",
        m("select.inline", {
          onchange: m.withAttr('value', ctrl.tempoTotalEstimado.entreTipoMaximo)
        }, unidades)
      ]),

      m("p", "Comentários sobre o tempo estimado"),

      m.component(EditorMarkdown, {
        rows: 5,
        oninput: m.withAttr('value', ctrl.tempoTotalEstimado.excecoes),
        value: ctrl.tempoTotalEstimado.excecoes()
      })
    ]);
  }
};
