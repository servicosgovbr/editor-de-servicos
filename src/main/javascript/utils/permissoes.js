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
  return possuiPermissao('criar PAGINA-TEMATICA') &&
    possuiPermissao('editar_salvar PAGINA-TEMATICA') &&
    possuiPermissao('publicar PAGINA-TEMATICA') &&
    possuiPermissao('descartar PAGINA-TEMATICA') &&
    possuiPermissao('despublicar PAGINA-TEMATICA') &&
    possuiPermissao('excluir PAGINA-TEMATICA');
}

function podePublicarOrgaoServico() {
  return possuiPermissao('publicar');
}

module.exports = {
  podeAlterarPaginasTematicas: podeAlterarPaginasTematicas,
  podePublicarOrgaoServico: podePublicarOrgaoServico
};
