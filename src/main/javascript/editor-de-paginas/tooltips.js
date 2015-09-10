'use strict';

var t = function (txt) {
  return {
    view: function () {
      return m('span.tooltip.dica-campo', m('span', txt));
    }
  };
};

module.exports = {
  tipo: t('Escolha o tipo de página que deseja editar.'),
  nome: t('Este é o nome que aparecerá em destaque. Deve ser curto e claro para o solicitante.'),
  conteudo: t('Aqui deverá ser definido o conteúdo da página, em formato Markdown.'),
};
