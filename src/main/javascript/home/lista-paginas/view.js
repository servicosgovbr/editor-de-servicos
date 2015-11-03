'use strict';

var referencia = require('referencia');

function isServico(p) {
  return p.conteudo.tipo === 'servico';
}

function excluirView(ctrl, pagina) {
  if (!isServico(pagina)) {
    return '';
  }

  if (pagina.excluindo && pagina.excluindo()) {
    return m('i.fa.fa-spin.fa-spinner');
  }

  return m('button.remover', {
    title: 'Remover este conteúdo',
    onclick: _.bind(ctrl.excluirConteudo, ctrl, pagina.id, pagina.conteudo.tipo, pagina)
  }, m('i.fa.fa-trash-o'));

}

function visualizarView(ctrl, pagina) {
  if (!isServico(pagina)) {
    return '';
  }
  return m('a.visualizar', {
    href: '/editar/visualizar/' + pagina.conteudo.tipo + '/' + pagina.id,
    title: 'Visualizar este conteúdo'
  }, m('i.fa.fa-eye'));
}

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
       m('th.center', 'Órgão'),
       m('th.center', 'Tipo'),
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
      m('td.center', s.nomeOrgao),
      m('td.center', referencia.tipoDePagina(s.conteudo.tipo)),
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
        visualizarView(ctrl, s),
        excluirView(ctrl, s),
      ])
    ]);
  })));
};
