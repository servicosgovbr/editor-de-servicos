'use strict';

var erro = require('utils/erro-ajax');

var ListaPaginas = require('home/lista-paginas/componente');

module.exports = {
  controller: function (args) {
    this.filtro = m.prop('');
    this.filtrarPublicados = m.prop();

    this.servicos = m.prop([]);
    this.orgao = m.prop('');

    this.servicosFiltrados = function () {
      var servicos = this.servicos();

      if (this.filtrarPublicados() === true) {
        servicos = this.servicos().filter(function (s) {
          return s.temAlteracoesNaoPublicadas;
        });
      }

      if (!!this.orgao()) {
        servicos = this.servicos().filter(_.bind(function (s) {
          return this.orgao() === (s.conteudo && s.conteudo.orgao && s.conteudo.orgao.id);
        }, this));
      }

      if (_.isEmpty(_.trim(this.filtro()))) {
        return _.sortBy(servicos, 'id');
      }

      servicos = new Fuse(servicos, {
        keys: ['conteudo.nome']
      }).search(this.filtro());

      return servicos;
    };

    this.listarConteudos = _.debounce(function () {
      m.request({
          method: 'GET',
          url: '/editar/api/conteudos'
        })
        .then(this.servicos, erro);
    }.bind(this), 500);

    this.listarConteudos();
  },

  view: function (ctrl) {
    moment.locale('pt-br');

    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('cabecalho/cabecalho'), {
          metadados: false,
          logout: true
        }),

        m('#bem-vindo', [
          m('h2', 'Bem-vindo!'),

          m('div.busca', [
            m('input[type=search][placeholder="Filtrar por..."]', {
              oninput: m.withAttr('value', ctrl.filtro)
            }),

            m('button#novo',
              m('a', {
                href: '/editar/servico/novo'
              }, [
                m('i.fa.fa-file-o'),
                m.trust('&nbsp; Novo Serviço')
              ])
            ),

            m('div#orgaos',
              m.component(require('orgao/select-orgao'), {
                orgao: ctrl.orgao
              })
            )
          ]),

          m('label', [
            m('input[type=checkbox]', {
              onchange: m.withAttr('checked', ctrl.filtrarPublicados)
            }),
            'Somente com alterações não publicadas'
          ]),

          m.component(ListaPaginas, {
            paginas: ctrl.servicos()
          })
        ])
      ])
    ]);
  }
};
