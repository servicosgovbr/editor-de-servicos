'use strict';

var ping = function (element, isInitialized) {
  if (!isInitialized) {
    var e = jQuery(element);

    var ok = function (resp) {
      if(resp.login) {
        e.attr('class', 'fa fa-circle ok').attr('title', 'Conexão estabelecida').html('');

      } else {
        e.attr('class', 'fa fa-circle nok').attr('title', 'Sessão expirada')
          .html('<span class="mensagem">Sua sessão expirou! <a href="/editar/" target="_blank">Clique aqui para entrar novamente.</a></span>');
      }
    };

    var nok = function (resp) {
      e.attr('class', 'fa fa-circle nok').attr('title', 'Conexão perdida').html('');
    };

    var atualizarStatus = function () {
      e.attr('class', 'fa fa-spinner fa-spin').attr('title', 'Verificando conexão').html('');
      m.request({
        url: '/editar/api/ping',
        extract: function(xhr) {
          if(/\/editar\/login/.test(xhr.responseURL)) {
            return '{"login": null}';
          }
          return xhr.responseText;
        },
        background: true
      }).then(ok, nok);
    };

    window.setInterval(atualizarStatus, 5000);
  }
};

module.exports = {

  controller: function (args) {
    var config = _.merge({
      salvar: _.noop
    }, args);

    this.salvar = config.salvar;
  },

  view: function (ctrl) {
    if (m.route().indexOf('/editar/servico/') >= 0) {
      return m('#metadados', [

      m('span#status-conexao', {
          config: ping
        }),

      m('button#salvar', {
          onclick: _.bind(ctrl.salvar, ctrl)
        }, [
        m('i.fa.fa-floppy-o'),
        m.trust('&nbsp; Salvar')
      ]),

    ]);
    }

    return m('#metadados');
  }

};
