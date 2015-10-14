'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;
  },

  view: function (ctrl) {
    return m('.ancoras', [m('ul', [
            m('li', [m('a', 'O que é?')]),
            !_.isEmpty(ctrl.servico.solicitantes()) ? m('li', [m('a', 'Quem pode utilizar este serviço?')]) : m(''),
            !_.isEmpty(ctrl.servico.etapas()) ? m('li', [m('a', 'Etapas para a realização desde serviço')]) : m(''),
            !_.isUndefined(ctrl.servico.tempoTotalEstimado()) && (!_.isUndefined(ctrl.servico.tempoTotalEstimado().ate) || !_.isUndefined(ctrl.servico.tempoTotalEstimado().entre)) ? m('li', [m('a', 'Quanto tempo leva?')]) : m(''),
            !_.isEmpty(ctrl.servico.legislacoes()) ? m('li', [m('a', 'Legislação')]) : m(''),
            m('li', [m('a', 'Outras informações')])
        ])]);
  }
};
