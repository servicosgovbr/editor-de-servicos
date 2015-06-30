jQuery(function($) {

    function desabilitaComponente(c) {
        $(c).prop('disabled', true);
    }

    $('#principal > form').on('submit', function(e) {
        $('fieldset, input, button, textarea').prop('disabled', true)
        $('a').prop('href', 'javascript:')

        return true;
    })
});