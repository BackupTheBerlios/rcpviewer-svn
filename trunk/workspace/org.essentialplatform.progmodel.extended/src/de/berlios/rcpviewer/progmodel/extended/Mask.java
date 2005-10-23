package de.berlios.rcpviewer.progmodel.extended;
import java.lang.annotation.*;

/**
 * A mask to apply to string attributes.
 * 
 * <p>
 * The characters that can be used are shown in the following table (adapted 
 * from masks used by Swing's MaskFormatter, Java's SimpleDateFormat and also 
 * Microsoft's MaskedEdit control): 
 * <table border='2'>
 * <tr><th align='center'>Character</th><th align='center'>Description</th><th align='center'>Source</th></tr>
 * <tr><td align='center'>#</td><td align='left'>Digit placeholder, uses <code>Character.isDigit</code>.</td><td align='left'>MS, Swing</td></tr>
 * <tr><td align='center'>.</td><td align='left'>Decimal placeholder. The actual character used is the one specified as the decimal placeholder in your international settings. This character is treated as a literal for masking purposes.</td><td align='left'>MS</td></tr>
 * <tr><td align='center'>,</td><td align='left'>Thousands separator. The actual character used is the one specified as the thousands separator in your international settings. This character is treated as a literal for masking purposes.</td><td align='left'>MS</td></tr>
 * <tr><td align='center'>:</td><td align='left'>Time separator. The actual character used is the one specified as the time separator in your international settings. This character is treated as a literal for masking purposes.</td><td align='left'>MS</td></tr>
 * <tr><td align='center'>/</td><td align='left'>Date separator. The actual character used is the one specified as the date separator in your international settings. This character is treated as a literal for masking purposes.</td><td align='left'>MS</td></tr>
 * <tr><td align='center'>\ or '</td><td align='left'>Treat the next character in the mask string as a literal. This allows you to include the '#', '&', 'A', and '?' characters in the mask. This character is treated as a literal for masking purposes.</td><td align='left'>MS (\), Swing (')</td></tr>
 * <tr><td align='center'>&amp;</td><td align='left'>Character placeholder. Valid values for this placeholder are ANSI characters in the following ranges: 32-126 and 128-255.</td><td align='left'>MS</td></tr>
 * <tr><td align='center'>A</td><td align='left'>Alphanumeric character placeholder (<code>Character.isLetter</code> or <code>Character.isDigit</code>), with entry required. For example: a – z, A – Z, or 0 – 9.</td><td align='left'></td></tr>
 * <tr><td align='center'>a</td><td align='left'>Alphanumeric character placeholder (entry optional).</td><td align='left'>MS</td></tr>
 * <tr><td align='center'>9</td><td align='left'>Digit placeholder (entry optional). For example: 0 – 9.</td><td align='left'>MS</td></tr>
 * <tr><td align='center'>?</td><td align='left'>Letter placeholder (<code>Character.isLetter</code>). For example: a – z or A – Z.</td><td align='left'>MS, Swing</td></tr>
 * <tr><td align='center'>U</td><td align='left'>Any character (<code>Character.isLetter</code>). All lowercase letters are mapped to upper case.</td><td align='left'>Swing</td></tr>
 * <tr><td align='center'>L</td><td align='left'>Any character (<code>Character.isLetter</code>). All lowercase letters are mapped to lower case.</td><td align='left'>Swing</td></tr>
 * <tr><td align='center'>H</td><td align='left'>Character.isLetter  or Character.isDigit.</td><td align='left'>Swing</td></tr>
 * <tr><td align='center'>yy or yyyy</td><td align='left'>Year, eg 1996; 96.</td><td align='left'>DateFormat</td></tr>
 * <tr><td align='center'>MM</td><td align='left'>Two digit representation of month, eg 07 for July.</td><td align='left'>DateFormat</td></tr>
 * <tr><td align='center'>MMM</td><td align='left'>Three character representation of month, eg <i>Jul</i> for July.</td><td align='left'>DateFormat</td></tr>
 * <tr><td align='center'>d</td><td align='left'>Day in month, eg 3 or 28.</td><td align='left'>DateFormat</td></tr>
 * <tr><td align='center'>dd</td><td align='left'>Two digit representation of day in month, eg 03 or 28.</td><td align='left'>DateFormat</td></tr>
 * <tr><td align='center'>HH</td><td align='left'>Two digit representation of hour in day (24 hour clock), eg 05 or 19.</td><td align='left'>DateFormat</td></tr>
 * <tr><td align='center'>mm</td><td align='left'>Minute in hour, eg 02 or 47.</td><td align='left'>DateFormat</td></tr>
 * <tr><td align='center'>ss</td><td align='left'>Second in minute in hour, eg 08 or 35.</td><td align='left'>DateFormat</td></tr>
 * <tr><td align='center'>Literal</td><td align='left'>All other symbols are displayed as literals; that is, as themselves.</td><td align='left'>MS</td></tr>
 * </table>
 * 
 * <p>
 * The minimum length and maximum length are inferred from the length of the
 * mask string.
 * 
 * <p>
 * The annotation can be either to:
 * <ul>
 * <li>the getter of a string attribute, or
 * <li>a string parameter of an operation, or
 * <li>a value type that implements {@link IStringParser}.
 * </ul>
 * <p>
 * If the annotation is applied to a string attribute then it is applied when
 * the attribute is modified.  If there is no setter, then the annotation is 
 * still used to determine min and max length.  Applying the annotation to a 
 * non-string attribute has no effect and applying the annotation any other 
 * method (eg a method representing a domain operation) also has no effect.
 * <p>
 * If the annotation is applied to a string parameter of an operation then it
 * determines what can be entered into that parameter.  Again it will be ignored
 * if applied to a non-string parameter or to a string parameter of a method
 * that does not represent a domain operation.  
 * <p>
 * If the annotation is applied to a value type (with a <code>@Value</code>
 * annotation) that implements {@link IStringParser}, then any attribute or 
 * operation parameter of that value type will inherit the mask.  The 
 * annotation will be ignored if it is applied to any other type. 
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
public @interface Mask {
	String value();
	class Factory {
		public static Mask create(final String value) {
			return new Mask(){
				public String value() { return value; }
				public Class<? extends Annotation> annotationType() { return Mask.class; }
			};
		}
	}

}
