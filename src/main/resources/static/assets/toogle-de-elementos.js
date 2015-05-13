jQuery(function($) {

  $('input:radio.toogle').each(function() {
    var others = $('input:radio[name=' + this.name + ']');
    var target = $('#' + this.id + '-toogle');
    var toogle = $(this);

    others.change($.fn.hide.bind(target));
    toogle.change($.fn.show.bind(target));
    target.hide();
  });

});

