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

function possuiPermissaoPaginaServico(permissao) {
  return possuiPermissao(permissao) && estaNaPaginaServico();
}

function possuiPermissaoPaginaServicoOrgaoEspecifico(permissao, orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissao(permissao) && estaNaPaginaServico() && (orgaoIdUsuario === orgaoIdPagina);
}

function possuiPermissaoPaginaTematica(permissao) {
  return possuiPermissao(permissao) && estaNaPaginaTematica();
}

function possuiPermissaoPaginaOrgao(permissao, orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissao(permissao) && estaNaPaginaOrgao() && (orgaoIdUsuario === orgaoIdPagina);
}

function podeSalvarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return  possuiPermissaoPaginaServico('EDITAR_SALVAR SERVICO') || 
          possuiPermissaoPaginaTematica('EDITAR_SALVAR PAGINA-TEMATICA') || 
          possuiPermissaoPaginaOrgao('EDITAR_SALVAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podeCriarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServico('CRIAR SERVICO') || 
         possuiPermissaoPaginaTematica('CRIAR PAGINA-TEMATICA') || 
         possuiPermissaoPaginaOrgao('CRIAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podePublicarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('PUBLICAR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) || 
         possuiPermissaoPaginaTematica('PUBLICAR PAGINA-TEMATICA') || 
         possuiPermissaoPaginaOrgao('PUBLICAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podeDescartarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('DESCARTAR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) || 
         possuiPermissaoPaginaTematica('DESCARTAR PAGINA-TEMATICA') || 
         possuiPermissaoPaginaOrgao('DESCARTAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podeDespublicarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('DESPUBLICAR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) || 
         possuiPermissaoPaginaTematica('DESPUBLICAR PAGINA-TEMATICA') || 
         possuiPermissaoPaginaOrgao('DESPUBLICAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podeExcluirPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('EXCLUIR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) || 
         possuiPermissaoPaginaTematica('EXCLUIR PAGINA-TEMATICA') || 
         possuiPermissaoPaginaOrgao('EXCLUIR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

module.exports = {
  podeSalvarPagina: podeSalvarPagina,
  podeCriarPagina: podeCriarPagina,
  podePublicarPagina: podePublicarPagina,
  podeDespublicarPagina: podeDespublicarPagina,
  podeDescartarPagina: podeDescartarPagina,
  podeExcluirPagina: podeExcluirPagina
};
