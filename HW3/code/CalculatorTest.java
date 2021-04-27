import java.io.*;
import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorTest {
	public static void main(String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			} catch (Exception e) {
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	public static void command(String input) {
		input = preprocessInput(input);
		Element[] inputElements = Element.parseElements(input);
		System.out.println("[Parsed Result]");
		for (Element element : inputElements) System.out.println(element);
	}

	// 공백 제거 및 input 형식 체크
	public static String preprocessInput(String input) {
		// 모든 whitespace 혹은 tab (\t)를 제거
		input = input.replaceAll("[ \t]+", "");

		Element.formatCheck(input);

		return input;
	}
}

class Expression {
	Operator operator;
	long result;

	Expression(Element[] elem) {
		int operatorIdx = findOperator(elem);
		operator = (Operator) elem[operatorIdx];
	}

	// TODO
	private long evaluate() {
		if(operator != null)
			return operator.evaluate();


	}

	private int findOperator(Element[] expr) {
		// TODO
//			int idx=0;
//
//			// 우선 순위가 가장 낮은 연산자를 찾는다.
//			for (idx = 0; idx < expr.length; idx++) {
//				if (!(expr[idx] instanceof Operator))
//			}
//
//			return idx;
		return 0;
	}

	public long getResult() {
		return result;
	}
}

abstract class Element {
	static Pattern ELEM_PATTERN = Pattern.compile("([0-9]+)(\\(.*\\))(\\^)((?![)0-9])-)(\\*)(/)(%)(\\+)((?=[)0-9])-)");

	abstract public long evaluate();
	abstract public String toString();

	public static Element[] parseElements(String input) {
		/*
			@param
			(String) input : preprocessed input String

			@return
			returns array of Elements
		 */

		formatCheck(input);

		int length;
		Element[] elements;

		Matcher matcher = ELEM_PATTERN.matcher(input);

		length = (int) matcher.results().count();
		assert length != 0;
		elements = new Element[length];

		matcher = ELEM_PATTERN.matcher(input);

		int idx=0;
		while(matcher.find()){
			if(!matcher.group(0).isEmpty()) {
				elements[idx++] = new Constant(matcher.group(0));
				continue;
			}

			if(!matcher.group(1).isEmpty()) {
				elements[idx++] = new Parenthesis(matcher.group(1));
				continue;
			}

			OperatorType[] operatorTypes = OperatorType.values();

			for(int i=1; i < operatorTypes.length; i++) {
				OperatorType operator = operatorTypes[i];

				if(!matcher.group(operator.idx).isEmpty()) {
					elements[idx++] = new Operator(operator);
					break;
				}
			}
		}

		return elements;
	}

	public static void formatCheck(String input) throws IllegalArgumentException {

		/*
			괄호의 내부는 Parenthesis 선언할 시 형식 체크함
		 */
		Matcher formatMatch = Pattern.compile("^$").matcher(input);
		if(!formatMatch.find())
			throw new InvalidParameterException();
	}
}

class Operator extends Element {
	Expression[] operands;
	OperatorType operatorType;

	Operator(OperatorType operatorType) {
		this.operatorType = operatorType;
	}

	@Override
	public long evaluate() {
		long[] operandResults = new long[operands.length];

		for(int i=0; i<operands.length; i++) {
			operandResults[i] = operands[i].getResult();
		}

		return operatorType.calculate(operandResults);
	}

	@Override
	public String toString() {
		return operatorType.printString;
	}
}

class Parenthesis extends Operator{
	Expression expr;
	Parenthesis (String string) {
		super(OperatorType.ParenthesisType);
		operands[0] = new Expression(Element.parseElements(string));
	}
}

class Constant extends Element {
	long value;

	Constant(String string) {
		value = Long.parseLong(string);
	}

	@Override
	public long evaluate() {
		return value;
	}

	@Override
	public String toString() {
		return null;
	}
}

enum OperatorType {
	ParenthesisType ("",1) {
		@Override
		public long calculate(long[] operands) {return operands[0];}
	},
	PowType ("^", 2){
		@Override
		public long calculate(long[] operands) {
			return (long) Math.pow(operands[0], operands[1]);
		}
	},
	MinusType ("~",3){
		@Override
		public long calculate(long[] operands) {return -operands[0];}
	},
	MultType ("\\*", 4){
		@Override
		public long calculate(long[] operands) {
			return operands[0] * operands[1];
		}
	},
	DivType ("/", 5){`
		@Override
		public long calculate(long[] operands) {
			return operands[0] / operands[1];
		}
	},
	ModType ("%", 6){
		@Override
		public long calculate(long[] operands) {
			return operands[0] % operands[1];
		}
	},
	Add ("\\+", 7){
		@Override
		public long calculate(long[] operands) {
			return operands[0] + operands[1];
		}
	},
	SubType ("-",8){
		@Override
		public long calculate(long[] operands) {
			return operands[0] - operands[1];
		}
	};

	private OperatorType(String print, int idx) {
		printString = print;
		this.idx = idx;
	}

	public abstract long calculate(long[] operands);
	public String patternString, printString;
	public int idx;
}