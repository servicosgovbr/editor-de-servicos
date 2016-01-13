'use strict';

var permissoes = require('utils/permissoes');

module.exports = {

  view: function (ctrl, args) {
    function metadados() {
      if (!args.metadados) {
        return [permissoes.podeCriarPagina() ? m('span.novo',
            m('a.button#nova-pagina', {
              href: '/editar/pagina/nova'
            }, [
              m('i.fa.fa-file-text-o'),
              m.trust('Nova página')
          ])
        ) : m(''),

        permissoes.podeCriarUsuario() ? m('span.novo',
            m('a.button#permissoes', {
              href: '/editar/usuarios'
            }, [
                m('i.fa.fa-user-plus'),
                m.trust('Gerenciar permissões')
            ])
          ) : m('')
        ];
      }

      return m.component(require('cabecalho/metadados'), args);
    }

    function logout() {
      if (!args.logout) {
        return m('');
      }
      return m.component(require('cabecalho/logout'));
    }

    var paginasComVoltar = [
    '/editar/pagina/nova',
    '/editar/importar-xml/novo'];

    var setaVoltar = args.metadados || _.contains(paginasComVoltar, m.route()) ? m('i.fa.fa-arrow-left') : '';

    return m('header', [
      m('', m('a[href=/editar]', m('h1', [setaVoltar, ' Editor de Serviços'], m('span.nome-pagina', args.nomeDaPagina)))),
      logout(),
      metadados()
    ]);
  }
};
