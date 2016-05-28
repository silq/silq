var navbar = require('./navbar');
var registerForm = require('./register');
var loginForm = require('./login');

var User = {};

User.createUser = function() {
    var id = Math.random().toString(36).substring(7);
    return {
        id: id,
        nome: 'Usuário de teste #' + id,
        email: id + '@localhost',
        senha: '12345',
        registered: false
    };
};

User.getUser = function() {
    if (!User.CURRENT_USER) {
        User.CURRENT_USER = User.createUser();
    }
    return User.CURRENT_USER;
};

User.register = function(user) {
    if (!user) {
        user = User.getUser();
    }

    if (user.registered) {
        return;
    }

    browser.get('/');
    navbar.cadastrar.click();
    registerForm.nomeInput.sendKeys(user.nome);
    registerForm.emailInput.sendKeys(user.email);
    registerForm.senhaInput.sendKeys(user.senha);
    registerForm.senhaConfirmInput.sendKeys(user.senha);
    registerForm.cadastrarButton.click();
    user.registered = true;
}

User.login = function(user) {
    if (!user) {
        user = User.getUser();
    }

    if (!user.registered) {
        User.register(user);
    }

    browser.get('/');
    loginForm.email.clear();
    loginForm.email.sendKeys(user.email);
    loginForm.senha.clear();
    loginForm.senha.sendKeys(user.senha);
    loginForm.submit.click();
};

User.logout = function() {
    navbar.accountMenu.click();
    navbar.logout.click();
};

module.exports = User;
