jQuery(function ($) {

  $('#servico-gratuito-sim, #servico-gratuito-nao').on('change', function() {
    $('.servico-gratuito-sim-toggle, .servico-gratuito-nao-toggle').hide();
    $('.' + this.id + '-toggle').show();
  });

});
