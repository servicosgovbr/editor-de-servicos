'use strict';

var referencia = require('referencia');

module.exports = {

  controller: function (args) {
    this.servico = args;
    this.unidades = referencia.unidadesDeTempoVisualizar;
    this.converter = new window.showdown.Converter();

    this.temTempoEntre = function () {
      return this.servico.tempoTotalEstimado().tipo() === 'entre';
    };

    this.temTempoAte = function () {
      return this.servico.tempoTotalEstimado().tipo() === 'ate';
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
                  ' é o tempo estimado para a prestação deste serviço.'
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
                  m('span', ctrl.unidades[ctrl.servico.tempoTotalEstimado().ateTipoMaximo()])
              ]);
      }
      return m.component(require('servico/visualizar/view-vazia'));
    };

    if (ctrl.temTempo()) {
      return m('#servico-tempo', [
              m('h3.subtitulo-servico', 'Quanto tempo leva?'),
              tempoEntreView(),
              tempoAteView(),
              ctrl.servico.tempoTotalEstimado().descricao().length > 0 ? m('br') : '',
              ctrl.servico.tempoTotalEstimado().descricao().length > 0 ? m('h4', 'Informações adicionais ao tempo estimado') : '',
              m.trust(ctrl.converter.makeHtml(ctrl.servico.tempoTotalEstimado().descricao()))
          ]);
    }
    return m.component(require('servico/visualizar/view-vazia'));
  }
};
