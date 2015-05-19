jQuery(function($) {

  var criaContador = function () {
    return '<div class="counter">Número de caracteres: <span id="' + this.id + '-counter"></span></div>';
  };

  var ativaContador = function () {
    var maxLength = parseInt($(this).attr("maxlength")) || 140;
    console.log(limite);

    var opcoes = {
      counter: '#' + this.id + '-counter',
      countDirection: 'down',
      maxCount: maxLength
    };

    $(this).simplyCountable(opcoes);
  };

  $('textarea')
    .after(criaContador)
    .each(ativaContador);
});
