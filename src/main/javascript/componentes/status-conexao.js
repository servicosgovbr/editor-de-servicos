'use strict';

var httpOk = function (resp) {
  if (resp.login) {
    this.attr('class', '')
      .attr('title', 'Conexão estabelecida')
      .html('');

  } else {
    this.attr('class', 'mensagem erro')
      .html('Sua sessão expirou. <a href="/editar/" target="_blank">Clique aqui para entrar novamente</a>');
  }
};

var httpError = function (resp) {
  this.attr('class', 'mensagem erro')
    .attr('title', 'Conexão perdida')
    .html('Você está sem conexão com a internet');
};

var atualizarStatus = function () {
  this.attr('class', '')
    .attr('title', 'Verificando conexão')
    .html('');

  m.request({
    url: '/editar/api/ping',
    extract: function (xhr) {
      if (xhr.status === 0 || /\/editar\/login/.test(xhr.responseURL)) {
        return '{}';
      }

      return xhr.responseText;
    },
    background: true
  }).then(_.bind(httpOk, this), _.bind(httpError, this));
};

var config = function (element, isInitialized) {
  if (isInitialized) {
    return;
  }

  var e = jQuery(element);
  var fn = _.bind(atualizarStatus, e);

  window.setInterval(fn, 10000);
  fn();
};

module.exports = {

  view: function () {
    return m('span#status-conexao', {
      config: config
    });
  }
};
