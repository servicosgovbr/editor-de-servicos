'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;
  },

  view: function (ctrl) {
    return m('', [
            m('h3#servico-outras-info.subtitulo-servico', 'Outras informações'),
            m('.info-extra', [
                !_.isEmpty(ctrl.servico.nomesPopulares()) ? m('.row',
          m('p', ['Você também pode conhecer este serviço como: ',
                        ctrl.servico.nomesPopulares().join(', '),
                        '.'
                    ])
                ) : m(''),
                m('div', m('p', 'Este serviço é gratuito para o cidadão.'))
            ])

        ]);
  }
};
