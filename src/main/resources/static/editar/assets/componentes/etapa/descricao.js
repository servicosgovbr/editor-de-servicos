var etapa = etapa || {};

etapa.Descricao = {

  controller: function(args) {
    this.descricao = args.descricao;
  },

  view: function(ctrl) {
    return m('.descricao', [
      m('h3', 'Descrição da etapa'),
      m.component(EditorMarkdown, {
        rows: 10,
        oninput: m.withAttr('value', ctrl.descricao),
        value: ctrl.descricao()
      })
    ]);
  }
};