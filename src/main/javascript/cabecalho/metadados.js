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

    this.salvando = m.prop(false);
    this.salvar = function () {
      this.salvando(true);
      return config.salvar().then(_.bind(function (resp) {
        this.salvando(false);
        alertify.success('Rascunho salvo com sucesso!');
        m.redraw();
        return resp;
      }, this), _.bind(function () {
        this.salvando(false);
        m.redraw();
      }, this));
    };

    this.publicar = function () {
      if (!config.publicar()) {
        alertify.error('Serviço ainda contém erros.');
      } else {
        alertify.success('Serviço publicado com sucesso!');
      }
    };

    this.visualizar = function () {
      config.visualizar();
    };

    this.editar = function () {
      config.editar();
    };

    this.config = config;
  },

  view: function (ctrl) {

    var salvarView = '';
    if (ctrl.config.salvar !== _.noop) {
      salvarView = m('button#salvar', {
        onclick: _.bind(ctrl.salvar, ctrl),
        disabled: ctrl.salvando() ? 'disabled' : ''
      }, ctrl.salvando() ? [
        m('i.fa.fa-spin.fa-spinner'),
        m.trust('&nbsp; Salvando...')
      ] : [
        m('i.fa.fa-floppy-o'),
        m.trust('&nbsp; Salvar')
      ]);
    }

    var visualizarView = '';
    if (ctrl.config.visualizar !== _.noop) {
      visualizarView = m('button#visualizar', {
        onclick: _.bind(ctrl.visualizar, ctrl),
      }, [
        m('i.fa.fa-eye'),
        m.trust('&nbsp; Visualizar')
      ]);
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
