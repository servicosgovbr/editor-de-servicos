'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('#dados-complementares', [
      m.component(require('componentes/orgao-responsavel'), {
        orgao: ctrl.servico.orgao
      }),
      m.component(require('componentes/segmentos-da-sociedade'), {
        segmentosDaSociedade: ctrl.servico.segmentosDaSociedade
      }),
      m.component(require('componentes/eventos-da-linha-da-vida'), {
        eventosDaLinhaDaVida: ctrl.servico.eventosDaLinhaDaVida
      }),
      m.component(require('componentes/areas-de-interesse'), {
        areasDeInteresse: ctrl.servico.areasDeInteresse
      }),
      m.component(require('componentes/palavras-chave'), {
        palavrasChave: ctrl.servico.palavrasChave
      }),
      m.component(require('componentes/legislacoes'), {
        legislacoes: ctrl.servico.legislacoes
      })
    ]);
  }
};
