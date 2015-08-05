module.exports.config = {

  paths: {
    watched: [
      'src/main/javascript/',
      'src/main/resources/vendor',
      'src/main/assets/stylesheets',
      'src/test/javascript/',
    ],
    public: 'src/main/resources/static/editar/assets/'
  },

  files: {
    javascripts: {
      joinTo: {
        'app.js': new RegExp('^src/main/javascript/'),
        'tests.js': new RegExp('^src/test/javascript/'),
        'vendor.js': new RegExp('^(?!src/(main|test)/javascript/)'),
      }
    },
    stylesheets: {
      defaultExtension: 'scss',
      joinTo: {
        'main.css': [
          new RegExp('^src/main/resources/vendor/.*\\.s?css'),
          new RegExp('^src/main/assets/stylesheets/.*\\.scss')
        ]
      }
    }
  },

  conventions: {
    assets: function () {
      return false;
    }
  },

  modules: {
    nameCleaner: function (path) {
      return path.replace(new RegExp('^src/(main|test)/javascript/'), '');
    }
  },

  plugins: {
    jshint: {
      pattern: new RegExp('^src/(main|test)/javascript/.*\\.js$'),
    },
    sass: {
      debug: 'comments'
    },
    cleanCss: {
      removeEmpty: true
    }
  }
};
