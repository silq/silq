var Register = {
    nomeInput: element(by.model('registerAccount.nome')),
    emailInput: element(by.model('registerAccount.email')),
    senhaInput: element(by.model('registerAccount.senha')),
    senhaConfirmInput: element(by.model('confirmPassword')),
    cadastrarButton: element(by.buttonText('Cadastrar'))
};

module.exports = Register;
