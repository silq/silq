var FlashMessage = function() {
    this.element = element(by.css('#flash-message > .alert-dismissible'));
};

FlashMessage.prototype.getText = function () {
    return this.element.getText();
};

FlashMessage.prototype.expectMessageContains = function(msg) {
    browser.ignoreSynchronization = true;
    browser.sleep(500);
    expect(this.getText()).toContain(msg);
    browser.sleep(500);
    browser.ignoreSynchronization = false;
};

module.exports = new FlashMessage();
