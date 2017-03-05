import {Parameter} from '../parameters/parameter';
import {Mechanism} from './mechanism';
import {ParameterValuePropertyPair} from '../parameters/parameter_value_property_pair';
import {ParameterInterface} from '../parameters/parameter_interface';


export class TextMechanism extends Mechanism {

    constructor(id:number, type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:ParameterInterface[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }

    getContext():any {
        var textareaStyle = this.getCssStyle([
            new ParameterValuePropertyPair('textareaFontColor', 'color'),
            new ParameterValuePropertyPair('borderColor', 'border-color'),
            new ParameterValuePropertyPair('borderWidth', 'border-width'),
            new ParameterValuePropertyPair('fieldBackgroundColor', 'background-color'),
            new ParameterValuePropertyPair('fieldFontSize', 'font-size'),
            new ParameterValuePropertyPair('fieldFontType', 'font-style'),
            new ParameterValuePropertyPair('fieldHeight', 'height'),
            new ParameterValuePropertyPair('fieldTextAlignment', 'text-align'),
            new ParameterValuePropertyPair('fieldWidth', 'width')
        ]);
        var labelStyle = this.getCssStyle([
            new ParameterValuePropertyPair('labelPositioning', 'text-align'),
            new ParameterValuePropertyPair('labelColor', 'color'),
            new ParameterValuePropertyPair('labelFontSize', 'font-size')
        ]);

        return {
            active: this.active,
            hint: this.getParameterValue('hint'),
            label: this.getParameterValue('label'),
            currentLength: 0,
            maxLength: this.getParameterValue('maxLength'),
            maxLengthVisible: this.getParameterValue('maxLengthVisible'),
            maxLengthStrict: this.getParameterValue('maxLengthStrict'),
            textareaStyle: textareaStyle,
            labelStyle: labelStyle,
            clearInput: this.getParameterValue('clearInput'),
            mandatory: this.getParameterValue('mandatory'),
            mandatoryReminder: this.getParameterValue('mandatoryReminder'),
            boxWidth: this.getParameterValue('boxWidth') || '100%',
            boxPaddingLeft: this.getParameterValue('boxPaddingLeft') || '0',
            boxPaddingRight: this.getParameterValue('boxPaddingRight') || '20px',
            validationEmail: this.getParameterValue('validateEmail') || false
        }
    }
}
