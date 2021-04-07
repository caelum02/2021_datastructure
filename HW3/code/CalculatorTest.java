import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorTest {
	public void main(String args[]) {
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

	public void command(String input) {
		String processedInput = preprocessInput(input);
		Element[] inputElements = parseElements(processedInput);
		for(Element element : inputElements) System.out.println(element);
	}

	private static String preprocessInput(String input) {
		return input.replaceAll("[ \t]+", "");
	}

	private Element[] parseElements(String input) {
		/*
			@param
			(String) input : preprocessed input String

			@return
			returns array of Elements
		 */

		int length=0;
		Element[] elements;

		StringBuilder patternString = new StringBuilder();
		for (OperatorType operator : OperatorType.values()) {
			patternString.append("(").append(operator.patternString).append(")");
		}

		Pattern pattern = Pattern.compile(patternString.toString());
		Matcher matcher = pattern.matcher(input);

		length = (int) matcher.results().count();
		elements = new Element[length];

		matcher = pattern.matcher(input);

		int idx=0;
		while(matcher.find()){
			if(!matcher.group(0).isEmpty()) {
				elements[idx++] = new Constant(matcher.group(0));
				continue;
			}
			for(OperatorType operator : OperatorType.values()) {
				if(!matcher.group(operator.idx).isEmpty()) {
					elements[idx++] = new Operator(operator);
					break;
				}
			}
		}

		return elements;
	}

	private class Expression {
		Operator operator;
		long result;

		Expression(Element[] elem) {
			int operatorIdx = findOperator(elem);
			operator = (Operator) elem[operatorIdx];

		}

		private long evaluate() {
			return operator.evaluate();
		}

		private int findOperator(Element[] expr) {
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

	private abstract class Element {
		abstract public long evaluate();
		abstract public String toString();
	}

	public class Operator extends Element {
		Expression[] operands;
		OperatorType operatorType;

		Operator(OperatorType operatorType) {
			this.operatorType = operatorType;
		}

		public void setOperands(Expression[] operands) {
			this.operands = operands;
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

	private class Constant extends Element {
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

	private enum OperatorType {
		Bracket ("()", "",1) {
			@Override
			public long calculate(long[] operands) {return operands[0];}
		},
		Pow ("^", 2){
			@Override
			public long calculate(long[] operands) {
				return (long) Math.pow(operands[0], operands[1]);
			}
		},
		Minus ("(?![)0-9])-", "~",3){
			@Override
			public long calculate(long[] operands) {return -operands[0];}
		},
		Mult ("*", 4){
			@Override
			public long calculate(long[] operands) {
				return operands[0] * operands[1];
			}
		},
		Div ("/", 5){
			@Override
			public long calculate(long[] operands) {
				return operands[0] / operands[1];
			}
		},
		Mod ("%", 6){
			@Override
			public long calculate(long[] operands) {
				return operands[0] % operands[1];
			}
		},
		Add ("+", 7){
			@Override
			public long calculate(long[] operands) {
				return operands[0] + operands[1];
			}
		},
		Sub ("(?=[)0-9])-", "-",8){
			@Override
			public long calculate(long[] operands) {
				return operands[0] - operands[1];
			}
		};

		private OperatorType(String regex, String print, int idx) {
			patternString = regex;
			printString = print;
			this.idx = idx;
		}

		private OperatorType(String regex, int idx) {
			this(regex, regex, idx);
		}

		public abstract long calculate(long[] operands);
		public String patternString, printString;
		public int idx;
	}

}




