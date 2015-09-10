'use strict';

var xhr = require('lib/mithril-fake-xhr')(window);
var editor = require('editor-de-paginas/editor');

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
});
