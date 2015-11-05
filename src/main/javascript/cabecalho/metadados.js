'use strict';

module.exports = {

  controller: function (args) {
    var config = _.merge({
      salvar: _.noop,
      publicar: _.noop,
      visualizar: _.noop,
      editar: _.noop,
    }, args);

    alertify.set({
      delay: 1500
    });

    this.publicar = function () {
      if (!config.publicar()) {
        alertify.error('Serviço ainda contém erros.');
      } else {
        alertify.success('Serviço publicado com sucesso!');
      }
    };

    this.editar = function () {
      config.editar();
    };

    this.config = config;
  },

  view: function (ctrl) {
    var salvarView = '';
    if (ctrl.config.salvar !== _.noop) {
      salvarView = m.component(require('cabecalho/salvar-button'), {
        salvar: ctrl.config.salvar
      });
    }

    var visualizarView = '';
    if (ctrl.config.visualizar !== _.noop) {
      visualizarView = m.component(require('cabecalho/visualizar-button'), {
        visualizar: ctrl.config.visualizar
      });
    }

    var publicarView = '';
    if (ctrl.config.publicar !== _.noop) {
      publicarView = m('button#publicar', {
        onclick: _.bind(ctrl.publicar, ctrl)
      }, [
        m('i.fa.fa-tv'),
         m.trust('&nbsp; Publicar')
      ]);
    }

    var editarView = '';
    if (ctrl.config.editar !== _.noop) {
      editarView = m('button#editar', {
        onclick: _.bind(ctrl.editar, ctrl)
      }, [
              m('i.fa.fa-pencil'),
              m.trust('&nbsp; Editar')
          ]);
    }



    return m('#metadados', [
      m.component(require('componentes/status-conexao')),
      salvarView,
      visualizarView,
      publicarView,
      editarView,
    ]);
  }

};
