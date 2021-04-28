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

			StringBuilder postfixBuilder = new StringBuilder();

			for(Object o : postfix) {
				postfixBuilder.append(o.toString());
				postfixBuilder.append(' ');
			}

			System.out.println(calculate(postfix));
			System.out.println(postfixBuilder.toString());

		} catch (Exception e) {
			System.out.println("ERROR");
//			System.err.println(e);
			e.printStackTrace();
		}
	}

	public static int calculate(Stack<Object> postfix) throws Exception{
		int result = 0;
		boolean wasInt = false;
		Stack<Object> calStack = new Stack<>();

		while (!postfix.isEmpty()) {
			Object o = postfix.pop();
			if (o instanceof Character) {
				char c = (Character) o; operators.push(c);
				wasInt = false;
			} else if (o instanceof Integer) {
				int operand = (Integer) o;
				if (!wasInt) wasInt = true;
				else {

				}
			}
		}

		return result;

	}

	public static Stack<Object> toPostfix(String infix) throws Exception {
		int depthParenthesis = 0, len = infix.length();
		boolean wasDigit = false;
		Stack<Object> postfix = new Stack<>();
		Stack<Integer> postParDepth = new Stack<>();

		Stack<Character> operatorStack = new Stack<>();
		Stack<Integer> opParDepth = new Stack<>();

		StringBuilder numberBuilder = new StringBuilder();

		for(int i=0; i<len; i++) {
			char c = infix.charAt(i);
			if (isDigit(c)) {
				if (!wasDigit) {
					wasDigit = true;
					numberBuilder = new StringBuilder();
				}

				numberBuilder.append(c);
			} else {
				if (wasDigit) {
					wasDigit = false;

					Integer operand = Integer.parseInt(numberBuilder.toString());
					postfix.push(operand);

					while(!operatorStack.isEmpty()) {
						postfix.push(operatorStack.pop());
						postParDepth.push(opParDepth.pop());
					}
				}

				if (c == '(') {
					depthParenthesis++;
				}

				else if (c == ')') {
					depthParenthesis--;
					if (depthParenthesis < 0)
						throw new Exception("Invalid Parentheses");

					while(!operatorStack.isEmpty()) {
						postfix.push(operatorStack.pop());
						postParDepth.push(opParDepth.pop());
					}
				}

				else if (isOperator(c)) {
					Integer depth = postParDepth.empty() ? -1 : postParDepth.peek();
					while(!postfix.empty() && postfix.peek() instanceof Character && postParDepth.peek() <= depth) {
						Character prevOp = (Character) postfix.pop();
						depth = postParDepth.pop();

						if (depth < depthParenthesis || (depth == depthParenthesis && compareOp(prevOp, c))) {
							operatorStack.push(prevOp);
							opParDepth.push(depth);
						} else {
							postfix.push(prevOp);
							postParDepth.push(depth);
							break;
						}
					}
					operatorStack.push(c);
					opParDepth.push(depthParenthesis);
				}
			}

//			System.out.printf("%c, depth : %d\n", c, depthParenthesis);
//			System.out.println("postfix" + postfix);
//			System.out.println("postfixDepth" + postParDepth);
//			System.out.println("operatorStack" + operatorStack);
//			System.out.println("operatorDepth" + opParDepth);
		}

		if (depthParenthesis != 0)
			throw new Exception("Invalid Parentheses");

		return postfix;
	}

	// compare priority of operators
	// true if c1 < c2
	private static boolean compareOp(char c1, char c2) {
		int p1 = priority(c1), p2 = priority(c2);
		if (p1 < p2) return true;
		else if (p1 == p2) {
			return c1 == '^';
		}
		else return false;
	}

	// return priority of operators
	private static int priority(char c) {
		if (c=='+' || c=='-') return 0;
		else if (c=='*' || c=='/' || c=='%') return 1;
		else if (c=='~') return 2;
		else if (c=='^') return 3;
		else return -1;
	}

	private static String preprocess(String s) throws Exception{
		StringBuilder stringBuilder = new StringBuilder(s);

		removeBlank(stringBuilder);
		if(invalidChar(stringBuilder))
			throw new Exception("Found invalid character in input");

		replaceUnaryMinus(stringBuilder);
		parenthesize(stringBuilder);

		return stringBuilder.toString();
	}

	private static void parenthesize(StringBuilder stringBuilder) {
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