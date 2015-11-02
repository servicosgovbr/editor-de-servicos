'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;
    this.etapas = this.servico.etapas();
    this.converter = new window.showdown.Converter();
  },

  view: function (ctrl) {

    return m('', ctrl.etapas.map(function (etapa, index) {
      return m('.etapas', [
                m('p.circle', index + 1),
                m('h4.etapa', etapa.titulo() ? etapa.titulo() : 'acesse o serviço'),
                m('.etapa markdown', m.trust(ctrl.converter.makeHtml(etapa.descricao()))),
                m.component(m.component(require('servico/visualizar/documentos'), etapa.documentos())),
                m.component(m.component(require('servico/visualizar/custos'), {
                  gratuidade: ctrl.servico.gratuidade(),
                  custos: etapa.custos()
                })),
                m.component(m.component(require('servico/visualizar/canais-de-prestacao'), etapa.canaisDePrestacao()))
            ]);
    }));

  }
};
