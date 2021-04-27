import java.io.*;

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
			input = preprocess(input);
			System.out.println(input);

		} catch (Exception e) {
			System.out.println("ERROR");
		}
	}
	
	private static String preprocess(String s) throws Exception{
		StringBuilder stringBuilder = new StringBuilder(s);

		removeBlank(stringBuilder);
		if(invalidChar(stringBuilder))
			throw new Exception("Found invalid character in input");

		replaceUnaryMinus(stringBuilder);

		return stringBuilder.toString();
	}

	// 입력에 적절하지 않은 문자가 존재할 경우 true 리턴
	private static boolean invalidChar(StringBuilder s){
		for(int i=0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(!(isOperand(c) || (isOperator(c) && c != '~')))
				return true;
		}

		return false;
	}

	private static void replaceUnaryMinus(StringBuilder s) {
		char prev = '(';
		for(int i=0; i < s.length(); i++) {
			char curr = s.charAt(i);
			if (curr == '-' && isOperator(prev)) {
				s.setCharAt(i, '~');
			}
			prev = curr;
		}
	}

	private static boolean isOperator(char c) {
		return c == '(' || c == '+' || c == '-' || c == '^' || c == '/' || c == '%' || c == '*' || c == '~';
	}

	private static boolean isOperand(char c) {
		return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == ')';
	}

	private static void removeBlank(StringBuilder s) {
		for(int i=0; i < s.length(); i++) {
			if (s.charAt(i) == '\t' || s.charAt(i) == ' ')
				s.deleteCharAt(i--);
		}
	}

	private static boolean isUnary(char c) {
		return c == '~';
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