jQuery(function ($) {

  $('.etapa > .sub-secao').click(function () {
    $(this).next()
      .slideToggle();

    $(this).find('> span.fa')
      .toggleClass('fa-chevron-up')
      .toggleClass('fa-chevron-down');
  });

});
