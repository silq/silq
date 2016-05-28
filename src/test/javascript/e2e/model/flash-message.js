var FlashMessage = function() {
    this.element = element(by.css('#flash-message > .alert-dismissible'));
};

FlashMessage.prototype.getText = function () {
    return this.element.getText();
};

module.exports = new FlashMessage();
