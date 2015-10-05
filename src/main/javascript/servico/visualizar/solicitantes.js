'use strict';

module.exports = {

    controller: function (args) {
      this.servico = args;
      this.converter = new window.showdown.Converter();
    },

    view: function (ctrl) {
      if (_.isEmpty(ctrl.servico.solicitantes())) {
        return m('');
      } else {
        return m('#servico-solicitantes', [
                m('h3.subtitulo-servico', 'Quem pode utilizar este serviço?'),
                ctrl.servico.solicitantes().map(function (s) {
                return m('.solicitantes markdown margem-solicitantes', [
                        m('h4', m.trust(s.tipo()),
                      m.trust(ctrl.converter.makeHtml(s.requisitos()))
                      ]);
                })


              ]);
        }
      }
    };
