jQuery(function ($) {

  var id = function(str) {
    return str.replace(/\./, '_') + '-counter';
  }

  var criaContador = function () {
    return '<div class="counter">Caracteres restantes: <span id="' + id(this.id) + '"></span></div>';
  };

  var ativaContador = function () {
    var maxLength = parseInt($(this).attr("maxlength")) || 140;
    $(this).removeAttr('maxlength');

    var opcoes = {
      counter: '#' + id(this.id),
      countDirection: 'down',
      maxCount: maxLength
    };

    console.log(opcoes);
    $(this).simplyCountable(opcoes);
  };

  $('textarea')
    .after(criaContador)
    .each(ativaContador);
});
