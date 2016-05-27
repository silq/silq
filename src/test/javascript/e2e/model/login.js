var Login = {
    email: element(by.model('email')),
    senha: element(by.model('password')),
    submit: element(by.buttonText('Entrar')),
};

module.exports = Login;
