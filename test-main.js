require.list()
  .filter(function (i) {
    return new RegExp('^src/test/javascript').test(i);
  })
  .map(function (i) {
    require(i);
  });
