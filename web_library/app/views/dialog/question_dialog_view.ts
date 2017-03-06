import {DialogView} from './dialog_view';


export class QuestionDialogView extends DialogView {

    constructor(public dialogId:string, public template:any, public context:any, public openCallback?:() => void,
                public closeCallback?:() => void) {
        super(dialogId, template, context, openCallback, closeCallback);
    }

    addAnswerOption(selector, callback:() => void) {
        jQuery(document).on('click', '' + selector, function(e) {
            e.preventDefault();
            e.stopPropagation();
            callback();
        });
    }
}