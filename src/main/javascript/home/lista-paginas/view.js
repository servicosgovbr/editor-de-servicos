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

function estaFiltrando(ctrl, args) {
  return args.filtro.filtroOrgaos ||
    args.filtro.filtroPaginasTematicas ||
    args.filtro.filtroServicos ||
    args.filtro.busca.length > 0;
}

module.exports = function (ctrl, args) {
  var filtro = args.filtro;
  var paginas = ctrl.paginasFiltradas(filtro);
  var mostrarCarregando = paginas.length === 0 && !estaFiltrando(ctrl, args);

  var iconesDeTipo = {
    servico: 'fa-file-text-o',
    orgao: 'fa-building-o'
  };

  return m('', mostrarCarregando ? m('div.carregando', m('i.fa.fa-spin.fa-spinner.fa-2x'), 'Carregando...') : '', paginas.length !== 0 ? m('table', [
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

      m('td', m('a.nome-link', {
        href: '/editar/' + s.conteudo.tipo + '/' + s.id
      }, [
        m('span.fa', {
          class: iconesDeTipo[s.conteudo.tipo] || 'fa-file-o'
        }),
        m.trust(' &nbsp; '),
        s.conteudo.nome
      ]), s.temAlteracoesNaoPublicadas ? m('span.mudanca', 'Alterações não publicadas') : ''),
      m('td.center', s.conteudo.nomeOrgao),
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
  }))) : !mostrarCarregando ? m('div.carregando', 'Não foram encontrados resultados para esta pesquisa') : '');
};
