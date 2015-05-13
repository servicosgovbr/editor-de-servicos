jQuery(function($) {

  $('textarea').after(function() {
    return '<div class="counter">Número de caracteres: <span id="' + this.id + '-counter"></span></div>';
  }).each(function() {
    $(this).simplyCountable({
      counter: '#' + this.id + '-counter',
      countDirection: 'up'
    });
  });

  $('input:radio.toogle').each(function() {
    var others = $('input:radio[name=' + this.name + ']');
    var target = $('#' + this.id + '-toogle');
    var toogle = $(this);

    others.change($.fn.hide.bind(target));
    toogle.change($.fn.show.bind(target));
    target.hide();
  });

});
