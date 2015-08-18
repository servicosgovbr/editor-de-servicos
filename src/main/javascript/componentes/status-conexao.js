'use strict';

var httpOk = function (resp) {
  if (resp.login) {
    this.attr('class', 'fa fa-circle ok')
      .attr('title', 'Conexão estabelecida')
      .html('');

  } else {
    this.attr('class', 'fa fa-circle nok')
      .attr('title', 'Sessão expirada')
      .html('<span class="mensagem">Sua sessão expirou! <a href="/editar/" target="_blank">Clique aqui para entrar novamente.</a></span>');
  }
};

var httpError = function (resp) {
  this.attr('class', 'fa fa-circle nok')
    .attr('title', 'Conexão perdida')
    .html('');
};

var atualizarStatus = function () {
  this.attr('class', 'fa fa-spinner fa-spin')
    .attr('title', 'Verificando conexão')
    .html('');

  m.request({
    url: '/editar/api/ping',
    extract: function (xhr) {
      if (/\/editar\/login/.test(xhr.responseURL)) {
        return '{"login": null}';
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
