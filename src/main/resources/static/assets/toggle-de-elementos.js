jQuery(function($) {

  var applicaToggle = function () {
    var elementoAlvo = $('#' + this.id + '-toggle');
    var escondeAlvo = $.fn.hide.bind(elementoAlvo);
    var mostraAlvo = $.fn.show.bind(elementoAlvo);

    $('input:radio[name=' + this.name + ']').change(escondeAlvo);
    $(this).change(mostraAlvo);

    escondeAlvo();
  };

  $('input:radio.toggle').each(applicaToggle);

});

