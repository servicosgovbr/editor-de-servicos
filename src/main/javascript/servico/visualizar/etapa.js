'use strict';

var referencia = require('referencia');

module.exports = {

  controller: function (args) {
    this.etapas = args;
    this.converter = new window.showdown.Converter();
    this.tipos = referencia.tiposDeCanalDePrestacaoVisualizar;
  },

  view: function (ctrl) {

    var canalPadrao = function (cp) {
      return m('ul', cp.campos().map(function (campo) {
        return m('li', [
                  m('span', ctrl.tipos[campo.tipo()].text + ': '),
                  ctrl.tipos[campo.tipo()].destacado ?
                      m('a', {
            href: campo.descricao()
          }, ctrl.tipos[campo.tipo()].descricaoLink) :
                      m('', m.trust(ctrl.converter.makeHtml(campo.descricao())))
              ]);
      }));
    };

    var outrosCanais = function (casos) {
      return casos.map(function (caso) {
        return m('ul', [
                m('.info-etapa', caso.descricao()),
                caso.campos().map(function (campo) {
            return m('li', [
                        m('span', ctrl.tipos[campo.tipo()].text + ': '),
                        ctrl.tipos[campo.tipo()].destacado ?
                            m('a', {
                href: campo.descricao()
              }, ctrl.tipos[campo.tipo()].descricaoLink) :
                            m('', m.trust(ctrl.converter.makeHtml(campo.descricao())))
                    ]);
          })
            ]);
      });
    };

    var canaisDePrestacao = function (etapa) {
      if (!_.isEmpty(etapa.canaisDePrestacao())) {
        return m('.subtitulo-etapa', [
                m('p.titulo-documento', 'Canais de prestação'),
                m('p.info-etapa', 'Canais de prestação padrão'),
                canalPadrao(etapa.canaisDePrestacao().casoPadrao()),
                outrosCanais(etapa.canaisDePrestacao().outrosCasos())
            ]);
      }
      return m.component(require('servico/visualizar/view-vazia'));
    };

    return m('', ctrl.etapas.map(function (etapa, index) {
      return m('.etapas', [
                m('p.circle', index + 1),
                m('h4.etapa', etapa.titulo() ? etapa.titulo() : 'acesse o serviço'),
                m('.etapa markdown', m.trust(ctrl.converter.makeHtml(etapa.descricao()))),
                m.component(m.component(require('servico/visualizar/documentos'), etapa.documentos())),
                m.component(m.component(require('servico/visualizar/custos'), etapa.custos())),
                canaisDePrestacao(etapa),
            ]);
    }));

  }
};
