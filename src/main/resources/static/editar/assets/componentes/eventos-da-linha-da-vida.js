var EventosDaLinhaDaVida = {
  controller: function(args) {
    this.servico = args.servico;

    this.eventosDaLinhaDaVida = m.request({ method: 'GET', url: '/editar/api/eventos-da-linha-da-vida' });
    this.adicionar = function(e) {
        var evento = e.target.value;
        var eventos = this.servico.eventosDaLinhaDaVida();

        eventos = _.without(eventos, evento);
        if (e.target.checked) {
            eventos.push(evento);
        }
        this.servico.eventosDaLinhaDaVida(eventos);
    }
  },
  view: function(ctrl) {
    return m('', [
      m("h3", "Eventos da linha da vida"),
      m("", ctrl.eventosDaLinhaDaVida().map(function(evento) {
        return m('label', [
          m("input[type=checkbox]", {
            value: evento,
            checked: _.contains(ctrl.servico.eventosDaLinhaDaVida(), evento),
            onchange: ctrl.adicionar.bind(ctrl)
          }),
          evento
        ]);
      }))
    ]);
  }
};
