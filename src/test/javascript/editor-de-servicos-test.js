'use strict';

var xhr = require('lib/mithril-fake-xhr')(window);
var editor = require('editor-de-servicos');

describe('editor', function () {

  it('deve ser um modulo do mithril', function () {
    expect(editor.controller).toBeDefined();
    expect(editor.view).toBeDefined();
  });

  it('deve inicializar um servico', function () {
    var ctrl = new editor.controller();
    expect(ctrl.servico).toBeDefined();
  });

  it('deve salvar um servico', function () {
    var response = xhr('post', '/editar/v3/servico/ola-mundo').respondWith('<servico/>');

    var ctrl = new editor.controller();
    ctrl.servico().nome('olá, mundo!');
    expect(ctrl.salvar).toBeDefined();

    ctrl.salvar();

    expect(xhr.unexpectedRequests).toBe(0);
    expect(response.count).toBe(1);
  });

});
