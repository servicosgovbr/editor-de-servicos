'use strict';

var xhr = require('lib/mithril-fake-xhr')(window);
var editor = require('editor-de-servicos');

describe('editor', function () {

  beforeEach(function () {
    xhr.reset();

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
    xhr('GET', '/editar/api/existe-id-servico/ola-mundo').respondWith('false');
    xhr('GET', '/editar/api/servico/v3/ola-mundo').respondWith('<servico/>');
    var response = xhr('POST', '/editar/v3/servico/ola-mundo').respondWith('<servico/>');

    var ctrl = new editor.controller();
    expect(ctrl.servico()).toBeDefined();

    ctrl.servico().nome('olá, mundo!');
    ctrl.servico().descricao('descrição');
    ctrl.servico().palavrasChave(['a', 'b', 'c']);

    expect(ctrl.salvar).toBeDefined();

    ctrl.salvar();

    expect(xhr.unexpectedRequests).toBe(0);
    expect(response.count).toBe(1);
  });

});
