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

	private static String preprocessInput(String input) {
		return input.replaceAll("[ \t]+", "");
	}

	private static void command(String input) {

	}

	private class Expression {
		Operator operator;
		long result;

		Expression(Element[] elem) {
			int operatorIdx = findOperator(elem);

			operator = (Operator) elem[operatorIdx];

		}

		private long evaluate() {
			result = operator.operate();
		}

		private int findOperator(Element[] expr) {
			int idx;

			// 우선 순위가 가장 낮은 연산자를 찾는다.
			for (idx = 0; idx < expr.length; idx++) {
				if (!(expr[idx] instanceof Operator))
			}

			return idx;
		}
	}

	private abstract class Element {

	}

	private class Operator extends Element{
		Expression[] operands;
		OperatorType operatorType;

		public long operate() {

		}
	}

	private enum OperatorType {
		Bracket {
			long calculate(long operand) {return operand;}
		},
		Pow {
			long calculate(long operand1, long operand2) {
				return (long) Math.pow(operand1, operand2);
			}
		},
		Minus {
			long calculate(long operand) {return -operand;}
		},

	}

}




