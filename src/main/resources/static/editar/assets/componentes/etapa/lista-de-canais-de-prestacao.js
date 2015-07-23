var etapa = etapa || {};

etapa.ListaDeCanaisDePrestacao = {

  controller: function(args) {
    this.canaisDePrestacao = args.campos;

    this.tiposDeCanalDePrestacao = m.request({ method: 'GET', url: '/editar/api/tipos-de-canal-de-prestacao' });

    this.adicionar = function() {
      this.canaisDePrestacao().push(new models.CanalDePrestacao());
    };

    this.remover = function(i) {
      this.canaisDePrestacao().splice(i, 1);
    };
  },

  view: function(ctrl) {
    return m('.canais-de-prestacao', [
      ctrl.canaisDePrestacao().map(function(canalDePrestacao, i) {
        return m('.canal-de-prestacao', [
          m('select', {
            onchange: m.withAttr('value', ctrl.canaisDePrestacao()[i].tipo)
          }, [m('option', {value: ''}, 'Selecione...')].concat(ctrl.tiposDeCanalDePrestacao().map(function(tipoCanal){
            return m('option', {
              value: tipoCanal,
              selected: ctrl.canaisDePrestacao()[i].tipo() == tipoCanal
            }, tipoCanal)
          }))),
          ' ',
          m('input.inline.inline-lg[type=text]', {
            value: canalDePrestacao.descricao(),
            onchange: m.withAttr('value', canalDePrestacao.descricao)
          }),
          ' ',
          m('button.inline.remove-peq', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m("span.fa.fa-times")
          ])
        ]);
      }),
      m('button.adicionar-canal-de-prestacao', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m("i.fa.fa-plus"),
        " Adicionar canal de prestação "
      ])
    ]);
  }
};
