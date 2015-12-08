/*global loggedUser, location*/
'use strict';

function retornaPermissoes() {
  var arrayOriginal = loggedUser.permissoes.slice(1);

  return arrayOriginal.map(function(permissao) {
    return permissao.authority;
  });
}

var arrayPermissoes = retornaPermissoes();

function possuiPermissao(permissao) {
  return _.contains(arrayPermissoes, permissao);
}

function estaNaPaginaServico() {
  return location.pathname.indexOf('editar/servico') > -1;
}

function estaNaPaginaOrgao() {
  return location.pathname.indexOf('editar/orgao') > -1;
}

function estaNaPaginaTematica() {
  return location.pathname.indexOf('editar/pagina-tematica') > -1;
}

function  podeSalvarServico() {
  return possuiPermissao('editar_salvar SERVICO') && estaNaPaginaServico();
}

function  podeSalvarOrgao() {
  return possuiPermissao('editar_salvar ORGAO (ORGAO ESPECIFICO)') && estaNaPaginaOrgao();
}

function  podeSalvarPaginaTematica() {
  return possuiPermissao('editar_salvar PAGINA_TEMATICA') && estaNaPaginaTematica();
}

function podeSalvarPagina() {
  return  podeSalvarServico() || podeSalvarPaginaTematica() || podeSalvarOrgao();
}

module.exports = {
  podeSalvarPagina: podeSalvarPagina
};
