describe('Account tests', function() {

    var accountMenuLink = element(by.id('account-menu'));
    var cadastrarMenuLink = element(by.cssContainingText('a', 'Cadastrar-se'));
    var logoutMenuLink = element(by.id('logout'));
    var editarDadosMenuLink = element(by.cssContainingText('a', 'Editar dados'));
    var alterarSenhaMenuLink = element(by.cssContainingText('a', 'Alterar senha'));

    // Formulário de registro:
    var registerForm = {
        nomeInput: element(by.model('registerAccount.nome')),
        emailInput: element(by.model('registerAccount.email')),
        senhaInput: element(by.model('registerAccount.senha')),
        senhaConfirmInput: element(by.model('confirmPassword')),
        cadastrarButton: element(by.buttonText('Cadastrar')),
    };

    // Página inicial + formulário de login:
    var loginForm = {
        email: element(by.model('email')),
        senha: element(by.model('password')),
        submit: element(by.buttonText('Entrar')),
    };

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

    // Nome aleatório do novo usuário
    var userId = Math.random().toString(36).substring(7);
    var user = {
        id: userId,
        nome: 'Usuário de teste #' + userId,
        email: userId + '@localhost',
        senha: '12345',
    };

    beforeAll(function() {
        browser.get('/');
    });

    it('deve registrar um novo usuário e logá-lo', function() {
        cadastrarMenuLink.click();
        registerForm.nomeInput.sendKeys(user.nome);
        registerForm.emailInput.sendKeys(user.email);
        registerForm.senhaInput.sendKeys(user.senha);
        registerForm.senhaConfirmInput.sendKeys(user.senha);
        registerForm.cadastrarButton.click();
        expect(browser.getCurrentUrl()).toContain('home');
        expect(element(by.css('.main-content')).getText()).toContain('Seja bem-vindo');
    });

    it('deve sair ao deslogar', function() {
        accountMenuLink.click();
        logoutMenuLink.click();
        expect(element(by.css('.main-content')).getText()).toContain('acrônimo para');
    });

    it('deve falhar ao logar com senha inválida', function() {
        loginForm.email.sendKeys(user.email);
        loginForm.senha.sendKeys('54321');
        loginForm.submit.click();
        expect(element(by.css('.alert.alert-danger')).getText()).toContain('Usuário ou senha incorretos');
    });

    it('deve entrar na área restrita ao logar com credenciais válidas', function() {
        loginForm.email.clear();
        loginForm.email.sendKeys(user.email);
        loginForm.senha.clear();
        loginForm.senha.sendKeys(user.senha);
        loginForm.submit.click();
        expect(browser.getCurrentUrl()).toContain('home');
        expect(element(by.css('.main-content')).getText()).toContain('Seja bem-vindo');
    });

    it('deve alterar as informações de usuário ao editar dados', function() {
        accountMenuLink.click();
        editarDadosMenuLink.click();
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
        accountMenuLink.click();
        alterarSenhaMenuLink.click();

        alterarSenhaForm.senhaAtual.sendKeys('54321');
        alterarSenhaForm.novaSenha.sendKeys('11111');
        alterarSenhaForm.confirmSenha.sendKeys('11111');
        alterarSenhaForm.submit.click();

        expect(element(by.css('.alert.alert-dismissible')).getText()).toContain('Senha atual incorreta');
    });

    it('deve obter sucesso ao alterar senha com senha atual correta e requisitar novo login', function() {
        accountMenuLink.click();
        alterarSenhaMenuLink.click();

        alterarSenhaForm.senhaAtual.clear();
        alterarSenhaForm.senhaAtual.sendKeys(user.senha);

        user.senha = '11111';
        alterarSenhaForm.novaSenha.clear();
        alterarSenhaForm.novaSenha.sendKeys(user.senha);
        alterarSenhaForm.confirmSenha.clear();
        alterarSenhaForm.confirmSenha.sendKeys(user.senha);
        alterarSenhaForm.submit.click();

        expect(element(by.css('.alert.alert-dismissible')).getText()).toContain('Senha alterada');
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
