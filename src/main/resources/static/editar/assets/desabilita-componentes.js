jQuery(function($) {
    $('#principal > form').on('submit', function(e) {
        $('fieldset, input, textarea').prop('readonly', true)
        return true;
    })
});