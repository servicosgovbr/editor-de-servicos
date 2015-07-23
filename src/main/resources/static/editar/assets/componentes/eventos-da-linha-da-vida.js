var EventosDaLinhaDaVida = {

  controller: function(args) {
    this.eventosDaLinhaDaVida = args.eventosDaLinhaDaVida;

    this.todosEventosDaLinhaDaVida = m.request({ method: 'GET', url: '/editar/api/eventos-da-linha-da-vida' });

    this.adicionar = function(e) {
      var evento = e.target.value;
      var eventos = this.eventosDaLinhaDaVida();

      eventos = _.without(eventos, evento);
      if (e.target.checked) {
        eventos.push(evento);
      }

      this.eventosDaLinhaDaVida(eventos);
    }
  },

  view: function(ctrl) {
    return m('fieldset#eventos-da-linha-da-vida', [
      m("h3", "Eventos da linha da vida"),
      m("", ctrl.todosEventosDaLinhaDaVida().map(function(evento) {
        return m('label', [
          m("input[type=checkbox]", {
            value: evento,
            checked: _.contains(ctrl.eventosDaLinhaDaVida(), evento),
            onchange: ctrl.adicionar.bind(ctrl)
          }),
          evento
        ]);
      }))
    ]);
  }
};
