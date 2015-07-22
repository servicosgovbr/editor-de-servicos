var PalavrasChave = {
  controller: function(args) {
    this.servico = args.servico;
  },
  view: function(ctrl) {
    return m('', [
      m('h3', 'Palavras-chave'),
      m('input[type=text]', {
        onchange: m.withAttr('value', ctrl.servico.palavrasChave),
        value: ctrl.servico.palavrasChave()
      })
    ]);
  }
};
