'use strict';

var erro = require('utils/erro-ajax');
var extrairMetadados = require('utils/extrair-metadados');
var atributosCsrf = require('utils/atributos-csrf');
var slugify = require('slugify');
var tratamentoAcessoNegado = require('utils/tratamento-acesso-negado');

function request(opts) {
  return m.request(_.assign({
      deserialize: _.identity
    }, opts))
    .then(null, erro);
}

function serializeXml(svc) {
  return new XMLSerializer().serializeToString(svc);
}

function deserializeXml(svc) {
  return jQuery.parseXML(svc);
}

function configCsrf(xhr) {
  xhr.setRequestHeader(atributosCsrf.header, atributosCsrf.token);
}

function configHttps(xhr) {
  xhr.setRequestHeader('X-Forwarded-For', 'https');
}

module.exports = {
  salvar: function (tipo, nome, xml, metadados) {
    var id = slugify(nome);

    return request({
      method: 'POST',
      url: '/editar/api/pagina/' + tipo + '/' + id,
      data: xml,
      config: function (xhr) {
        xhr.setRequestHeader('Accepts', 'application/xml');
        xhr.setRequestHeader('Content-Type', 'application/xml');
        configCsrf(xhr);
        configHttps(xhr);
      },
      serialize: serializeXml,
      extract: extrairMetadados(metadados),
      deserialize: deserializeXml,
      background: true
    });
  },

  carregar: function (tipo, nome, metadados) {
    return request({
      method: 'GET',
      url: '/editar/api/pagina/' + tipo + '/' + slugify(nome),
      config: function (xhr) {
        xhr.setRequestHeader('Accept', 'application/xml');
        configHttps(xhr);
      },
      extract: extrairMetadados(metadados),
      deserialize: deserializeXml
    });
  },

  publicar: function (tipo, id, metadados) {
    id = slugify(id);
    return request({
      method: 'PUT',
      background: true,
      url: '/editar/api/pagina/' + tipo + '/' + id,
      extract: extrairMetadados(metadados),
      config: function (xhr) {
        configCsrf(xhr);
        configHttps(xhr);
      }
    });
  },

  descartar: function (tipo, id, metadados) {
    var url = '/editar/api/pagina/' + tipo + '/' + id + '/descartar';
    var mimeType = 'application/xml';

    return request({
      method: 'POST',
      background: true,
      url: url,
      config: function (xhr) {
        xhr.setRequestHeader('Accept', mimeType);
        configCsrf(xhr);
        configHttps(xhr);
      },
      extract: extrairMetadados(metadados),
      deserialize: function (str) {
        return new DOMParser().parseFromString(str, 'application/xml');
      }
    });
  },

  despublicar: function (tipo, id, metadados) {
    id = slugify(id);
    var url = '/editar/api/pagina/' + tipo + '/' + id + '/despublicar';
    return request({
      method: 'POST',
      background: true,
      url: url,
      extract: extrairMetadados(metadados),
      config: function (xhr) {
        configCsrf(xhr);
        configHttps(xhr);
      }
    });
  },

  excluir: function (tipo, id) {
    var url = '/editar/api/pagina/' + tipo + '/' + slugify(id);

    return request({
      method: 'DELETE',
      url: url,
      extract: tratamentoAcessoNegado,
      config: function (xhr) {
        configCsrf(xhr);
        configHttps(xhr);
      },
    });
  },

  renomear: function (id, novoNome) {
    return request({
      method: 'PATCH',
      background: true,
      url: '/editar/api/pagina/servico/' + id,
      config: function (xhr) {
        configCsrf(xhr);
        configHttps(xhr);
      },
      serialize: _.identity,
      extract: tratamentoAcessoNegado,
      data: novoNome
    });
  },

  importarXml: function (urlParam) {
    return request({
        method: 'GET',
        url: '/editar/api/importar-xml',
        config: function (xhr) {
          xhr.setRequestHeader('Accept', 'application/xml');
          configHttps(xhr);
        },
        data: {
          url: urlParam
        }
      }).then(deserializeXml)
      .then(null, function (e) {
        erro('Erro no formato XML. Verifique se o conteúdo do endereço informado é válido: ' + urlParam);
      });
    //retorno com erro não usa xml, por isso não usamos função "deserialize", e fazemos isso aqui
  },

  obterNomeOrgao: function (urlOrgao) {
    return request({
      method: 'GET',
      url: '/editar/api/orgao',
      data: {
        urlOrgao: urlOrgao
      },
      config: configHttps
    });
  }
};
