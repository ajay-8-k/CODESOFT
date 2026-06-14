package q11053;
import java.util.Scanner;
	public class AdditionOfMatrix {

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		
		System.out.println("Matrix 1:");
		System.out.print("Enter number of rows: ");
		int r1 = scan.nextInt();
		System.out.print("Enter number of columns: ");
		int c1 = scan.nextInt();
		int[][] mat1 = new int[r1][c1];
		

		System.out.println("Enter "+c1+" number separated by space");
		for (int i = 0; i < r1; i++) {
			System.out.print("Enter row "+(i+1)+": ");
			for (int j = 0; j < c1; j++) {
				mat1[i][j] = scan.nextInt();
			}
		}

		System.out.println("Matrix 2:");
		System.out.print("Enter number of rows: ");
		int r2 = scan.nextInt();
		System.out.print("Enter number of columns: ");
		int c2 = scan.nextInt();
		int[][] mat2 = new int[r2][c2];

		System.out.println("Enter "+c2+" numbers separated by space");
		for (int i = 0; i< r2; i++) {
			System.out.print("Enter row "+(i+1)+": ");
			for (int j = 0; j < c2; j++) {
				mat2[i][j] = scan.nextInt();
			}
		}

		
		
		if(r1 != r2 || c1 != c2)
		{
		    System.out.println("Addition of different sized matrices is not possible");
		}
		else
		{
		    int[][] result = new int[r1][c1];
		for(int i = 0 ; i < r1 ; i ++)
		{
		    for(int j=0; j < c1 ;j++)
		    {
		        result[i][j] = mat1[i][j] * mat2[i][j] ;
		    }
		}
		System.out.println("Sum of the two given matrices is");
		
		for(int i = 0 ; i < r1 ;i++)
		{
		    for(int j = 0 ;j < c1 ;j++)
		    {
		        
		        System.out.print(result[i][j] + " ");
		    }
		    System.out.println();
		}
     }
	n
	}
}

