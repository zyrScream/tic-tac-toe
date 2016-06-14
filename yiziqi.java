package testPackage;

import java.util.Scanner;

import javax.xml.bind.helpers.ValidationEventImpl;
/**
 * author:zyrScream
 * mail:zyrScream@163.com
 */

public class yiziqi {
	private int chessSize=3;
	private int openLength=9;
	private int openTailPointer=0;
	private int openHeadPointer=0;
	private int[][] openTable=new int[openLength][10];
	private int[][] currentState	=new int[chessSize][chessSize];
	private int secondTableLength=9;
	private int secondPointer=0;
	private int[][] secondTable=new int[secondTableLength][11];	
	private int whoseTurn;
	private int[] tempState=new int[10];
	
	//构造函数
	public yiziqi() {
		
	}
	
	/**
	 * 初始化棋盘
	 * @param temp
	 */
	public  void initialChessBoard(int[][] temp) {
		for(int i=0;i<temp.length;i++)
			for(int j=0;j<temp[i].length;j++) {
				temp[i][j]=-1;
			}
	}
	
	/**
	 * 打印棋盘
	 * @param temp
	 */
	public   void printChessBoard(int[][] temp) {
		for(int i=0;i<temp.length;i++) {
			for(int j=0;j<temp[i].length;j++) {
				System.out.print(temp[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	/**
	 * 找到数组temp 中的空缺位置
	 * @param temp
	 * @return array 一维数组中存储的是数组中空缺的位置
	 */
	public int[] findEmptyInArray(int temp[][]) {
		int[] array=new int[chessSize*chessSize];
		for(int i=0;i<array.length;i++) {
			array[i]=-1;
		}
		int arrayPointer=0;
		for(int i=0;i<temp.length;i++)
			for(int j=0;j<temp[i].length;j++) {
				if(temp[i][j]==-1) {
					array[arrayPointer++]=chessSize*i+j;
				}
			}
		return array;
	}
	
	/**
	 * CurrentState 当前数组的情况  insertIntoOpen 当前玩家可走的情况放入openTable中
	 * @param temp 当前数组空缺的位置
	 * @param player 当前玩家编号
	 */
	public void insertIntoOpen(int[] temp,int player) {
		int tempPointer=0;int[] arraylist=new int[chessSize*chessSize];
		while(tempPointer<temp.length&&temp[tempPointer]!=-1) {
			int index=temp[tempPointer++];		
				for(int i=0;i<currentState.length;i++) {
					for(int j=0;j<currentState[i].length;j++) {
						if((3*i)+j==index) {
							openTable[openTailPointer][3*i+(j+1)]=player;
						}else {
						openTable[openTailPointer][3*i+(j+1)]=currentState[i][j];
						}
					}	
				}
				
				for(int i=1;i<openTable[openTailPointer].length;i++) {
					arraylist[i-1]=openTable[openTailPointer][i];
				}
				
				if(calculateNum(arraylist, player)>0) {
					openTable[openTailPointer][0]=Integer.MAX_VALUE;
				}
				openTailPointer++;
		}
	}
	
	/**
	 * 从openTable中读取一条记录到tempState数组中
	 */
	public void openTableIntoTempState() {
		tempState[0]=openHeadPointer;
 		int[] temp=openTable[openHeadPointer++];
		for(int i=1;i<temp.length;i++) {
			tempState[i]=temp[i];
		}
	}
	/**
	 * 一维数组变成二维数组
	 * @param temp
	 * @return
	 */
	public int[][] changeOneDimensionalToTwoDimensional(int[] temp,int head) {
		int[][] change=new int[chessSize][chessSize];
		
		for(int i=head;i<temp.length;i++) {
			change[(i-head)/chessSize][(i-head)%chessSize]=temp[i];
		}
		
		return change;
	}
	
	/**
	 *预测另一个玩家可能走得情况，将情况放入secondTable
	 */
	public void insertIntoSecondTable() {
		int[][] temp=changeOneDimensionalToTwoDimensional(tempState,1);
		int fatherPoint=tempState[0];
		int[] array=findEmptyInArray(temp);
		
		if(whoseTurn==1) {//原来是玩家1，现在变成玩家2
			int arrayPointer=0;
			int[] arraylist=new int[chessSize*chessSize];
			while(arrayPointer<array.length&&array[arrayPointer]!=-1) {
				int index=array[arrayPointer++];
				secondTable[secondPointer][0]=fatherPoint;
				for(int i=0;i<temp.length;i++) {
					for(int j=0;j<temp[i].length;j++)
						if((3*i)+j==index) {
							secondTable[secondPointer][3*i+(j+2)]=2;
						}else {
							secondTable[secondPointer][3*i+(j+2)]=temp[i][j];
						}
					}

				for(int i=2;i<secondTable[secondPointer].length;i++) {
					arraylist[i-2]=secondTable[secondPointer][i];
				}
				
				if(calculateNum(arraylist, 2)>0) {
					secondTable[secondPointer][1]=Integer.MIN_VALUE;
				}
				secondPointer++;
				}
			}
		else {//原来是玩家2，现在是玩家1
			int arrayPointer=0;
			int[] arraylist=new int[chessSize*chessSize];
			while(arrayPointer<array.length&&array[arrayPointer]!=-1) {
				int index=array[arrayPointer++];
				secondTable[secondPointer][0]=fatherPoint;
				for(int i=0;i<temp.length;i++) {
					for(int j=0;j<temp[i].length;j++)
						if((3*i)+j==index) {
							secondTable[secondPointer][3*i+(j+2)]=1;
						}else {
							secondTable[secondPointer][3*i+(j+2)]=temp[i][j];
						}
					}
				
				for(int i=2;i<secondTable[secondPointer].length;i++) {
					arraylist[i-2]=secondTable[secondPointer][i];
				}
				
				if(calculateNum(arraylist, 1)>0) {
					secondTable[secondPointer][1]=Integer.MIN_VALUE;
				}
				secondPointer++;
				}
		}	
	}
	
	public int[] findEmptyInSecondTable(int[] temp) {
		int[] array=new int[temp.length];
		int j=0;
		for(int i=0;i<temp.length;i++) {
			if(temp[i]==-1) {
				array[j++]=i;
			}
		}
		return array;
	}
	
	public int estimateSecondTable(int[] temp,int player) {
		 int result;
		 int[] array1=new int[chessSize*chessSize];
		 int[] array2=new int[chessSize*chessSize];
		 System.arraycopy(temp, 2, array1, 0, array1.length);
		 System.arraycopy(temp, 2, array2, 0, array2.length);
		 
		 fullArrayWith(array1,1);
		 fullArrayWith(array2,2);
		 if(player==1) {
			 result=calculateNum(array1, 1)-calculateNum(array2, 2);
		 }else {
			 result=calculateNum(array2, 2)- calculateNum(array1, 1);
		 }
		 return result;
	}
	
	public int calculateNum(int[] temp,int player) {
		int num=0;
		if(temp[0]==temp[1]&&temp[1]==temp[2]&&temp[0]==player) {
			num++;
		}
		if(temp[3]==temp[4]&&temp[4]==temp[5]&&temp[3]==player) {
			num++;
		}
		if(temp[6]==temp[7]&&temp[7]==temp[8]&&temp[8]==player) {
			num++;
		}
		if(temp[0]==temp[3]&&temp[3]==temp[6]&&temp[6]==player) {
			num++;
		}
		if(temp[1]==temp[4]&&temp[4]==temp[7]&&temp[7]==player) {
			num++;
		}
		if(temp[2]==temp[5]&&temp[5]==temp[8]&&temp[8]==player) {
			num++;
		}
		if(temp[0]==temp[4]&&temp[4]==temp[8]&&temp[8]==player) {
			num++;
		}
		if(temp[2]==temp[4]&&temp[4]==temp[6]&&temp[6]==player) {
			num++;
		}
		return num;
	}
	
	public void fullArrayWith(int[] temp,int num) {
		for(int i=0;i<temp.length;i++) {
			if(temp[i]==-1) {
				temp[i]=num;
			}
		}
	}
	
	/**
	 * 计算值
	 * @param temp
	 * @return
	 */
	public void caculateSecondTable() {
		if(whoseTurn==1) {
			for(int i=0;i<secondPointer;i++) {
				if(secondTable[i][1]!=Integer.MIN_VALUE) {
					secondTable[i][1]=estimateSecondTable(secondTable[i], 1);
				}
			}
		}else {
			for(int i=0;i<secondPointer;i++) {
				if(secondTable[i][1]!=Integer.MIN_VALUE) {
					secondTable[i][1]=estimateSecondTable(secondTable[i], 2);
				}
			}
		}
	}
	
	public void selectTheSmallestInSecondTable() {
		int index=secondTable[0][1];	int pointer=0;
		for(int i=0;i<secondPointer;i++) {
			if(index>secondTable[i][1]) {
				index=secondTable[i][1];
				pointer=i;
			}
		}
		if(openTable[secondTable[pointer][0]][0]!=Integer.MAX_VALUE){
			openTable[secondTable[pointer][0]][0]=index;
		}
	}
	
	public void selectTheBigestInOpenTable() {
		int index=openTable[0][0]; int pointer=0;
		for(int i=0;i<openTailPointer;i++) {
			if(index<openTable[i][0]) {
				index=openTable[i][0];
				pointer=i;
			}
		}
		for(int i=1;i<openTable[pointer].length;i++) {
			currentState[(i-1)/3][(i-1)%3]=openTable[pointer][i];
		}
	}
	
	public int[] twoDimensionalToOneDimensional(int[][] temp) {
		int[] array=new int[temp.length*temp[0].length];
		for(int i=0;i<temp.length;i++)
			for(int j=0;j<temp[i].length;j++) {
				array[temp[i].length*i+j]=temp[i][j];
			}
		return array;
	}
	
	public boolean currentStateIsFull() {
		for(int i=0;i<currentState.length;i++)
			for(int j=0;j<currentState[i].length;j++) {
				if(currentState[i][j]==-1) {
					return false;
				}
			}
		return true;
	}
	
	public void clearSecondTable() {
		for(int i=0;i<secondPointer;i++)
			for(int j=0;j<secondTable[i].length;j++)
			{
				secondTable[i][j]=0;
			}
	}
	
	public void clearOpenTable() {
		for(int i=0;i<openHeadPointer;i++)
			for(int j=0;j<openTable[i].length;j++)
			{
				openTable[i][j]=0;
			}
	}
	
	public void machineVsMachineControler() {
		initialChessBoard(currentState);
		whoseTurn=1;
		while(calculateNum(twoDimensionalToOneDimensional(currentState),1)==0&&calculateNum(twoDimensionalToOneDimensional(currentState), 2)==0) {
			if(currentStateIsFull()) {
				System.out.println("-----------------------draw----------------------");//平手
				break;
			}
			insertIntoOpen(findEmptyInArray(currentState), whoseTurn);
			for(int i=0;i<openTailPointer;i++) {
				openTableIntoTempState();
				insertIntoSecondTable();
				caculateSecondTable();
			//	System.out.println("------------------secondTable--------------------");
				//printChessBoard(secondTable);//打印secondTable
				selectTheSmallestInSecondTable();
		//		System.out.println("------------------openTable------------------");
				//printChessBoard(openTable);//打印openTable
				clearSecondTable();
				secondPointer=0;
			}
			selectTheBigestInOpenTable();	
			clearOpenTable();
			openHeadPointer=0;
			openTailPointer=0;
			printChessBoard(currentState);
			System.out.println();
			if(whoseTurn==1) {
				whoseTurn=2;
			}else {
				whoseTurn=1;
			}
		}
	}
	
	public boolean isIn(int num,int[] array) {
		boolean flag=false;
		for(int i=0;i<array.length;i++) {
			if(array[i]!=-1&&num==array[i]) {
				flag=true;
			}
		}
		return flag;
	}
	
	public  void machineVsHumanControl(int whoIsFirst) {//whoIsFirst 1.电脑先走	2.人先走
		Scanner input=new Scanner(System.in);int playerSetPlace;
		initialChessBoard(currentState);
		if(whoIsFirst==1) {//电脑先走
			whoseTurn=1;
			while(calculateNum(twoDimensionalToOneDimensional(currentState),1)==0&&calculateNum(twoDimensionalToOneDimensional(currentState), 2)==0) {
				insertIntoOpen(findEmptyInArray(currentState), whoseTurn);
				for(int i=0;i<openTailPointer;i++) {
					openTableIntoTempState();
					insertIntoSecondTable();
					caculateSecondTable();
					System.out.println("-------------------secondTable------------------------");
					printChessBoard(secondTable);//打印secondTable
					selectTheSmallestInSecondTable();
					System.out.println("------------------openTable------------------------");
					printChessBoard(openTable);//打印openTable
					clearSecondTable();
					secondPointer=0;
				}
				selectTheBigestInOpenTable();	
				clearOpenTable();
				openHeadPointer=0;
				openTailPointer=0;
				printChessBoard(currentState);
				System.out.println();
				if(currentStateIsFull()) {
					System.out.println("-----------------------draw----------------------");//平手
					break;
				}
				int[] emptyPlace=findEmptyInArray(currentState);
				System.out.println("----------the empty place in chessboard-----------");
				for(int i=0;i<emptyPlace.length;i++) {
					System.out.print(emptyPlace[i]+" ");
				}
				System.out.println();
				playerSetPlace=input.nextInt();
				while(!isIn(playerSetPlace, emptyPlace)) {
					System.out.println("---------------illegal input,please input again----------------");
					playerSetPlace=input.nextInt();
				}
				currentState[playerSetPlace/chessSize][playerSetPlace%chessSize]=2;	
			}
			if(calculateNum(twoDimensionalToOneDimensional(currentState), 1)==1) {
				System.out.println("-----------------computer win------------------");
			}
			if(calculateNum(twoDimensionalToOneDimensional(currentState), 2)==1) {
				System.out.println("--------------------you win--------------------");
			}
		}else {//人先走
			whoseTurn=2;
			while(calculateNum(twoDimensionalToOneDimensional(currentState),1)==0&&calculateNum(twoDimensionalToOneDimensional(currentState), 2)==0) {		
				int[] emptyPlace=findEmptyInArray(currentState);
				System.out.println("----------the empty place in chessboard-----------");
				for(int i=0;i<emptyPlace.length;i++) {
					System.out.print(emptyPlace[i]+" ");
				}
				System.out.println();
				playerSetPlace=input.nextInt();
				while(!isIn(playerSetPlace, emptyPlace)) {
					System.out.println("---------------illegal input,please input again----------------");
					playerSetPlace=input.nextInt();
				}
				currentState[playerSetPlace/chessSize][playerSetPlace%chessSize]=1;
				if(currentStateIsFull()) {
					System.out.println("-----------------------draw----------------------");//平手
					break;
				}
				
				insertIntoOpen(findEmptyInArray(currentState), whoseTurn);
				for(int i=0;i<openTailPointer;i++) {
					openTableIntoTempState();
					insertIntoSecondTable();
					caculateSecondTable();
					System.out.println("----------------secondTable-------------------------");
					printChessBoard(secondTable);//打印secondTable
					selectTheSmallestInSecondTable();
					System.out.println("------------------openTable----------------------");
					printChessBoard(openTable);//打印openTable
					clearSecondTable();
					secondPointer=0;
				}
				selectTheBigestInOpenTable();
				clearOpenTable();
				openHeadPointer=0;
				openTailPointer=0;
				printChessBoard(currentState);
				System.out.println();
			}
			if(calculateNum(twoDimensionalToOneDimensional(currentState), 2)==1) {
				System.out.println("-----------------computer win------------------");
			}
			if(calculateNum(twoDimensionalToOneDimensional(currentState), 1)==1) {
				System.out.println("--------------------you win--------------------");
			}
		}
	}
	
	public static void main(String[] args) {
		yiziqi chess=new yiziqi();
		int kind;
		System.out.println("------------------------一字棋---------------------------");
		System.out.println("--select 1:machine vs machine--seclect 2:machine vs human--");
		Scanner input=new Scanner(System.in);
		kind=input.nextInt();
		if(kind==1) {
			chess.machineVsMachineControler();
		}else if(kind==2) {
			System.out.println("----------whoIsFirst: 1.computer 2.you--------------");
			int whoIsFirst=input.nextInt();
			chess.machineVsHumanControl(whoIsFirst);
		}else {
			System.out.println("illegal Input");
		}
	}	
}
