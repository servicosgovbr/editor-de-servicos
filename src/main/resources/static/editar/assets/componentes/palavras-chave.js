var PalavrasChave = {
  controller: function(args) {
    this.palavrasChave = args.palavrasChave;
  },
  view: function(ctrl) {
    return m('', [
      m('h3', 'Palavras-chave'),
      m('input[type=text]', {
        onchange: m.withAttr('value', ctrl.palavrasChave),
        value: ctrl.palavrasChave()
      })
    ]);
  }
};
