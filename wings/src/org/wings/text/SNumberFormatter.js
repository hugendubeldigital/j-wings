var old;

function saveOld (__field) {
	old = __field.value;
}

function numberFormatter (__minIntDig, __maxIntDig, __minFracDig, __maxFracDig, __minVal, __maxVal, __groupUsed, __intOnly, __decSep, __groSep, __field) {

        var re;
	
        if ( __groupUsed ) {
                var regexpString = "(^[0-9]*$)|(^[0-9]*\\"+ __decSep +"[0-9]*$)|(^[0-9]{1,3}\\"+ __groSep +"([0-9]{3}\\"+ __groSep +")*[0-9]{3}\\"+ __decSep +"[0-9]*$)";
                re = new RegExp( regexpString );
	} else {
                var regexpString = "(^[0-9]*$)|(^[0-9]*\\"+ __decSep +"[0-9]*$)";
                re = new RegExp( regexpString );
	}
	
	if ( re.test( __field.value ) ) {
                var regexpString = "\\"+__decSep;

                // alert("DecimalSepReg ="+ regexpString); 
                var searchPoint = new RegExp(regexpString);

		var splitValue = __field.value.search(searchPoint);
		if (splitValue == -1) {
                        // alert("splitValue == -1");
			var integerDigits  = __field.value;
			var fractionDigits = "";
		} else {
			var split = new String(__field.value);
			var fractionDigits = split.substring(splitValue + 1);
			var integerDigits = split.substring(0,splitValue);
                        // alert( "FracDig : " + fractionDigits + " IntDig : " + integerDigits ); 
		}
		if ( integerDigits.length > __maxIntDig) {
                        // alert("IntDig("+integerDigits.length+") > maxIntDig("+__maxIntDig+")");
			__field.focus();  
			return false; 
		}
		if (integerDigits.length < __minIntDig) { 
                        // alert("IntDig("+integerDigits.length+") < minIntDig("+__minIntDig+")");
			var fillInt = parseInt( __minIntDig ) - fractionDigits.length; 
			for (var i = 0; i < fillInt; i++){ 
				integerDigits =  "0" + integerDigits ; 
			}
                        __field.value = integerDigits + __decSep + fractionDigits;
			i = __field.value.search(searchPoint);
			var number = __field.value;
			integerDigits = number.substring(0, i);
			fractionDigits = number.substring(i+1, number.length );
            	}
		if (fractionDigits.length > __maxFracDig ){
                        // alert("FracDig("+fractionDigits.length+") > maxFracDig("+ __maxFracDig + ")");  
                        var round = fractionDigits.substring(__maxFracDig-1,__maxFracDig) +"."+ fractionDigits.substring( __maxFracDig );
                        round = new String(Math.round( round ));
                        var newFracDig = fractionDigits.substring(0, __maxFracDig-1) + round;
                        __field.value = integerDigits + __decSep + newFracDig;
            	}
		if (fractionDigits.length < __minFracDig && fractionDigits.length >= 0 ){
                        // alert("FracDig("+fractionDigits.length+") < minFracDig(" + __minFracDig + ") && FracDig("+fractionDigits.length+") >= 0");
                        var fillFrac = new String( fractionDigits );
                        for(var i = 0; i < __minFracDig - fractionDigits.length; i++) {
                            fillFrac = fillFrac + "0";
                        }
                        fractionDigits = fillFrac;
                        __field.value = integerDigits + __decSep + fractionDigits; 
            	}
		if ( __field.value < __minVal ) {
                        // alert("Field.value("+__field.value+") < minVal("+__minVal+")");
			__field.value = __minVal;	
			return false;
		}
		if ( __field.value > __maxVal) {
			// alert("Field.value("+__field.value+") > maxVal("+__maxVal+")");
                        __field.value = __maxVal;
			return false;
		}
		if ( __intOnly ) {
			__field.value = Math.round( __field.value );
		}
		return true;
	} else {
		__field.value = old;
		return true;
	}
}