var User = require('./model/user');
var registerForm = require('./model/register');
var loginForm = require('./model/login');
var navbar = require('./model/navbar');
var flashMessage = require('./model/flash-message');

describe('Account tests', function() {

    // Formulário de 'Editar dados':
    var editarDadosForm = {
        nome: element(by.model('settingsAccount.nome')),
        submit: element(by.buttonText('Salvar dados')),
    };

    // Formulário de 'Alterar senha':
    var alterarSenhaForm = {
        senhaAtual: element(by.model('alterarSenhaForm.senhaAtual')),
        novaSenha: element(by.model('alterarSenhaForm.novaSenha')),
        confirmSenha: element(by.model('alterarSenhaForm.confirmSenha')),
        submit: element(by.buttonText('Alterar senha')),
    };

    var user = User.getUser();

    beforeAll(function() {
        browser.get('/');
    });

    afterAll(function () {
        User.logout();
    });

    it('deve registrar um novo usuário e logá-lo', function() {
        User.register(user);
        expect(browser.getCurrentUrl()).toContain('home');
        expect(element(by.css('.main-content')).getText()).toContain('Seja bem-vindo');
    });

    it('deve sair ao deslogar', function() {
        navbar.accountMenu.click();
        navbar.logout.click();
        expect(element(by.css('.main-content')).getText()).toContain('acrônimo para');
    });

    it('deve falhar ao logar com senha inválida', function() {
        loginForm.email.sendKeys(user.email);
        loginForm.senha.sendKeys('54321');
        loginForm.submit.click();
        expect(element(by.css('.alert.alert-danger')).getText()).toContain('Usuário ou senha incorretos');
    });

    it('deve entrar na área restrita ao logar com credenciais válidas', function() {
        User.login(user);
        expect(browser.getCurrentUrl()).toContain('home');
        expect(element(by.css('.main-content')).getText()).toContain('Seja bem-vindo');
    });

    it('deve alterar as informações de usuário ao editar dados', function() {
        navbar.accountMenu.click();
        navbar.editarDados.click();
        expect(element(by.css('.main-content')).getText()).toContain('Informações do usuário ' + user.nome);

        editarDadosForm.nome.clear();
        editarDadosForm.nome.sendKeys('Novo nome');
        editarDadosForm.submit.click();
        expect(element(by.css('.main-content')).getText()).toContain('Sucesso');
        expect(element(by.css('.main-content')).getText()).toContain('Informações do usuário Novo nome');

        // Volta às informações originais
        editarDadosForm.nome.clear();
        editarDadosForm.nome.sendKeys(user.nome);
        editarDadosForm.submit.click();
    });

    it('deve falhar ao alterar senha com senha atual errada', function() {
        navbar.accountMenu.click();
        navbar.alterarSenha.click();

        alterarSenhaForm.senhaAtual.sendKeys('54321');
        alterarSenhaForm.novaSenha.sendKeys('11111');
        alterarSenhaForm.confirmSenha.sendKeys('11111');
        alterarSenhaForm.submit.click();

        flashMessage.expectMessageContains('Senha atual incorreta');
    });

    it('deve obter sucesso ao alterar senha com senha atual correta e requisitar novo login', function() {
        navbar.accountMenu.click();
        navbar.alterarSenha.click();

        alterarSenhaForm.senhaAtual.clear();
        alterarSenhaForm.senhaAtual.sendKeys(user.senha);

        user.senha = '11111';
        alterarSenhaForm.novaSenha.clear();
        alterarSenhaForm.novaSenha.sendKeys(user.senha);
        alterarSenhaForm.confirmSenha.clear();
        alterarSenhaForm.confirmSenha.sendKeys(user.senha);
        alterarSenhaForm.submit.click();

        flashMessage.expectMessageContains('Senha alterada');
    });

    it('deve logar novamente com nova senha', function() {
        loginForm.email.clear();
        loginForm.email.sendKeys(user.email);
        loginForm.senha.clear();
        loginForm.senha.sendKeys('11111');
        loginForm.submit.click();
        expect(element(by.css('.main-content')).getText()).toContain('Seja bem-vindo');
    });
});
