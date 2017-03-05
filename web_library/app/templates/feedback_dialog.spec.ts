import Handlebars = require('handlebars');
import i18n = require('i18next');
import {I18nHelper} from '../js/helpers/i18n';

/*
describe('Feedback dialog template', () => {
    let feedbackDialogTemplate;
    let feedbackDialogTemplateWithTranslations;

    beforeAll(() => {
        var resources = { en: {translation: {"take_screenshot": "Take screenshot"}}, de: {translation: {"take_screenshot": "Screenshot erstellen"}}};
        I18nHelper.initializeI18n({lang: 'en'}, resources);

        var t = function(i18n_key) {
            return i18n.t(i18n_key);
        };
        Handlebars.registerHelper('t', t);

        i18n.changeLanguage('en');
    });

    beforeEach(() => {
        feedbackDialogTemplate = '{{#if textMechanism.active}}<section class="feedback-mechanism horizontal" id="textType">' +
            '<p id="textTypeLabel" class="explanation col col-left" style="{{ textMechanism.labelStyle }}">{{ textMechanism.label }}</p>' +
            '<article class="col col-right">' +
            '<textarea class="validate" {{#if textMechanism.mandatory }} data-mandatory="1" ' +
            'data-mandatory-default-text="This field can\'t be blank" data-mandatory-manual-text="{{ textMechanism.mandatoryReminder }}" ' +
            '{{/if}}placeholder="{{ textMechanism.hint }}" id="textTypeText" style="{{ textMechanism.textareaStyle }}"></textarea>' +
            '<p class="textarea-bottom">' +
            '{{#if textMechanism.clearInput }}<button id="textTypeTextClear" title="Clear the text input">x</button>{{/if}}{{#if textMechanism.maxLengthVisible}}' +
            '<span id="textTypeMaxLength">{{ textMechanism.currentLength }} / {{ textMechanism.maxLength }}</span> ' +
            '{{/if}}</p><div class="clearfix"></div></article><div class="clearfix"></div></section>{{/if}}';

        feedbackDialogTemplateWithTranslations = '{{#if screenshotMechanism.active}}<section class="feedback-mechanism horizontal" id="screenshotType">' +
            '<article class="col col-left"><button id="takeScreenshot" class="button small">{{t "take_screenshot" }}</button></article></section>{{/if}}';
    });

    it('should not display anything if mechanism is not in context', () => {
        var template = Handlebars.compile(feedbackDialogTemplate);
        var context = {};
        var html = template(context);
        expect(html).toEqual('');
    });

    it('should not display anything if mechanism is disabled', () => {
        var template = Handlebars.compile(feedbackDialogTemplate);
        var context = {
            textMechanism: {
                active: false
            }
        };
        var html = template(context);
        expect(html).toEqual('');
    });

    it('should be configured via the textMechanism object', () => {
        var template = Handlebars.compile(feedbackDialogTemplate);
        var context = {
            textMechanism: {
                active: true,
                mandatory: false,
                label: 'This is the label',
                labelStyle: 'font-size: 10px;',
                hint: 'This is the hint',
                textareaStyle: 'font-size: 12px;',
                clearInput: false,
                maxLengthVisible: true,
                currentLength: 0,
                maxLength: 100
            }
        };
        var html = template(context);
        expect(html).not.toEqual('');

        var expectedFeedbackDialogTemplate = '<section class="feedback-mechanism horizontal" id="textType">' +
            '<p id="textTypeLabel" class="explanation col col-left" style="font-size: 10px;">This is the label</p>' +
            '<article class="col col-right">' +
            '<textarea class="validate" placeholder="This is the hint" id="textTypeText" style="font-size: 12px;"></textarea>' +
            '<p class="textarea-bottom">' +
            '<span id="textTypeMaxLength">0 / 100</span> ' +
            '</p><div class="clearfix"></div></article><div class="clearfix"></div></section>';

        expect(html).toEqual(expectedFeedbackDialogTemplate);
    });

    it('should do the translations when building the template', () => {
        var template = Handlebars.compile(feedbackDialogTemplateWithTranslations);
        var context = {
            screenshotMechanism: {
                active: true
            }
        };
        var html = template(context);

        var expectedTemplate = '<section class="feedback-mechanism horizontal" id="screenshotType">' +
            '<article class="col col-left"><button id="takeScreenshot" class="button small">Take screenshot</button></article></section>';
        expect(html).toEqual(expectedTemplate);


        i18n.changeLanguage('de');
        var html = template(context);

        var expectedTemplate = '<section class="feedback-mechanism horizontal" id="screenshotType">' +
            '<article class="col col-left"><button id="takeScreenshot" class="button small">Screenshot erstellen</button></article></section>';

        expect(html).toEqual(expectedTemplate);
    });
});
*/