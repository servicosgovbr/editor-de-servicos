/*global loggedUser, console*/
'use strict';

function retornaPermissoes() {
  var arrayOriginal = loggedUser.permissoes.slice(1);

  return arrayOriginal.map(function(permissao) {
    return permissao.authority;
  });
}

var arrayPermissoes = retornaPermissoes();

function possuiPermissao(permissao) {
  console.log(arrayPermissoes);
  console.log(permissao);
  return _.contains(arrayPermissoes, permissao);
}

function podeAlterarPaginasTematicas() {
  return possuiPermissao('criar PAGINA_TEMATICA') &&
    possuiPermissao('editar_salvar PAGINA_TEMATICA') &&
    possuiPermissao('publicar PAGINA_TEMATICA') &&
    possuiPermissao('descartar PAGINA_TEMATICA') &&
    possuiPermissao('despublicar PAGINA_TEMATICA') &&
    possuiPermissao('excluir PAGINA_TEMATICA');
}

function podePublicarOrgaoServico() {
  return possuiPermissao('publicar');
}

module.exports = {
  podeAlterarPaginasTematicas: podeAlterarPaginasTematicas,
  podePublicarOrgaoServico: podePublicarOrgaoServico
};
