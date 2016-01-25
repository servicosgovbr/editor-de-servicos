'use strict';

var referencia = require('referencia');
var Gratuidade = require('servico/modelos').Gratuidade;

module.exports = {

  controller: function (args) {
    this.servico = args;
    this.nomeOrgao = m.prop('');
    this.converter = new window.showdown.Converter();

    this.obterOrgao = function (orgao) {
      m.request({
        method: 'GET',
        url: '/editar/api/orgao',
        data: {
          urlOrgao: orgao
        },
        deserialize: function (data) {
          return data;
        }

      }).then(_.bind(function (nome) {
        this.nomeOrgao(nome);
      }, this));
    };

    this.ehFeminino = function () {
      return _.includes(referencia.orgaosFemininos, _.chain(this.nomeOrgao().toLowerCase()).words().first().value());
    };

    this.obterOrgao(this.servico.orgao().nome());
  },

  view: function (ctrl) {
    var gratuidadeView = function (ehGratuito) {
      if (ehGratuito) {
        return m('', m('p', 'Este serviço é gratuito para o cidadão.'));
      }
      return '';
    };

    return m('', [
            m('h3#servico-outras-info.subtitulo-servico', 'Outras informações'),
            m('.info-extra', gratuidadeView(ctrl.servico.gratuidade() === Gratuidade.GRATUITO)),
            m('.row', m('p.separacao-orgao', [
                'Este é um serviço ',
                ctrl.ehFeminino() ? 'da ' : 'do ',
                m('a', ctrl.nomeOrgao()),
                '. Em caso de dúvidas, reclamações ou sugestões favor contactá-',
                ctrl.ehFeminino() ? 'la.' : 'lo.'
            ]),
        m('p.orgao-contato.markdown',  [!_.isEmpty(ctrl.servico.orgao().contato()) ?
                'Para mais informações ou dúvidas sobre este serviço, entre em contato: ': '' ,
                m.trust(ctrl.converter.makeHtml(ctrl.servico.orgao().contato()))
            ])),
            m('hr', {
        style: {
          'margin-top': '20px',
          border: 'none'
        }
      })
    ]);
  }
};
