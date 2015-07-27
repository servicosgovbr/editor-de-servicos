'use strict';

var slugify = require('slugify');

describe('slugify', function () {

  it('deve transformar strings em slugs', function () {
    expect(slugify('LALALA')).toEqual('lalala');
    expect(slugify('um dois')).toEqual('um-dois');
    expect(slugify('um_dois')).toEqual('um-dois');
    expect(slugify('um (dois)')).toEqual('um-dois');
    expect(slugify('áéíóú àèìòù ãñçõ âêîôû')).toEqual('aeiou-aeiou-anco-aeiou');
    expect(slugify('olá, mundo!')).toEqual('ola-mundo');
  });

});
