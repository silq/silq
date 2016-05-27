exports.config = {
  framework: 'jasmine',
  seleniumAddress: 'http://localhost:4444/wd/hub',
  suites: {
      account: 'e2e/account.test.js',
      grupos: 'e2e/grupos.test.js',
  },
  baseUrl: 'http://127.0.0.1:8080',
}
