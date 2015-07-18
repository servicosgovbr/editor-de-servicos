jQuery(function ($) {

  $('#tempoEstimado-tipo').on('change', function() {
    $('span.tempoEstimado-selecao').hide();
    $('span#tempoEstimado-' + $(this).val()).show();
  });

});
