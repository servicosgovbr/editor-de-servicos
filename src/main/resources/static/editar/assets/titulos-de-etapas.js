jQuery(function ($) {

  $('.etapa').find('input[type="text"]:first').change(function() {
    var label = $(this).parents('.etapa').find('.sub-secao h3');
    label.text(label.text().split(/:/)[0] + ': ' + $(this).val());
  });

});
