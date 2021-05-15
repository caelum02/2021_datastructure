import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	private static void swap(int[] value, int i, int j) {
		int tmp = value[i];
		value[i] = value[j];
		value[j] = tmp;
	}

	private static void insert(int[] value, int i, int length, int item) {
		if (length - i >= 0) System.arraycopy(value, i, value, i + 1, length - i);
		value[i] = item;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value)
	{
		// TODO : Bubble Sort 를 구현하라.
		// value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
		// 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
		// 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
		// 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.

		int length = value.length;
		for (int i=length-1; i>=0; i--) {
			for (int j=0; j<i; j++) {
				if (value[j]>value[j+1])
					swap(value, j, j+1);
			}
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{
		// TODO : Insertion Sort 를 구현하라.
		int length = value.length;
		for (int i=1; i<length; i++) {
			int j = i-1, curr = value[i];
			while (j >= 0 && value[j] > curr) j--;
			insert(value, j+1, i, curr);
		}

		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value)
	{
		// TODO : Heap Sort 를 구현하라.
		int n = value.length;

		// heapify
		for (int i=(n-2)/2; i>=0; i--) {
			int j=i;

			// percolateDown
			while (2*j+1 < n) {
				int child = 2*j+1, right = 2*j+2;
				if (right < n && value[child] < value[right])
					child = right;
				if (value[child] > value[j]) {
					swap(value, child, j);
					j = child;
				} else break;
			}
		}
		while(n-- > 0) {
			int j = 0;
			swap(value, n, 0);
			while (2*j+1 < n) {
				int child = 2*j+1, right = 2*j+2;
				if (right < n && value[child] < value[right])
					child = right;
				if (value[child] > value[j]) {
					swap(value, child, j);
					j = child;
				} else break;
			}
		}

		return value;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value)
	{
		// TODO : Merge Sort 를 구현하라.
		int[] value_ = value.clone();
		mergeSort(value, value_, 0, value.length);
		return (value);
	}

	private static void mergeSort(int[] A, int[] B, int l, int r) {
		if (r-l <= 1) return;
		int m = (l+r) / 2;

		mergeSort(B, A, l, m);
		mergeSort(B, A, m, r);

		int p1=l, p2=m, p0=l;
		while(p1 < m && p2 < r) {
			if (B[p1] < B[p2])
				A[p0++] = B[p1++];
			else A[p0++] = B[p2++];
		}
		while(p1 < m)
			A[p0++] = B[p1++];
		while(p2 < r)
			A[p0++] = B[p2++];
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value)
	{
		// TODO : Quick Sort 를 구현하라.
		quickSort(value, 0, value.length);

		return (value);
	}

	private static void quickSort(int[] value, int l, int r) {
		if (r-l <= 1) return;

		int p = r-1, x = value[p];
		int i=l;
		for (int j=l; j<r-1; j++) {
			if(value[j] < x)
				swap(value, i++, j);
		}
		swap(value, i, p);
		quickSort(value, l, i);
		quickSort(value, i+1, r);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value)
	{
		// TODO : Radix Sort 를 구현하라.
		int n = value.length;

		int pow = 1;
		while (true) {
			int[][] byDigit = new int[19][n];
			int[] p = new int[19];

			for (int j : value) {
				int digit = (j / pow) % 10 + 9;
				byDigit[digit][p[digit]++] = j;
			}

			if (p[9] == n) break;

			int idx = 0;
			for (int digit=0; digit<19; digit++) {
				System.arraycopy(byDigit[digit], 0, value, idx, p[digit]);
				idx += p[digit];
			}

			pow *= 10;
		}

		return (value);
	}
}
