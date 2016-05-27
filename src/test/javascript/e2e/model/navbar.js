var Navbar = {
    cadastrar: element(by.cssContainingText('a', 'Cadastrar-se')),
    accountMenu: element(by.id('account-menu')),
        logout: element(by.id('logout')),
        editarDados: element(by.cssContainingText('a', 'Editar dados')),
        alterarSenha: element(by.cssContainingText('a', 'Alterar senha')),
    grupos: element(by.cssContainingText('a', 'Grupos')),
};

module.exports = Navbar;
