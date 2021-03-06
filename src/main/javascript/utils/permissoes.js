'use strict';

function retornaPermissoes() {
  if (window.loggedUser.permissoes !== undefined) {
    var arrayOriginal = window.loggedUser.permissoes.slice(1);

    return arrayOriginal.map(function (permissao) {
      return permissao.authority;
    });
  } else {
    return [];
  }
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
  return m.route().indexOf('editar/servico') > -1;
}

function estaNaPaginaOrgao() {
  return m.route().indexOf('editar/orgao') > -1;
}

function estaNaPaginaTematica() {
  return m.route().indexOf('editar/pagina-tematica') > -1;
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

function possuiPermissaoPaginaOrgao(permissao) {
  return possuiPermissao(permissao) && estaNaPaginaOrgao();
}

function possuiPermissaoPaginaOrgaoOrgaoEspecifico(permissao, orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissao(permissao) && estaNaPaginaOrgao() && (formataUrl(orgaoIdUsuario) === formataUrl(orgaoIdPagina));
}

function podeSalvarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return !orgaoIdPagina ||
    possuiPermissaoPaginaServico('EDITAR_SALVAR SERVICO') ||
    possuiPermissaoPaginaServicoOrgaoEspecifico('EDITAR_SALVAR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) ||
    possuiPermissaoPaginaTematica('EDITAR_SALVAR PAGINA-TEMATICA') ||
    possuiPermissaoPaginaOrgao('EDITAR_SALVAR ORGAO') ||
    possuiPermissaoPaginaOrgaoOrgaoEspecifico('EDITAR_SALVAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
}

function podeMostrarPaginaLista(tipo, orgaoIdUsuario, orgaoIdPagina) {
  var returnValue = false;

  if (tipo === 'servico') {
    returnValue = possuiPermissao('EDITAR_SALVAR SERVICO') ||
      possuiPermissaoOrgaoEspecifico('EDITAR_SALVAR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina);
  }

  if (tipo === 'pagina-tematica') {
    returnValue = possuiPermissaoOrgaoEspecifico('EDITAR_SALVAR PAGINA-TEMATICA');
  }

  if (tipo === 'orgao') {
    returnValue = possuiPermissaoOrgaoEspecifico('EDITAR_SALVAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) ||
      possuiPermissao('EDITAR_SALVAR ORGAO');
  }

  return returnValue;
}

function podeCriarPagina(tipoPagina) {
  if (tipoPagina === 'servico') {
    return possuiPermissao('CRIAR SERVICO') ||
      possuiPermissao('CRIAR SERVICO (ORGAO ESPECIFICO)');
  }

  if (tipoPagina === 'orgao') {
    return possuiPermissao('CRIAR ORGAO (ORGAO ESPECIFICO)') ||
      possuiPermissao('CRIAR ORGAO');
  }

  if (tipoPagina === 'pagina-tematica') {
    return possuiPermissao('CRIAR PAGINA-TEMATICA');
  }

  if (tipoPagina === 'importar-xml') {
    return possuiPermissao('CRIAR SERVICO') ||
      possuiPermissao('CRIAR SERVICO (ORGAO ESPECIFICO)');
  }

  return possuiPermissao('CRIAR SERVICO') ||
    possuiPermissao('CRIAR SERVICO (ORGAO ESPECIFICO)') ||
    possuiPermissao('CRIAR ORGAO') ||
    possuiPermissao('CRIAR ORGAO (ORGAO ESPECIFICO)') ||
    possuiPermissao('CRIAR PAGINA-TEMATICA');
}

function podePublicarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('PUBLICAR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) ||
    possuiPermissaoPaginaServico('PUBLICAR SERVICO') ||
    possuiPermissaoPaginaTematica('PUBLICAR PAGINA-TEMATICA') ||
    possuiPermissaoPaginaOrgaoOrgaoEspecifico('PUBLICAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) ||
    possuiPermissaoPaginaOrgao('PUBLICAR ORGAO');
}

function podeDescartarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('DESCARTAR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) ||
    possuiPermissaoPaginaServico('DESCARTAR SERVICO') ||
    possuiPermissaoPaginaTematica('DESCARTAR PAGINA-TEMATICA') ||
    possuiPermissaoPaginaOrgaoOrgaoEspecifico('DESCARTAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) ||
    possuiPermissaoPaginaOrgao('DESCARTAR ORGAO');
}

function podeDespublicarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return possuiPermissaoPaginaServicoOrgaoEspecifico('DESPUBLICAR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) ||
    possuiPermissaoPaginaServico('DESPUBLICAR SERVICO') ||
    possuiPermissaoPaginaTematica('DESPUBLICAR PAGINA-TEMATICA') ||
    possuiPermissaoPaginaOrgaoOrgaoEspecifico('DESPUBLICAR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) ||
    possuiPermissaoPaginaOrgao('DESPUBLICAR ORGAO');
}

function podeExcluirPagina(tipoPagina, orgaoIdUsuario, orgaoIdPagina) {
  if (tipoPagina === 'servico') {
    return possuiPermissaoOrgaoEspecifico('EXCLUIR SERVICO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) ||
      possuiPermissao('EXCLUIR SERVICO');
  }

  if (tipoPagina === 'orgao') {
    return possuiPermissaoOrgaoEspecifico('EXCLUIR ORGAO (ORGAO ESPECIFICO)', orgaoIdUsuario, orgaoIdPagina) ||
      possuiPermissao('EXCLUIR ORGAO');
  }

  if (tipoPagina === 'pagina-tematica') {
    return possuiPermissao('EXCLUIR PAGINA-TEMATICA');
  }

  return false;
}

function podePublicarDascartarPagina(orgaoIdUsuario, orgaoIdPagina) {
  return podePublicarPagina(orgaoIdUsuario, orgaoIdPagina) &&
    podeDescartarPagina(orgaoIdUsuario, orgaoIdPagina);
}

function podeCriarUsuario() {
  return possuiPermissao('CADASTRAR ADMIN') ||
    possuiPermissao('CADASTRAR PONTO_FOCAL') ||
    possuiPermissao('CADASTRAR PUBLICADOR') ||
    possuiPermissao('CADASTRAR EDITOR');
}

function podeRenomearServico() {
  return possuiPermissao('CRIAR SERVICO');
}

module.exports = {
  podeCriarUsuario: podeCriarUsuario,
  podeSalvarPagina: podeSalvarPagina,
  podeCriarPagina: podeCriarPagina,
  podePublicarDascartarPagina: podePublicarDascartarPagina,
  podeDespublicarPagina: podeDespublicarPagina,
  podeExcluirPagina: podeExcluirPagina,
  podeMostrarPaginaLista: podeMostrarPaginaLista,
  podeRenomearServico: podeRenomearServico,
  possuiPermissaoPaginaTematica: possuiPermissaoPaginaTematica
};
