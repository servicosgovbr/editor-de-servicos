var Nome = {

  controller: function(args) {
    this.nome = args.nome;
  },

  view: function(ctrl) {
    return m('#nome', [
      m('h3', 'Nome do serviço'),
      m('input[type=text]', {
        onchange: m.withAttr('value', ctrl.nome),
        value: ctrl.nome()
      })
    ]);
  }

};