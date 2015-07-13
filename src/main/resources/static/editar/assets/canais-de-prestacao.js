jQuery(function ($) {
    var onCanalDePrestacaoSelect = function(container, option) {
        option = ("" || option).trim().toLowerCase();
        var labelDescricao = $(container).find('label.descricao');
        var inputDescricao = $(container).find('label.descricao > input');
        var labelReferencia = $(container).find('label.referencia > span');

        if (option == "web") {
            labelDescricao.show();
            labelReferencia.val("Link/URL");
        } else {
            labelDescricao.hide();
            inputDescricao.val("");
            labelReferencia.text("");
        }
    };

    $('.canal-prestacao').each(function() {
        var container = this;
        $(container).find('select').change(function() {
            onCanalDePrestacaoSelect($(container), $(this).val());
        });
    });
});
