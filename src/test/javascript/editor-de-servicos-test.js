var editor = require('editor-de-servicos');

describe('editor', function () {

  it('deve ser um modulo do mithril', function () {
    expect(editor.controller).toBeDefined();
    expect(editor.view).toBeDefined();
  });

});
