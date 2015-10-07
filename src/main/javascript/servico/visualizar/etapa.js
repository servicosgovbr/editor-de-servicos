'use strict';

module.exports = {

  controller: function (args) {
    this.etapas = args;
    this.converter = new window.showdown.Converter();
  },

  view: function (ctrl) {
    var casoPadrao = function (cp) {
      return m('ul', cp.campos().map(function (campo) {
        return m('li', m('span', campo.descricao()));
      }));
    };

    var outrosCasos = function (casos) {
      return casos.map(function (caso) {
        return m('ul.caso-descricao', [
                m('.info-etapa', caso.descricao()),
                caso.campos().map(function (c) {
            return m('li', m('span', c.descricao()));
          })
            ]);
      });
    };

    var documentos = function (etapa) {
      if (!_.isEmpty(etapa.documentos())) {
        return m('.subtitulo-etapa', [
                m('p.titulo-documento', 'Documentação'),
                m('p.info-etapa', 'Documentação necessária'),
                casoPadrao(etapa.documentos().casoPadrao()),
                outrosCasos(etapa.documentos().outrosCasos())
        ]);
      }
      return m.component(require('servico/visualizar/view-vazia'));
    };

    return m('', ctrl.etapas.map(function (etapa, index) {
      return m('.etapas', [
                m('p.circle', index + 1),
                m('h4.etapa', etapa.titulo() ? etapa.titulo() : 'acesse o serviço'),
                m('.etapa markdown', m.trust(ctrl.converter.makeHtml(etapa.descricao()))),
                documentos(etapa)
            ]);
    }));

  }
};
