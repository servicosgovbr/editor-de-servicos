jQuery(function($) {

  var applicaToogle = function () {
    var elementoAlvo = $('#' + this.id + '-toogle');
    var escondeAlvo = $.fn.hide.bind(elementoAlvo);
    var mostraAlvo = $.fn.show.bind(elementoAlvo);

    $('input:radio[name=' + this.name + ']').change(escondeAlvo);
    $(this).change(mostraAlvo);

    escondeAlvo();
  };

  $('input:radio.toogle').each(applicaToogle);

});

