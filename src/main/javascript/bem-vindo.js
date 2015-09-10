'use strict';

var slugify = require('slugify');
var erro = require('utils/erro-ajax');

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

    this.publicarConteudo = _.noop;

    this.excluirConteudo = function (id) {
      m.request({
        method: 'DELETE',
        url: '/editar/api/servico/' + slugify(id),
      }).then(this.listarConteudos, erro);
    };

    this.listarConteudos();
  },

  view: function (ctrl) {
    moment.locale('pt-br');

    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('componentes/cabecalho')),

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
              m.component(require('componentes/select2'), {
                ajax: {
                  url: '/editar/api/orgaos',
                  dataType: 'json',
                  delay: 250,
                  data: function (params) {
                    return {
                      q: params.term
                    };
                  },
                  processResults: function (data, page) {
                    var result = _.map(data, function (o) {
                      return {
                        id: o.id,
                        text: o.nome
                      };
                    });
                    return {
                      results: result
                    };
                  },
                  cache: true
                },
                prop: ctrl.orgao,
                placeholder: 'Filtrar por órgão...',
                width: '100%',
                allowClear: true,
                minimumResultsForSearch: 1,
                minimumInputLength: 3,
                initSelection: function (element, callback) {
                  m.request({
                    method: 'GET',
                    url: '/editar/api/orgao',
                    data: {
                      urlOrgao: ctrl.orgao()
                    },
                    deserialize: function (data) {
                      return data;
                    }
                  }).then(function (orgao) {
                    callback({
                      id: ctrl.orgao(),
                      text: orgao
                    });
                  }, erro);
                }
              })
            )
          ]),

          m('label', [
            m('input[type=checkbox]', {
              onchange: m.withAttr('checked', ctrl.filtrarPublicados)
            }),
            'Somente com alterações não publicadas'
          ]),

          m('table', [
            m('tr', [
              m('th[width="40%"]', 'Nome'),
              m('th.center', 'Publicação'),
              m('th.center', 'Edição'),
              m('th.right', '')
            ])
          ].concat(ctrl.servicosFiltrados().map(function (s) {
            var iconesDeTipo = {
              servico: 'fa-file-text-o',
              orgao: 'fa-building-o'
            };

            return m('tr', [

              m('td', m('a', {
                href: '/editar/' + s.conteudo.tipo + '/' + slugify(s.id)
              }, [
                m('span.fa', {
                  class: iconesDeTipo[s.conteudo.tipo] || 'fa-file-o'
                }),
                m.trust(' &nbsp; '),
                s.conteudo.nome
              ])),

              m('td.center', s.publicado ? [
                moment(s.publicado.horario).fromNow(),
                ', por ',
                s.publicado.autor.split('@')[0]
              ] : '—'),

              m('td.center', s.editado ? [
                moment(s.editado.horario).fromNow(),
                ', por ',
                s.editado.autor.split('@')[0]
              ] : '—'),

              m('td.right', [

                s.temAlteracoesNaoPublicadas ? m('button.publicar', {
                  onclick: _.bind(ctrl.publicarConteudo, ctrl, s.id),
                  title: 'Publicar alterações deste conteúdo'
                }, m('i.fa.fa-paper-plane')) : null,

                m('a.visualizar', {
                  href: '/editar/visualizar/' + slugify(s.id),
                  title: 'Visualizar este conteúdo'
                }, m('i.fa.fa-eye')),

                m('button.remover', {
                  title: 'Remover este conteúdo',
                  onclick: _.bind(ctrl.excluirConteudo, ctrl, s.id)
                }, m('i.fa.fa-trash-o')),

              ])
            ]);
          })))
        ])
      ])
    ]);
  }
};
