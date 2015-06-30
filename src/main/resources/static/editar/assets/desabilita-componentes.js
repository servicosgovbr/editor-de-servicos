jQuery(function($) {

    function desabilitaComponente(c) {
        $(c).prop('disabled', true);
    }

    $('#principal > form').on('submit', function(e) {
        $('fieldset, input, textarea').prop('readonly', true)
        $('input[type=submit], button').prop('disabled', true)
        $('a').prop('href', 'javascript:')

        return true;
    })
});