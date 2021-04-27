import java.io.*;
import java.util.Stack;

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
		try {
			String infix = preprocess(input);
			System.out.println(infix);
			Stack<Object> postfix = toPostfix(infix);

		} catch (Exception e) {
			System.out.println("ERROR");
		}
	}

	public static Stack<Object> toPostfix(String infix) throws Exception {
		int numParenthesis = 0, len = infix.length();
		boolean isOpen = false, wasNum = false;
		Stack<Object> Postfix = new Stack<>(); Stack<Character> operatorStack = new Stack<>();
		StringBuilder numberBuilder = new StringBuilder();

		for(int i=0; i<len; i++) {
			char c = infix.charAt(i);
			if (isDigit(c)) {
				if(!wasNum) {
					numberBuilder = new StringBuilder();
					wasNum = true;
				}

				numberBuilder.append(c);

			} else {
				if(wasNum) {
					wasNum = false;

					Postfix.push(
							Integer.parseInt(numberBuilder.toString())
					);
					Postfix.push(operatorStack.pop());
				}

				if (c == '(') {
					numParenthesis++;
					isOpen = true;
				} else if (c == ')') {
					numParenthesis--;
					if (numParenthesis < 0)
						throw new Exception("Invalid Parentheses");
					isOpen = false;

					Postfix.push(operatorStack.pop());
				} else if (isOperator(c)) {
					if(isOpen) operatorStack.push(c);

				}
			}
		}

		if (numParenthesis != 0)
			throw new Exception("Invalid Parentheses");

		return Postfix;
	}


	private static String preprocess(String s) throws Exception{
		StringBuilder stringBuilder = new StringBuilder(s);

		removeBlank(stringBuilder);
		if(invalidChar(stringBuilder))
			throw new Exception("Found invalid character in input");

		replaceUnaryMinus(stringBuilder);
		Parenthesize(stringBuilder);

		return stringBuilder.toString();
	}

	private static void Parenthesize(StringBuilder stringBuilder) {
		stringBuilder.insert(0, '(');
		stringBuilder.append(')');
	}

	// 입력에 적절하지 않은 문자가 존재할 경우 true 리턴
	private static boolean invalidChar(StringBuilder s){
		for(int i=0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(!(isDigit(c) || (isOperator(c) && c != '~') || c == '(' || c==')'))
				return true;
		}

		return false;
	}

	private static void replaceUnaryMinus(StringBuilder s) {
		char prev = '(';
		for(int i=0; i < s.length(); i++) {
			char curr = s.charAt(i);
			if (curr == '-' && (isOperator(prev) || prev == '(')) {
				s.setCharAt(i, '~');
			}
			prev = curr;
		}
	}

	private static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '^' || c == '/' || c == '%' || c == '*' || c == '~';
	}

	private static boolean isDigit(char c) {
		return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
	}

	private static void removeBlank(StringBuilder s) {
		for(int i=0; i < s.length(); i++) {
			if (s.charAt(i) == '\t' || s.charAt(i) == ' ')
				s.deleteCharAt(i--);
		}
	}

//	private static String
//
//	private static int oprPriority(char o1, char o2) {
//		if(o1 == o2) {
//
//		}
//	}
}

//	static Pattern ELEM_PATTERN = Pattern.compile("([0-9]+)(\\(.*\\))(\\^)((?![)0-9])-)(\\*)(/)(%)(\\+)((?=[)0-9])-)");
//
//enum OperatorType {
//	ParenthesisType ("",1) {
//		@Override
//		public long calculate(long[] operands) {return operands[0];}
//	},
//	PowType ("^", 2){
//		@Override
//		public long calculate(long[] operands) {
//			return (long) Math.pow(operands[0], operands[1]);
//		}
//	},
//	MinusType ("~",3){
//		@Override
//		public long calculate(long[] operands) {return -operands[0];}
//	},
//	MultType ("\\*", 4){
//		@Override
//		public long calculate(long[] operands) {
//			return operands[0] * operands[1];
//		}
//	},
//	DivType ("/", 5){`
//		@Override
//		public long calculate(long[] operands) {
//			return operands[0] / operands[1];
//		}
//	},
//	ModType ("%", 6){
//		@Override
//		public long calculate(long[] operands) {
//			return operands[0] % operands[1];
//		}
//	},
//	Add ("\\+", 7){
//		@Override
//		public long calculate(long[] operands) {
//			return operands[0] + operands[1];
//		}
//	},
//	SubType ("-",8){
//		@Override
//		public long calculate(long[] operands) {
//			return operands[0] - operands[1];
//		}
//	};
//
//	private OperatorType(String print, int idx) {
//		printString = print;
//		this.idx = idx;
//	}
//
//	public abstract long calculate(long[] operands);
//	public String patternString, printString;
//	public int idx;
//}