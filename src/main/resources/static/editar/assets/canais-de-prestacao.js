jQuery(function ($) {
    var onCanalDePrestacaoSelect = function(container, option) {
        option = (option || "").trim().toLowerCase();
        var titulo = $(container).find('label.titulo');
        var referencia = $(container).find('label.referencia > span');

        if (option == "web") {
            titulo.show();
            referencia.val("Link/URL");
        } else {
            titulo.hide().find('input').val('');
            referencia.text("");
        }
    };

    $('.grupo-canal-prestacao').each(function() {
        var container = $(this);
        container.find('select').change(function() {
            onCanalDePrestacaoSelect(container, $(this).val());
        });
    });
});
