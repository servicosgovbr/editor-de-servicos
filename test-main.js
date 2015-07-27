require.list()
  .filter(function (i) {
    return new RegExp('test$').test(i);
  })
  .map(function (i) {
    require(i);
  });
