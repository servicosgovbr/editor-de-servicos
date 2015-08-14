'use strict';

var slugify = require('slugify');

describe('slugify', function () {

  it('deve transformar strings em slugs', function () {
    expect(slugify('')).toEqual('');
    expect(slugify('V32/5')).toEqual('v32-5');
    expect(slugify('LALALA')).toEqual('lalala');
    expect(slugify('um dois')).toEqual('um-dois');
    expect(slugify('um_dois')).toEqual('um-dois');
    expect(slugify('um (dois)')).toEqual('um-dois');
    expect(slugify('áéíóú àèìòù ãñçõ âêîôû')).toEqual('aeiou-aeiou-anco-aeiou');
    expect(slugify('olá, mundo!')).toEqual('ola-mundo');
    expect(slugify('Parcelamento MP nº 449, de 2008')).toEqual('parcelamento-mp-n-449-de-2008');
    expect(slugify('Parcelamento MP nº 449, de 2008')).toEqual('parcelamento-mp-n-449-de-2008');
    expect(slugify('Opções da Lei nº 11.941/2009')).toBe('opcoes-da-lei-n-11-941-2009');
  });

});
