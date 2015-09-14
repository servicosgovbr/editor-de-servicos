'use strict';

module.exports = require('componentes/checkboxes-ajax').create({
  chave: 'areasDeInteresse',
  id: 'areas-de-interesse',
  titulo: 'Áreas de interesse',
  itens: require('referencia').areasDeInteresse()
});
