'use strict';

var referencia = require('referencia');
var permissoes = require('utils/permissoes');

function filtraArray(array) {
  return _.filter(array, function (pagina) {
    if (permissoes.podeCriarPagina(pagina.id)) {
      return true;
    }
  });
}

var iconesDeTipo = {
  servico: 'fa-file-text-o',
  orgao: 'fa-building-o'
};

module.exports = {
  view: function (ctrl, args) {
    var tooltipTipo = args.tooltipTipo;
    var componenteTipo = args.novo ? m('', {
      class: 'criar-pagina'
    }, filtraArray(referencia.tiposDePagina).map(function (pagina) {
      return m('a', {
        href: '/editar/' + pagina.id + '/novo',
        class: 'button botao-primario'
      }, [m('span.fa', {
        class: iconesDeTipo[pagina.id] || 'fa-file-o'
      }), 'Criar ' + pagina.text]);
    })) : referencia.tipoDePagina(args.tipo());

    return m('fieldset#tipoDePagina', [
      m('div', [
        m('h3', 'Tipo de Página: '),
        args.novo ? m.component(tooltipTipo) : '',
        componenteTipo
      ])
    ]);
  }
};
