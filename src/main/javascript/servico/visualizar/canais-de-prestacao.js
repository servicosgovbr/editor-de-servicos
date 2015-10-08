'use strict';

var referencia = require('referencia');

module.exports = {

  controller: function (args) {
    this.canaisDePrestacao = args;
    this.tipos = referencia.tiposDeCanalDePrestacaoVisualizar;
    this.converter = new window.showdown.Converter();

    this.temCanalDePrestacao = function() {
        return !_.isEmpty(this.canaisDePrestacao.casoPadrao().campos()) || !_.isEmpty(this.canaisDePrestacao.outrosCasos());
    };
  },

  view: function (ctrl) {

    var descricaoView = function (campo) {
      if (ctrl.tipos[campo.tipo()].destacado) {
        return m('a', {
          href: campo.descricao()
        }, ctrl.tipos[campo.tipo()].descricaoLink);
      }
      return m('', m.trust(ctrl.converter.makeHtml(campo.descricao())));
    };

    var camposView = function (campos) {
      return campos.map(function (campo) {
        return m('li', [
                    m('span', ctrl.tipos[campo.tipo()].text + ': '),
                    descricaoView(campo)
                ]);
      });
    };

    var canalPadraoView = function (cp) {
      return m('ul', camposView(cp.campos()));
    };

    var outrosCanaisView = function (casos) {
      return casos.map(function (caso) {
        return m('ul', [
                    m('.info-etapa', caso.descricao()),
                    camposView(caso.campos())
                ]);
      });
    };

    if (ctrl.temCanalDePrestacao()) {
      return m('.subtitulo-etapa', [
                    m('p.titulo-documento', 'Canais de prestação'),
                    m('p.info-etapa', 'Canais de prestação padrão'),
                    canalPadraoView(ctrl.canaisDePrestacao.casoPadrao()),
                    outrosCanaisView(ctrl.canaisDePrestacao.outrosCasos())
                ]);
    }
    return m.component(require('servico/visualizar/view-vazia'));

  }
};
