jQuery(function($) {

  $('textarea').after(function() {
    return '<div class="counter">Número de caracteres: <span id="' + this.id + '-counter"></span></div>';
  }).each(function() {
    $(this).simplyCountable({
      counter: '#' + this.id + '-counter',
      countDirection: 'up'
    });
  });

});
