var etapa = etapa || {};

etapa.ListaDeCanaisDePrestacao = {

  controller: function(args) {
    this.canaisDePrestacao = args.campos;

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
          m('select', [ m('option', 'Selecione...')]),
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
