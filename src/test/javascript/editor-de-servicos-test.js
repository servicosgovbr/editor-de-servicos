'use strict';

var xhr = require('lib/mithril-fake-xhr')(window);
var Editor = require('editor-de-servicos');

describe('editor', function () {

  var editor;
  beforeEach(function () {
    editor = new Editor();
    m.route.param = function () {
      return 'ola-mundo';
    };
  });

  it('deve ser um modulo do mithril', function () {
    expect(editor.controller).toBeDefined();
    expect(editor.view).toBeDefined();
  });

  it('deve inicializar um servico', function () {
    xhr('GET', '/editar/api/servico/v3/ola-mundo').respondWith('<servico/>');
    var ctrl = new editor.controller();
    expect(ctrl.servico()).toBeDefined();
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

  it('deve validar e impedir salvamento', function() {
    var ctrl = new editor.controller();
    ctrl.salvar();

    expect(xhr.unexpectedRequests).toBe(0);
  });

});
