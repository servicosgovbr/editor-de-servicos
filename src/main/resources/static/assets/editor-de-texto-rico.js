(function($, _, marked) {

  $(function() {
    $('textarea').each(function() { EditorMarkdown(this).cria() })
  })

  var EditorMarkdown = function(e) {

    var editor = $(e);

    var componentesEditor = [
      BarraFerramentas(editor),
      Preview(editor)
    ];

    var cria = function() {
      _.each(componentesEditor, function(c) { c.cria() });
    };

    return {
      cria: _.once(cria)
    };
  };

  var Preview = function(editor) {
    return Componente('<div class="editor-preview"></div>', function(elemento) {
      var atualizaTamanho = function() {
        elemento.css({
          'max-height': editor.css('height'),
          'height': editor.css('height')
        });
      };

      var atualizaTexto = function() {
        elemento.html(marked(
          editor.val() || editor.attr('placeholder') || ''
        ));
      };

      var atualiza = _.debounce(
        _.compose(atualizaTamanho, atualizaTexto),
      500);

      editor
        .after(elemento)
        .mouseup(atualizaTamanho)
        .keydown(atualiza)
        .change(atualiza);

      atualiza();
    });
  };

  var BarraFerramentas = function(editor) {

    var botoes = [
      Link(editor),
      Lista(editor)
    ];

    return Componente('<div class="editor-barra-ferramentas"></div>', function(elemento) {
      _.each(botoes, function(botao) {
        elemento.append(botao.cria());
      });

      editor.before(elemento);
    });
  };

  var Link = function(editor) {
    return Botao('Adicionar link', 'fa fa-link', editor, function() {
      var textoLink = editor.getSelection().text || 'digite a url aqui'
      editor.surroundSelectedText('[', ']('+ textoLink +')');
    });
  };

  var Lista = function(editor) {
    return Botao('Adicionar item de lista', 'fa fa-list', editor, function() {
      var textoSelecionado = editor.getSelection()
      editor.replaceSelectedText('\n- ' + (textoSelecionado.text || 'Novo item'))
    });
  };

  var Botao = function(nome, icone, editor, acao) {

    var html = '<a href="javascript:void(0)" title="' + nome + '" alt="' + nome  + '"><i class="' + icone + '"></i>'

    return Componente(html, function(elemento) {
      var disparaAcao = _.wrap(acao, function(acao) {
        acao();
        editor.change();
      });

      elemento.click(disparaAcao);
    });
  };

  var Componente = function(html, construtor) {

    var elemento = $(html);

    var cria = _.wrap(construtor, function(cons) {
      cons(elemento);
      return elemento;
    });

    return {
      cria: cria
    };
  };

})(jQuery, _, marked);
