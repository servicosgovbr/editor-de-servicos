'use strict';

module.exports = {

  view: function (ctrl, args) {
    function metadados() {
      if (!args.metadados) {
        return [m('span.novo',
            m('a.button#nova-pagina', {
              href: '/editar/pagina/nova'
            }, [
              m('i.fa.fa-file-text-o'),
              m.trust('Nova página')
          ])
        ),

        m('span.novo',
            m('a.button#permissoes', {
              href: '/editar/servico/novo'
            }, [
              m('i.fa.fa-user-plus'),
              m.trust('Gerenciar permissões')
          ])
        )];
      }

      return m.component(require('cabecalho/metadados'), args);
    }

    function logout() {
      if (!args.logout) {
        return m('');
      }
      return m.component(require('cabecalho/logout'));
    }

    var setaVoltar = args.metadados ? m('i.fa.fa-arrow-left') : '';

    return m('header', [
      m('', m('a[href=/editar]', m('h1', [setaVoltar, ' Editor de Serviços']))),
      logout(),
      metadados()
    ]);
  }
};
