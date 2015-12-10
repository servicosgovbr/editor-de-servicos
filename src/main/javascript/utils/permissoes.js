/*global loggedUser, location*/
'use strict';

function retornaPermissoes() {
  var arrayOriginal = loggedUser.permissoes.slice(1);

  return arrayOriginal.map(function(permissao) {
    return permissao.authority;
  });
}

var arrayPermissoes = [];

function formataUrl(url) {
  if (url !== undefined) {
    return url.replace(/:\/\//g, '-').replace(/\./g, '-').replace(/\//g, '-');
  } else {
    return url;
  }
}

function possuiPermissao(permissao) {
  arrayPermissoes = retornaPermissoes();
  return _.contains(arrayPermissoes, permissao);
}

function possuiPermissaoOrgaoEspecifico(permissao, orgaoIdUsuario, orgaoIdPagina) {
  arrayPermissoes = retornaPermissoes();
  return _.contains(arrayPermissoes, permissao) && (formataUrl(orgaoIdUsuario) === formataUrl(orgaoIdPagina));
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
  return possuiPermissao(permissao) && estaNaPaginaServico() && (formataUrl(orgaoIdUsuario) === formataUrl(orgaoIdPagina));
}

function possuiPermissaoPaginaTematica(permissao) {
  return possuiPermissao(permissao) && estaNaPaginaTematica();
}

function possuiPermissaoPaginaOrgao(permissao, orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissao(permissao) && estaNaPaginaOrgao() && (formataUrl(orgaoIdUsuario) === formataUrl(orgaoIdPagina));
}

function podeSalvarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return  possuiPermissaoPaginaServico('EDITAR_SALVAR SERVICO') || 
          possuiPermissaoPaginaTematica('EDITAR_SALVAR PAGINA-TEMATICA') || 
          possuiPermissaoPaginaOrgao('EDITAR_SALVAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podeMostrarPaginaLista(tipo, orgaoIdUsuario, orgaoIdPagina) {
  var returnValue = false;

  if (tipo === 'servico') { returnValue = possuiPermissao('EDITAR_SALVAR SERVICO'); }
  if (tipo === 'pagina-tematica') { returnValue = possuiPermissaoOrgaoEspecifico('EDITAR_SALVAR PAGINA-TEMATICA'); }
  if (tipo === 'orgao') { returnValue = possuiPermissaoOrgaoEspecifico('EDITAR_SALVAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina); }
  
  return returnValue;
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

function podeExcluirPagina(orgaoIdUsuario, modelo) {
  var orgaoIdPagina;

  if (modelo.conteudo.tipo === 'servico') {
    orgaoIdPagina = modelo.conteudo.orgaoId;
  }

  if (modelo.conteudo.tipo === 'orgao') {
    orgaoIdPagina = modelo.id;
  }

  return possuiPermissaoOrgaoEspecifico('EXCLUIR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) || 
         possuiPermissao('EXCLUIR PAGINA-TEMATICA') || 
         possuiPermissaoOrgaoEspecifico('EXCLUIR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podePublicarDascartarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return podePublicarPagina(orgaoIdUsuario, orgaoIdPagina) &&
         podeDescartarPagina(orgaoIdUsuario, orgaoIdPagina);
}

module.exports = {
  podeSalvarPagina: podeSalvarPagina,
  podeCriarPagina: podeCriarPagina,
  podePublicarDascartarPagina: podePublicarDascartarPagina,
  podeDespublicarPagina: podeDespublicarPagina,
  podeExcluirPagina: podeExcluirPagina,
  podeMostrarPaginaLista: podeMostrarPaginaLista
};
