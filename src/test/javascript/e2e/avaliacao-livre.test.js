var path = require('path');
var user = require('./model/user');
var navbar = require('./model/navbar');
var flashMessage = require('./model/flash-message');

describe('Avaliação Livre tests', function() {

    var form = {
        area: element(by.model('model.area')),
        areaOptions: element.all(by.css('select[name=area] option')),
        submit: element(by.cssContainingText('button', 'Iniciar Avaliação')),
    };

    var files = element.all(by.repeater('file in files'));
    var dropbox = element(by.css('form[name=uploadForm]'));
    var uploadInput = element(by.css('input[type=file]'));

    beforeAll(function() {
        user.login();
    });

    afterAll(function () {
        user.logout();
    });

    it('deve entrar na página de avaliação livre', function() {
        navbar.avaliacaoLivre.click();
        expect(element.all(by.css('.main-content h2')).first().getText()).toContain('Avaliação livre de currículos');
    });

    it('deve possuir a dropbox vazia ao entrar', function() {
        expect(files.count()).toBe(0);
    });

    it('deve apresentar um erro ao tentar avaliar sem currículos enviados', function() {
        form.areaOptions.get(8).click();
        form.submit.click();
        expect(flashMessage.getText()).toContain('Selecione ao menos um currículo válido para avaliar');
    });

    it('deve adicionar um arquivo ao dropbox ao fazer upload de um arquivo', function() {
        var fileToUpload = '../../resources/fixtures/curricula/carina.xml';
        var absolutePath = path.resolve(__dirname, fileToUpload);

        // uploadInput.sendKeys(absolutePath);
        // expect(files.count()).toBe(1);
        // browser.sleep(500000000);
    });
});
