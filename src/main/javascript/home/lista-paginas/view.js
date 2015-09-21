'use strict';

//var safeGet = require('utils/code-checks').safeGet;

module.exports = function (ctrl, args) {

  var filtro = args.filtro;

  var paginas = ctrl.paginasFiltradas(filtro);

  var iconesDeTipo = {
    servico: 'fa-file-text-o',
    orgao: 'fa-building-o'
  };

  return m('table', [
     m('tr', [
       m('th[width="40%"]', 'Nome'),
       m('th.center', 'Publicação'),
       m('th.center', 'Edição'),
       m('th.right', '')
     ])
    ].concat(paginas.map(function (s) {

    return m('tr', [

       m('td', m('a', {
        href: '/editar/' + s.conteudo.tipo + '/' + s.id
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
          href: (s.temAlteracoesNaoPublicadas ? '/editar/visualizar/' : '/servico/') + s.id,
          target: '_blank',
          title: 'Visualizar este conteúdo'
        }, m('i.fa.fa-eye')),

         m('button.remover', {
          title: 'Remover este conteúdo',
          onclick: _.bind(ctrl.excluirConteudo, ctrl, s.id)
        }, m('i.fa.fa-trash-o')),
       ])
     ]);
  })));
};
