jQuery(function($) {

  var criaContador = function () {
    return '<div class="counter">Número de caracteres: <span id="' + this.id + '-counter"></span></div>';
  };

  var ativaContador = function () {
    var opcoes = {
      counter: '#' + this.id + '-counter',
      countDirection: 'up'
    };

    $(this).simplyCountable(opcoes);
  };

  $('textarea')
    .after(criaContador)
    .each(ativaContador);
});
