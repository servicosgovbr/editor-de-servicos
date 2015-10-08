'use strict';

var referencia = require('referencia');

module.exports = {

  controller: function (args) {
    this.servico = args;
    this.unidades = referencia.unidadesDeTempoVisualizar;

    this.temTempoEntre = function () {
      return (!_.isEmpty(this.servico.tempoTotalEstimado().entreMinimo()) || !_.isEmpty(this.servico.tempoTotalEstimado().entreMaximo()));
    };

    this.temTempoAte = function () {
      return !_.isEmpty(this.servico.tempoTotalEstimado().ateMaximo());
    };

    this.temTempo = function () {
      return this.servico.tempoTotalEstimado() && (this.temTempoAte() || this.temTempoEntre());
    };
  },

  view: function (ctrl) {

    var tempoEntreView = function () {
      if (ctrl.temTempoEntre()) {
        return m('p', [
                  'Entre ',
                  m('span', ctrl.servico.tempoTotalEstimado().entreMinimo()),
                  ' e ',
                  m('span', ctrl.servico.tempoTotalEstimado().entreMaximo()),
                  ' ',
                  m('span', ctrl.unidades[ctrl.servico.tempoTotalEstimado().entreTipoMaximo()]),
                  ' é o tempo estimado para a prestação imediata deste serviço.'
              ]);
      }
      return m.component(require('servico/visualizar/view-vazia'));
    };

    var tempoAteView = function () {
      if (ctrl.temTempoAte()) {
        return m('p', [
                  m('span', 'Até '),
                  m('span', ctrl.servico.tempoTotalEstimado().ateMaximo()),
                  m('span', ' '),
                  m('span', ctrl.servico.tempoTotalEstimado().ateTipoMaximo()),
              ]);
      }
      return m.component(require('servico/visualizar/view-vazia'));
    };

    if (ctrl.temTempo()) {
      return m('#servico-tempo', [
              m('h3.subtitulo-servico', 'Quanto tempo leva?'),
              tempoEntreView(),
                tempoAteView()
          ]);
    }
    return m.component(require('servico/visualizar/view-vazia'));
  }
};
