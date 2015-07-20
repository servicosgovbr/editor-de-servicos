jQuery(function ($) {
  $(document).ready(function () {
    $('input').keydown(function (event){
      if(event.keyCode == 13) {
        event.preventDefault();
        return false;
      }
    });
  });
});