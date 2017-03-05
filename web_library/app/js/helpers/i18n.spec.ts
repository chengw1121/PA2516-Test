import {I18nHelper} from './i18n';


describe('i18n Helper', () => {

    //assumes that de and en are available, but ru not
    it('should determine the correct language depending on fallback and set language options', () => {
        var optionsBothAvailable = {
            'fallbackLang': 'de',
            'lang': 'en',
            'distPath': 'base/app/'
        };
        var langNotAvailable = {
            'fallbackLang': 'de',
            'lang': 'ru',
            'distPath': 'base/app/'
        };

        expect(I18nHelper.getLanguage(optionsBothAvailable)).toEqual('en');
        expect(I18nHelper.getLanguage(langNotAvailable)).toEqual('de');
    });
});