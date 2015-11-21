'use strict';

module.exports = {

  controller: function (args) {
    this.documentos = args;

    this.temDocumento = function () {
      return !_.isEmpty(this.documentos.casoPadrao().campos()) || !_.isEmpty(this.documentos.outrosCasos());
    };
  },

  view: function (ctrl) {

    var camposView = function (campos) {
      return campos.map(function (campo) {
        return m('li', m('span', campo.descricao()));
      });
    };

    var casoPadraoView = function (cp) {
      return m('ul', camposView(cp.campos()));
    };

    var outrosCasosView = function (casos) {
      return casos.map(function (caso) {
        return m('ul.caso-descricao', [
                    m('.info-etapa', caso.descricao()),
                    camposView(caso.campos())
                ]);
      });
    };

    if (ctrl.temDocumento()) {
      return m('.subtitulo-etapa', [
                m('p.titulo-documento', 'Documentação'),
                m('p.info-etapa', ''),
                casoPadraoView(ctrl.documentos.casoPadrao()),
                outrosCasosView(ctrl.documentos.outrosCasos())
            ]);
    }
    return m.component(require('servico/visualizar/view-vazia'));
  }
};
