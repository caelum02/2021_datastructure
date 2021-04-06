import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger {
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";

    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("^[+-]?\\d+[*+-][+-]?\\d+$");
    private static final int MAX_INPUT_LENGTH = 100;
    private static final int MAX_NUM_DIGITS = 2*MAX_INPUT_LENGTH;

    private byte[] digits = new byte[MAX_NUM_DIGITS]; // 최대 200자리수까지 가능 + sign
    private byte sign;

    public BigInteger(int i) {
        sign = (byte) (i >= 0 ? 1 : -1);

        i *= sign;
        for (int n=0; n < MAX_INPUT_LENGTH; n++) {
            digits[n] = (byte) (i % 10);
            i /= 10;
        }

        for (int n=MAX_INPUT_LENGTH; n<MAX_NUM_DIGITS; n++)
            digits[n] = 0;
    }

    public BigInteger() {
        this(0);
    }

    public BigInteger(byte[] num1, byte sign) {
        this.sign = sign;

        if (num1.length != MAX_NUM_DIGITS)
            throw new IllegalArgumentException();

        this.digits = num1;
    }
  
    public BigInteger(String s) {
        int len = s.length();
        char firstChar = s.charAt(0);
        String absolute_val;

        if (firstChar == '+' || firstChar == '-') {
            absolute_val = s.substring(1, len);
            sign = (byte) (firstChar == '+' ? 1 : -1); len -= 1;
        } else {
            absolute_val = s;
            sign = 1;
        }

        digits = new byte[MAX_NUM_DIGITS];
        for (int i = 0; i < MAX_NUM_DIGITS; i++) {
            if (i < len)
                digits[i] = (byte) (absolute_val.charAt(len - 1 - i) - '0');
            else
                digits[i] = 0;
        }
    }

    public BigInteger add(BigInteger big)
    {
        byte[] digits1 = sign==1 ? digits : complement(digits), digits2 = big.sign==1 ? big.digits : complement(big.digits);

        byte[] resultDigits = addP(digits1, digits2); byte sign = 1;
        if(resultDigits[MAX_NUM_DIGITS-1] == 9) {
            resultDigits = complement(resultDigits); sign = -1;
        }

        return new BigInteger(resultDigits, sign);
    }

    public BigInteger subtract(BigInteger big)
    {
        return this.add(big.minus());
    }

    public BigInteger multiply(BigInteger big)
    {
        byte[] resultDigits = multiplyP(digits, big.digits);
        byte sign = (byte) (this.sign * big.sign);

        return new BigInteger(resultDigits, sign);
    }

    static private byte[] multiplyP(byte[] digits1, byte[] digits2) {
        byte[] result = new byte[MAX_NUM_DIGITS];
        for(int i=0; i<MAX_NUM_DIGITS;i++) result[i] = 0;
        for(int i=0; i<MAX_INPUT_LENGTH; i++) {
            byte[] mult = multiplyP(digits1, digits2[i]);
            result = addP(result, shift(mult, i));
        }

        return result;
    }

    static private byte[] multiplyP(byte[] digits, byte digit) {
        byte[] result = new byte[MAX_NUM_DIGITS]; byte carry = 0;
        for(int i=0; i<MAX_INPUT_LENGTH+1; i++) {
            byte sum = (byte) (carry + digits[i] * digit);
            result[i] = (byte) (sum % 10);
            carry = (byte) (sum / 10);
        }

        return result;
    }

    static private byte[] addP(byte[] digits1, byte[] digits2) {
        byte[] resultDigits = new byte[MAX_NUM_DIGITS];
        byte carry=0;

        for(int i=0;i<MAX_NUM_DIGITS; i++) {
            byte sum = (byte) (digits1[i] + carry + digits2[i]);
            resultDigits[i] = (byte) (sum % 10);
            carry = (byte) (sum / 10);
        }

        return resultDigits;
    }

    private BigInteger minus() {
        return new BigInteger(digits, (byte) -sign);
    }

    private static byte[] complement(byte[] digits) {
        if (digits.length != MAX_NUM_DIGITS)
            throw new IllegalArgumentException("Wrong length of digits.");

        byte[] complement = new byte[MAX_NUM_DIGITS];
        for(int i=0; i<MAX_NUM_DIGITS; i++)
            complement[i] = (byte) (9 - digits[i]);

        byte[] digitsOne = new byte[MAX_NUM_DIGITS]; digitsOne[0] = 1;

        return addP(complement, digitsOne);
    }

    private static byte[] shift(byte[] digits, int i) {
        if (i<0)
            throw new IllegalArgumentException("only left shift possible");

        byte[] result = new byte[MAX_NUM_DIGITS];

        for(int j=0; j<i; j++)
            result[j] = 0;

        for(int j=i; j<MAX_NUM_DIGITS; j++)
            result[j] = digits[j-i];

        return result;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        boolean digitStart = false;
        boolean isZero = false;

        int i=MAX_NUM_DIGITS-1;
        while(i>=0 && digits[i]==0) { i--; }
        if(i==-1) isZero = true;

        for(; i>=0; i--) {
            string.append(digits[i]);
        }
        if (isZero)
            return "0";
        else if(sign==-1)
            return "-" + string.toString();
        else
            return string.toString();
    }
  
    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        // remove all whitespace
        input = input.replaceAll("\\s+", "");

        // input 주어진 형식에 맞는지 체크
        if (!isValidInput(input))
            throw new IllegalArgumentException();

        String arg1, arg2, operator;

        // 숫자 매칭 (부호는 그리디하게 숫자로 포함)
        Pattern numPattern = Pattern.compile("[+-]?\\d+");
        Matcher numMatcher = numPattern.matcher(input);

        numMatcher.find(); arg1 = numMatcher.group();
        numMatcher.find(); arg2 = numMatcher.group();

        // 연산자 매칭
        // 숫자 사이에 연산자 하나있을 경우 : operator는 +라고 간주함)
        // 두개 있을 경우에는 첫번째가 operator, 두번째는 부호가 됨

        // 연산자 두개 있을때에만 match 성공
        Pattern operatorPattern = Pattern.compile("(\\*(?=[+-]?\\d)|[+-](?=[+-]\\d))");
        Matcher operatorMatcher = operatorPattern.matcher(input);

        if (operatorMatcher.find()) { // 연산자 존재할 경우
            operator = operatorMatcher.group();
        } else { // 연산자 없을 경우
            operator = "+";
        }

        return query(arg1, arg2, operator);
    }

    static boolean isValidInput(String input) {
        Matcher match = EXPRESSION_PATTERN.matcher(input);
        return match.find();
    }

    static BigInteger query(String arg1, String arg2, String operator) {
        BigInteger num1 = new BigInteger(arg1), num2 = new BigInteger(arg2);

        switch (operator) {
            case "+":
                return num1.add(num2);
            case "-":
                return num1.subtract(num2);
            case "*":
                return num1.multiply(num2);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
