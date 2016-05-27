var user = require('./model/user');
var navbar = require('./model/navbar');

describe('Grupo tests', function() {

    var grupos = element.all(by.repeater('grupo in grupos'));
    var criarGrupoButton = element.all(by.cssContainingText('button', 'Criar grupo')).first();
    var editarButtons = element.all(by.cssContainingText('button', 'Editar'));
    var excluirButtons = element.all(by.cssContainingText('button', 'Excluir'));

    var modalForm = {
        nome: element(by.model('grupo.nomeGrupo')),
        instituicao: element(by.model('grupo.nomeInstituicao')),
        area: element(by.model('grupo.nomeArea')),
        areaOptions: element.all(by.css('select[name=nomeArea] option')),
        submit: element(by.buttonText('Salvar')),
    };

    beforeAll(function() {
        user.register();
        user.login();
    });

    it('deve entrar na página de grupos e apresentá-la vazia', function() {
        navbar.grupos.click();
        expect(element.all(by.css('.main-content h2')).first().getText()).toContain('Grupos');
        expect(grupos.count()).toBe(0);
    });

    it('deve adicionar um grupo na lista ao criar um novo grupo', function() {
        criarGrupoButton.click();

        expect(element(by.css('.modal-header')).getText()).toContain('Dados do grupo');

        modalForm.nome.sendKeys('Grupo de teste #1');
        modalForm.instituicao.sendKeys('UFSC');
        modalForm.areaOptions.get(1).click();
        modalForm.submit.click();

        var r = by.repeater('grupo in grupos');
        expect(grupos.count()).toBe(1);
        expect(element.all(r.row(0).column('grupo.nomeGrupo')).getText()).toContain('Grupo de teste #1');
        expect(element.all(r.row(0).column('grupo.nomeInstituicao')).getText()).toContain('UFSC');
        expect(element.all(r.row(0).column('grupo.nomeArea')).getText()).toContain('Administração, Ciências Contábeis e Turismo');
        expect(element.all(r.row(0).column('grupo.pesquisadores.length')).getText()).toContain('0');
    });

    it('deve alterar dados do grupo ao editá-lo', function() {
        editarButtons.first().click();
        expect(element(by.css('.modal-header')).getText()).toContain('Dados do grupo');

        modalForm.nome.sendKeys('-2');
        modalForm.instituicao.sendKeys('-2');
        modalForm.areaOptions.get(2).click();
        modalForm.submit.click();

        navbar.grupos.click();

        var r = by.repeater('grupo in grupos');
        expect(grupos.count()).toBe(1);
        expect(element.all(r.row(0).column('grupo.nomeGrupo')).getText()).toContain('Grupo de teste #1-2');
        expect(element.all(r.row(0).column('grupo.nomeInstituicao')).getText()).toContain('UFSC-2');
        expect(element.all(r.row(0).column('grupo.nomeArea')).getText()).toContain('Antropologia / Arqueologia');
        expect(element.all(r.row(0).column('grupo.pesquisadores.length')).getText()).toContain('0');
    });

    it('deve remover grupo da lista ao excluir grupo', function() {
        excluirButtons.first().click();
        expect(element(by.css('.modal-header')).getText()).toContain('Confirmar deleção');
        element(by.cssContainingText('.modal-footer button', 'Deletar')).click();
        expect(grupos.count()).toBe(0);
    });
});
