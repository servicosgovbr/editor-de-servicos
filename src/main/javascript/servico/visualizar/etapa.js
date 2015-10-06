'use strict';

module.exports = {

  controller: function (args) {
    this.etapas = args;
    this.converter = new window.showdown.Converter();
  },

  view: function (ctrl) {
    var documentos = function (etapa) {
      if (!_.isEmpty(etapa.documentos())) {
        window.console.log(etapa.documentos());
        return m('.subtitulo-etapa', [
                m('p.titulo-documento', 'Documentação'),
                m('p.info-etapa', 'Documentação necessária'),
                m('ul', etapa.documentos().casoPadrao().campos().map(function (campo) {
            return m('li', m('span', campo.descricao()));
          }))]);
      }
      return m.component(require('servico/visualizar/view-vazia'));
    };

    return m('', ctrl.etapas.map(function (etapa, index) {
      return m('.etapas', [
                m('p.circle', index + 1),
                m('h4.etapa', etapa.titulo() ? etapa.titulo() : 'Acesse o serviço'),
                m('.etapa markdown', m.trust(ctrl.converter.makeHtml(etapa.descricao()))),
                documentos(etapa)
            ]);
    }));

  }
};
