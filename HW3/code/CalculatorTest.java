import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class CalculatorTest {
	private static final String EOS = "e";

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			} catch (Exception e) {
				System.out.println("입력이 잘못되었습니다. 오류 : " + e);
			}
		}
	}


	/**
	 *
	 * @param input input String to calculate
	 *
	 */
	public static void command(String input) {
		try {
			// parse vocabs and replace unary minus
			List<String> infix = parseVocab(input);

			// validate infix format
			infixCheck(infix);

			// transform infix to postfix
			List<String> postfix = toPostfix(infix);

			long result = calculate(postfix);

			System.out.print(postfix.get(0));
			for(int i=1; i<postfix.size(); i++)
				System.out.printf(" %s", postfix.get(i));
			System.out.println();

			System.out.println(result);

		} catch (Exception e) {
			System.out.println("ERROR");
//			e.printStackTrace();
		}
	}

	public static long calculate(List<String> postfix) throws Exception{
		Stack<Long> calStack = new Stack<>();

		for(String vocab : postfix) {
			char c = vocab.charAt(0);
			if (isDigit(c)) {
				calStack.push(Long.parseLong(vocab));
			}
			else {
				if (isUnary(c)) {
					calStack.push(unaryOperate(c, calStack.pop()));
				} else {
					long B = calStack.pop(), A = calStack.pop();
					calStack.push(binaryOperate(c, A, B));
				}
			}
		}

		return calStack.pop();

	}

	// (3+4)*100000-5*(3^(5+3*2-4*(10-9)))
	// - 2749 / 3481 * - 59 + 7080

	public static List<String> toPostfix(List<String> infix) throws Exception {
		List<String> postfix = new ArrayList<>();
		Stack<Character> parentNodes = new Stack<>(); // parent nodes in computation tree

		for(String vocab : infix) {
			char c = vocab.charAt(0);
			if(isOperator(c) || c =='(' || c == ')') {
				while (!parentNodes.isEmpty() && comparePriority(parentNodes.peek(), c))
					postfix.add(Character.toString(parentNodes.pop()));
				parentNodes.push(c);

				if (c == ')') {
					parentNodes.pop(); parentNodes.pop();
				}
			}
			else postfix.add(vocab);
//			System.out.print("postfix : "); System.out.print(postfix);
//			System.out.print(" / stack : "); System.out.println(parentNodes);
		}

		postfix.remove(postfix.size()-1);
		while (!parentNodes.isEmpty())
			postfix.add(Character.toString(parentNodes.pop()));

		return postfix;
//		int depthParenthesis = 0, len = infix.length();
//		boolean isDigit = false;
//
//		Stack<Object> postfix = new Stack<>();
//		Stack<Integer> postParDepth = new Stack<>();
//
//		Stack<Character> operatorStack = new Stack<>();
//		Stack<Integer> opParDepth = new Stack<>();
//
//		StringBuilder numberBuilder = new StringBuilder();
//
//		for(int i=0; i<len; i++) {
//			char c = infix.charAt(i);
//			if (!isDigit && isDigit(c)) {
//				numberBuilder = new StringBuilder();
//				isDigit = true;
//			}
//			if (isDigit) {
//				numberBuilder.append(c);
//				isDigit = (i+1 < len) && isDigit(infix.charAt(i+1));
//
//				if (!isDigit) {
//					Object operand;
//					if(c!='z')
//						operand = Long.parseLong(numberBuilder.toString());
//					else
//						operand = 'z';
//					postfix.push(operand);
//
//					while (!operatorStack.isEmpty()) {
//						postfix.push(operatorStack.pop());
//						postParDepth.push(opParDepth.pop());
//					}
//				}
//			}
//			else {
//				if (c == '(') {
//					if(i!=0 && !isOperator(infix.charAt(i-1)) && infix.charAt(i-1) != '(')
//						throw new Exception("Invalid parenthesis (");
//
//					depthParenthesis++;
//				}
//
//				else if (c == ')') {
//					if(i==0 || !(isDigit(infix.charAt(i-1)) || infix.charAt(i-1) == ')'))
//						throw new Exception("Invalid parenthesis )");
//
//					depthParenthesis--;
//					if (depthParenthesis < 0)
//						throw new Exception("Invalid Parentheses");
//
//					while (!operatorStack.isEmpty()) {
//						postfix.push(operatorStack.pop());
//						postParDepth.push(opParDepth.pop());
//					}
//				}
//
//				else if (isOperator(c)) {
////					System.out.println(c);
//					int prevDepth = -1;
//					char prevOp = '+';
//					while (!postfix.empty() && postfix.peek() instanceof Character && (Character) postfix.peek() != 'z') {
//						char topOp = (Character) postfix.peek();
//						int topDepth = postParDepth.peek();
//
//						if(compareOp(prevOp, topOp, prevDepth, topDepth) && compareOp(topOp, c, topDepth, depthParenthesis)) {
//							operatorStack.push(topOp);
//							postfix.pop();
//							opParDepth.push(topDepth);
//							postParDepth.pop();
//							prevDepth = topDepth;
//							prevOp = topOp;
//						}
//						else break;
//					}
//
//					operatorStack.push(c);
//					opParDepth.push(depthParenthesis);
//				}
//			}
//
////			System.out.printf("%c, depth : %d\t", c, depthParenthesis);
////			System.out.print("postfix" + postfix + "\t");
////			System.out.print("postfixDepth" + postParDepth + "\t");
////			System.out.print("operatorStack" + operatorStack + "\t");
////			System.out.print("operatorDepth" + opParDepth + "\n");
//		}
//
//		if (depthParenthesis != 0)
//			throw new Exception("Invalid Parentheses");
//
//		// remove imaginary 0
//		Stack<Object> tmpStack = new Stack<>();
//		while(!postfix.empty()) {
//			Object top = postfix.pop();
//			if(!(top instanceof Character) || (char) top != 'z')
//				tmpStack.push(top);
//		}
//		while(!tmpStack.isEmpty()) {
//			postfix.push(tmpStack.pop());
//		}
//
//		return postfix;
	}

	/**
	 * true if o1 > o2
	 * @param c1 first operator
	 * @param c2 second operator
	 * @return o1 > o2
	 */
	private static boolean comparePriority(char c1, char c2) {
		if (c1 == '(' || c2 == '(') return false;
		if (c2 == ')') return true;

		int p1 = priority(c1), p2 = priority(c2);
		if (p1 > p2) return true;
		else if (p1 == p2) {
			return c1 != '^' && c1 != '~';
		}
		else return false;
	}

	// return priority of operators
	private static int priority(char c) {
		if (c=='+' || c=='-') return 1;
		else if (c=='*' || c=='/' || c=='%') return 2;
		else if (c=='~') return 3;
		else if (c=='^') return 4;
		else return -1;
	}

	/**
	 * A function to parse Vocabs from the raw input. Unary minus is also replaced to "~".
	 *
	 * There exist two parsing states;
	 *
	 *	1) begin of vocab (begin = true)
	 * 		case c (current looking character)
	 *			whitespace -> begin of vocab
	 *			operator, parenthesis  -> push c to stack -> begin of vocab
	 *			digit -> mark starting index of digits -> inside of vocab
	 *			EOS -> parsing ends
	 *
	 *  2) inside of vocab (begin = false)
	 * 		occurs only when current parsing vocab is number
	 *
	 *		c != digit (including EOS) -> push ended vocab -> start of vocab
	 *		c == digit -> inside of vocab
	 *
	 * @param input target input String to parse Vocabs
	 * @return Stack of parsed vocabs. Vocabs are stacked in reversed order.
	 */
	private static List<String> parseVocab(String input) {

		input += EOS; // add EOS character

		List<String> vocabs = new ArrayList<>();

		boolean begin = true; // true if previous character was Digit
		boolean isUnary = true; // true if this minus is unary; prev was BOS, Operator or (

		// tmp variables
		boolean isDigit, isOperator, isParenthesis;

		int digitLeft = 0; // index of character in the input where the current number began

		for (int i=0; i<input.length(); i++) {
			char c = input.charAt(i);

			isDigit = isDigit(c);
			isOperator = isOperator(c);
			isParenthesis = c == '(' || c == ')';

			if (!begin && !isDigit) {
				vocabs.add(input.substring(digitLeft, i));
				begin = true;
			}
			if (begin && isDigit) {

					digitLeft = i; begin = false;
			}
			else if (begin && (isOperator || isParenthesis)) {
				if (c == '-' && isUnary) vocabs.add("~");
				else vocabs.add(Character.toString(c));
			}

			isUnary = c == ' ' || c == '\t' ? isUnary : isOperator(c) || c == '(';
		}

		vocabs.add(EOS); // add EOS

		return vocabs;
	}

	/**
	 * Check if given infix is valid. If not, throws exceptions;
	 *
	 * Validate if all parentheses are matched, and state transition is proper.
	 *
	 * there are several types of Vocabs : number, binary operator, unary operator, (, ), EOS
	 * there are several states in infix : BOE (begin of expression), EOE (end of expression)
	 *
	 * only following state transition is valid (EOE at initial)
	 *
	 * BOE -> (	-> BOE
	 *     -> number -> EOE
	 *     -> unary operator -> BOE
	 * EOE -> ) -> EOE
	 * 	   -> EOS
	 * 	   -> binary operator -> BOE
	 *
	 * @param vocabs infix expression to validate
	 * @throws InvalidInfixException thrown when given infix is invalid
	 */

	private static void infixCheck(List<String> vocabs) throws InvalidInfixException, InvalidParenthesesException {
		boolean begin = true; // true if BOE, false if EOE
		boolean wasPow = false;
		int depth = 0;

		for(String vocab : vocabs) {
			char c = vocab.charAt(0);
			if (begin) {
				if (isDigit(c)) begin = false;
				else if (c == '(') depth++;
				else if (c == '~' && !wasPow) continue;
				else throw new InvalidInfixException();
			}
			else {
				if (c == ')') {
					depth--;
					if(depth < 0)
						throw new InvalidParenthesesException();
				}
				else if (isBinaryOperator(c)) begin = true;

				else if (vocab.equals(EOS)) break;
				else throw new InvalidInfixException();
			}
			wasPow = c == '^';
		}

		if (depth != 0)
			throw new InvalidParenthesesException();
	}

	private static class InvalidInfixException extends Exception {};
	private static class InvalidParenthesesException extends Exception {};

	private static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '^' || c == '/' || c == '%' || c == '*' || c == '~';
	}

	private static boolean isDigit(char c) {
		int charDist = c - '0';
		return 0 <= charDist && charDist < 10;
	}

	private static void removeBlank(StringBuilder s) {
		for(int i=0; i < s.length(); i++) {
			if (s.charAt(i) == '\t' || s.charAt(i) == ' ')
				s.deleteCharAt(i--);
		}
	}

	private static boolean isBinaryOperator(char c) {
		return isOperator(c) && !isUnary(c);
	}

	private static boolean isUnary(char c) {
		return c == '~';
	}

	private static long unaryOperate(char c, long a) throws Exception {
		if (c == '~')
			return -a;
		else
			throw new Exception("Invalid Operation");
	}

	private static long binaryOperate(char c, long a, long b) throws Exception {
		if (c == '+')
			return a + b;
		else if (c == '-')
			return a - b;
		else if (c == '*')
			return a * b;
		else if (c == '/')
			return a / b;
		else if (c == '%')
			return a % b;
		else if (c == '^') {
			if(a==0 && b < 0)
				throw new ArithmeticException("Invalid power operation");
			return (long) Math.pow(a, b);
		}

		else throw new Exception("Invalid operation");
	}

}