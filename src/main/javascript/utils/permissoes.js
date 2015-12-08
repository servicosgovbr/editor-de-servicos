/*global loggedUser, location*/
'use strict';

function retornaPermissoes() {
  var arrayOriginal = loggedUser.permissoes.slice(1);

  return arrayOriginal.map(function(permissao) {
    return permissao.authority;
  });
}

var arrayPermissoes = retornaPermissoes();

//funções apoio
function possuiPermissao(permissao) {
  return _.contains(arrayPermissoes, permissao);
}

//funções checa URL
function estaNaPaginaServico() {
  return location.pathname.indexOf('editar/servico') > -1;
}

function estaNaPaginaOrgao() {
  return location.pathname.indexOf('editar/orgao') > -1;
}

function estaNaPaginaTematica() {
  return location.pathname.indexOf('editar/pagina-tematica') > -1;
}

//serviço
function possuiPermissaoPaginaServico(permissao) {
  return possuiPermissao(permissao) && estaNaPaginaServico();
}

function possuiPermissaoPaginaServicoOrgaoEspecifico(permissao, orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissao(permissao) && estaNaPaginaServico() && orgaoIdUsuario === orgaoIdPagina;
}

//orgão
function possuiPermissaoPaginaOrgao(permissao, orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissao(permissao) && estaNaPaginaOrgao() && orgaoIdUsuario === orgaoIdPagina;
}

//página temática
function possuiPermissaoPaginaTematica(permissao) {
  return possuiPermissao(permissao) && estaNaPaginaTematica();
}

//funções genéricas
function podeSalvarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return  possuiPermissaoPaginaServico('editar_salvar SERVICO') || 
          possuiPermissaoPaginaTematica('editar_salvar PAGINA_TEMATICA') || 
          possuiPermissaoPaginaOrgao('editar_salvar ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podeCriarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServico('criar SERVICO') || 
         possuiPermissaoPaginaTematica('criar PAGINA-TEMATICA') || 
         possuiPermissaoPaginaOrgao('criar ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podePublicarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('publicar SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) || 
         possuiPermissaoPaginaTematica('publicar PAGINA_TEMATICA') || 
         possuiPermissaoPaginaOrgao('publicar ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podeDescartarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('descartar SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) || 
         possuiPermissaoPaginaTematica('descartar PAGINA_TEMATICA') || 
         possuiPermissaoPaginaOrgao('descartar ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podeDespublicarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('despublicar SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) || 
         possuiPermissaoPaginaTematica('despublicar PAGINA_TEMATICA') || 
         possuiPermissaoPaginaOrgao('despublicar ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podeExcluirPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('excluir SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) || 
         possuiPermissaoPaginaTematica('excluir PAGINA_TEMATICA') || 
         possuiPermissaoPaginaOrgao('excluir ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

module.exports = {
  podeSalvarPagina: podeSalvarPagina,
  podeCriarPagina: podeCriarPagina,
  podePublicarPagina: podePublicarPagina,
  podeDespublicarPagina: podeDespublicarPagina,
  podeDescartarPagina: podeDescartarPagina,
  podeExcluirPagina: podeExcluirPagina
};
