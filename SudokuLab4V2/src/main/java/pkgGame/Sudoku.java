package pkgGame;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import pkgHelper.LatinSquare;

/**
 * Sudoku - This class extends LatinSquare, adding methods, constructor to
 * handle Sudoku logic
 * 
 * @version 1.2
 * @since Lab #2
 * @author Bert.Gibbons
 *
 */
public class Sudoku extends LatinSquare {
	/**
	 * 
	 * iSize - the length of the width/height of the Sudoku puzzle.
	 * 
	 * @version 1.2
	 * @since Lab #2
	 */
	private int iSize;

	/**
	 * iSqrtSize - SquareRoot of the iSize. If the iSize is 9, iSqrtSize will be
	 * calculated as 3
	 * 
	 * @version 1.2
	 * @since Lab #2
	 */

	private int iSqrtSize;

	/**
	 * Sudoku - for Lab #2... do the following:
	 * 
	 * set iSize If SquareRoot(iSize) is an integer, set iSqrtSize, otherwise throw
	 * exception
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @param iSize-
	 *            length of the width/height of the puzzle
	 * @throws Exception
	 *             if the iSize given doesn't have a whole number square root
	 */
	
	private Map<Integer, Cell> cells = new HashMap<Integer, Cell>();
	
	public Sudoku(int iSize) throws Exception {

		this.iSize = iSize;

		double SQRT = Math.sqrt(iSize);
		if ((SQRT == Math.floor(SQRT)) && !Double.isInfinite(SQRT)) {
			this.iSqrtSize = (int) SQRT;
		} else {
			throw new Exception("Invalid size");
		}

		int[][] puzzle = new int[iSize][iSize];
		super.setLatinSquare(puzzle);
		FillDiagonalRegions();
	}

	/**
	 * Sudoku - pass in a given two-dimensional array puzzle, create an instance.
	 * Set iSize and iSqrtSize
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @param puzzle
	 *            - given (working) Sudoku puzzle. Use for testing
	 * @throws Exception
	 *             will be thrown if the length of the puzzle do not have a whole
	 *             number square root
	 */
	public Sudoku(int[][] puzzle) throws Exception {
		super(puzzle);
		this.iSize = puzzle.length;
		double SQRT = Math.sqrt(iSize);
		if ((SQRT == Math.floor(SQRT)) && !Double.isInfinite(SQRT)) {
			this.iSqrtSize = (int) SQRT;
		} else {
			throw new Exception("Invalid size");
		}

	}

	/**
	 * getPuzzle - return the Sudoku puzzle
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @return - returns the LatinSquare instance
	 */
	public int[][] getPuzzle() {
		return super.getLatinSquare();
	}

	/**
	 * getRegionNbr - Return region number based on given column and row
	 * 
	 * 
	 * Example, the following Puzzle:
	 * 
	 * 0 1 2 3 <br>
	 * 1 2 3 4 <br>
	 * 3 4 1 2 <br>
	 * 4 1 3 2 <br>
	 * 
	 * getRegionNbr(3,0) should return a value of 1
	 * 
	 * @param iCol - Given column number
	 * @param iRow - Given row number
	 * @version 1.3
	 * @since Lab #3
	 * 
	 * @return - return region number based on given column and row
	 */
	public int getRegionNbr(int iCol, int iRow) {

		int i = (iCol / iSqrtSize) + ((iRow / iSqrtSize) * iSqrtSize);

		return i;
	}

	/**
	 * getRegion - figure out what region you're in based on iCol and iRow and call
	 * getRegion(int)<br>
	 * 
	 * Example, the following Puzzle:
	 * 
	 * 0 1 2 3 <br>
	 * 1 2 3 4 <br>
	 * 3 4 1 2 <br>
	 * 4 1 3 2 <br>
	 * 
	 * getRegion(0,3) would call getRegion(1) and return [2],[3],[3],[4]
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @param iCol
	 *            given column
	 * @param iRow
	 *            given row
	 * @return - returns a one-dimensional array from a given region of the puzzle
	 */
	public int[] getRegion(int iCol, int iRow) {

		int i = (iCol / iSqrtSize) + ((iRow / iSqrtSize) * iSqrtSize);

		return getRegion(i);
	}

	/**
	 * getRegion - pass in a given region, get back a one-dimensional array of the
	 * region's content<br>
	 * 
	 * Example, the following Puzzle:
	 * 
	 * 0 1 2 3 <br>
	 * 1 2 3 4 <br>
	 * 3 4 1 2 <br>
	 * 4 1 3 2 <br>
	 * 
	 * getRegion(2) and return [3],[4],[4],[1]
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @param r
	 *            given region
	 * @return - returns a one-dimensional array from a given region of the puzzle
	 */

	public int[] getRegion(int r) {

		int[] reg = new int[super.getLatinSquare().length];

		int i = (r % iSqrtSize) * iSqrtSize;
		int j = (r / iSqrtSize) * iSqrtSize;
		int iMax = i + iSqrtSize;
		int jMax = j + iSqrtSize;
		int iCnt = 0;

		for (; j < jMax; j++) {
			for (i = (r % iSqrtSize) * iSqrtSize; i < iMax; i++) {
				reg[iCnt++] = super.getLatinSquare()[j][i];
			}
		}

		return reg;
	}

	/**
	 * isPartialSudoku - return 'true' if...
	 * 
	 * It's a LatinSquare If each region doesn't have duplicates If each element in
	 * the first row of the puzzle is in each region of the puzzle At least one of
	 * the elemnts is a zero
	 * 
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @return true if the given puzzle is a partial sudoku
	 */
	public boolean isPartialSudoku() {

		if (!super.isLatinSquare()) {
			return false;
		}

		for (int k = 0; k < this.getPuzzle().length; k++) {

			if (super.hasDuplicates(getRegion(k))) {
				return false;
			}

			if (!hasAllValues(getRow(0), getRegion(k))) {
				return false;
			}
		}

		if (ContainsZero()) {
			return false;
		}

		return true;

	}

	/**
	 * isSudoku - return 'true' if...
	 * 
	 * Is a partialSudoku Each element doesn't a zero
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @return - returns 'true' if it's a partialSudoku, element match (row versus
	 *         column) and no zeros
	 */
	public boolean isSudoku() {

		if (!isPartialSudoku()) {
			return false;
		}

		if (ContainsZero()) {
			return false;
		}

		return true;
	}

	/**
	 * isValidValue - test to see if a given value would 'work' for a given column /
	 * row
	 * 
	 * @version 1.2
	 * @since Lab #2
	 * @param iCol
	 *            puzzle column
	 * @param iRow
	 *            puzzle row
	 * @param iValue
	 *            given value
	 * @return - returns 'true' if the proposed value is valid for the row and column
	 */
	public boolean isValidValue(int iRow, int iCol,  int iValue) {
		
		if (doesElementExist(super.getRow(iRow),iValue))
		{
			return false;
		}
		if (doesElementExist(super.getColumn(iCol),iValue))
		{
			return false;
		}
		if (doesElementExist(this.getRegion(iCol, iRow),iValue))
		{
			return false;
		}
		
		return true;
	}

	/**
	 * PrintPuzzle This method will print the puzzle to the console (space between
	 * columns, line break after row)
	 * 
	 * @version 1.3
	 * @since Lab #3
	 */
	public void PrintPuzzle() {
		for (int i = 0; i < this.getPuzzle().length; i++) {
			System.out.println("");
			for (int j = 0; j < this.getPuzzle().length; j++) {
				System.out.print(this.getPuzzle()[i][j]);
				if ((j + 1) % iSqrtSize == 0)
					System.out.print(" ");
			}
			if ((i + 1) % iSqrtSize == 0)
				System.out.println(" ");

		}
		System.out.println("");
	}

	/**
	 * FillDiagonalRegions - After the puzzle is created, set the diagonal regions
	 * with random values
	 * 
	 * @version 1.3
	 * @since Lab #3
	 */
	public void FillDiagonalRegions() {

		for (int i = 0; i < iSize; i = i + iSqrtSize) {
			System.out.println("Filling region: " + getRegionNbr(i, i));
			SetRegion(getRegionNbr(i, i));
			ShuffleRegion(getRegionNbr(i, i));
		}
	}

	/**
	 * SetRegion - purpose of this method is to set the values of a given region
	 * (they will be shuffled later)
	 * 
	 * Example, the following Puzzle start state:
	 * 
	 * 0 0 0 0 <br>
	 * 0 0 0 0 <br>
	 * 0 0 0 0 <br>
	 * 0 0 0 0 <br>
	 * 
	 * SetRegion(2) would transform the Puzzle to:<br>
	 * 
	 * 0 0 0 0 <br>
	 * 0 0 0 0 <br>
	 * 1 2 0 0 <br>
	 * 3 4 0 0 <br>
	 * 
	 * @version 1.3
	 * @since Lab #3
	 * @param r
	 *            - Given region number
	 */
	private void SetRegion(int r) {
		int iValue = 0;

		iValue = 1;
		for (int i = (r / iSqrtSize) * iSqrtSize; i < ((r / iSqrtSize) * iSqrtSize) + iSqrtSize; i++) {
			for (int j = (r % iSqrtSize) * iSqrtSize; j < ((r % iSqrtSize) * iSqrtSize) + iSqrtSize; j++) {
				this.getPuzzle()[i][j] = iValue++;
			}
		}
	}
	

	/**
	 * SetRegion - purpose of this method is to set the values of a given region
	 * (they will be shuffled later)
	 * 
	 * Example, the following Puzzle start state:
	 * 
	 * 1 2 0 0 <br>
	 * 3 4 0 0 <br>
	 * 0 0 0 0 <br>
	 * 0 0 0 0 <br>
	 * 
	 * ShuffleRegion(0) might transform the Puzzle to:<br>
	 * 
	 * 2 3 0 0 <br>
	 * 1 4 0 0 <br>
	 * 0 0 0 0 <br>
	 * 0 0 0 0 <br>
	 * 
	 * @version 1.3
	 * @since Lab #3
	 * @param r
	 *            - Given region number
	 */
	private void ShuffleRegion(int r) {
		int[] region = getRegion(r);
		shuffleArray(region);
		int iCnt = 0;
		for (int i = (r / iSqrtSize) * iSqrtSize; i < ((r / iSqrtSize) * iSqrtSize) + iSqrtSize; i++) {
			for (int j = (r % iSqrtSize) * iSqrtSize; j < ((r % iSqrtSize) * iSqrtSize) + iSqrtSize; j++) {
				this.getPuzzle()[i][j] = region[iCnt++];
			}
		}
	}

	/**
	 * shuffleArray this method will shuffle a given one-dimension array
	 * 
	 * @version 1.3
	 * @since Lab #3
	 * @param ar
	 *            given one-dimension array
	 */
	private void shuffleArray(int[] ar) {

		Random rand = new SecureRandom();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rand.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
	
	public boolean isValidRowValue(int iRow, int iValue) {
		
		return doesElementExist(getRow(iRow), iValue) ? false : true;
	}

	public boolean isValidColumnValue(int iCol, int iValue) {
		
		return doesElementExist(getColumn(iCol), iValue) ? false : true;
	}

	public boolean isValidRegionValue(int iRow, int iCol, int iValue) {
		
		return doesElementExist(getRegion(iCol, iRow), iValue) ? false : true;
	}

	public ArrayList<Integer> validValues(int iRow, int iCol) {
		
		ArrayList<Integer> validValues = new ArrayList<Integer>();
		
		for (int i = 1; i <= iSize; i++) {
			if (!doesElementExist(getRow(iRow), i) &&
					!doesElementExist(getColumn(iCol), i) && 
					!doesElementExist(getRegion(iCol, iRow), i)) {
				validValues.add(i);
			}
		}
		return validValues;
	}
	
	static int counter;
	
	public int getCounter() {
		return counter;
	}
	
	private static int stepBackAmount;
	
	public boolean fillRemaining(int iRow, int iCol) {
		counter++;
		if (counter > 2250)
			return false;
		//assume iRow and iCol will be the first value to be entered
		int randomIndex;
		if (iRow != iSize && iCol != iSize) {
			//handle for randomly generated diagonals
			if (this.getPuzzle()[iRow][iCol] != 0) {
				iCol++;
				if (iCol >= iSize) {
					iRow++;
					iCol = 0;
				}
				return fillRemaining(iRow, iCol);
			}
			
			//backwards
			System.out.println("Poss Values Size: " + this.validValues(iRow, iCol).size());
			if (this.validValues(iRow, iCol).size() == 0) {
				System.out.println("SIZE 0?" + iRow + " " + iCol + this.validValues(iRow, iCol));
				do {
					int[] stepsBackArray = {1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,3,4,5,6,7};
					int stepsBack = stepsBackArray[ThreadLocalRandom.current().nextInt(0, 20)];
					System.out.println("Steps taken backwards: " + stepsBack);
					for (int i = 0; i < stepsBack; i++) {
						iCol--;
						if (iCol < 0) {
							iRow--;
							iCol = iSize - 1;
							}
						if (iRow < 0) {
							iRow = 0;
							iCol = 0;
						}
						this.getPuzzle()[iRow][iCol] = 0;
					}	
				} while (this.validValues(iRow, iCol).size() == 1);
				System.out.println("backtracked to " + iRow + " " + iCol);
				this.PrintPuzzle();
			}

			//forwards
			System.out.println("Beginning " + iRow + " " + iCol);
			System.out.println("VV " + this.validValues(iRow, iCol));
			if (this.validValues(iRow, iCol).size() == 1) {
				randomIndex = 0;
			} else {
				randomIndex = ThreadLocalRandom.current().nextInt(0, this.validValues(iRow, iCol).size());
			}
			System.out.println("RI " + randomIndex);
			this.getPuzzle()[iRow][iCol] = this.validValues(iRow, iCol).get(randomIndex);
			//update iRow and iCol
			System.out.println("Finished with: " + iRow + " " + iCol);
			System.out.println("Recursive Call Number: " + counter);
			System.out.println("\n");
			iCol++;
			if (iCol >= iSize) {
				iRow++;
				iCol = 0;
			}
			this.PrintPuzzle();
			return fillRemaining(iRow, iCol);
		} else {
			return true;
		}
	}
	
//	public boolean fillRemainingLooping(int iRow, int iCol) {
//		//always set to 0
//		iRow = 0;
//		iCol = 0;
//		//solve for each cell
//		for (int i = 0; i < iSize * iSize; i++) {
//			//ignore randomly generated diagonals
//			if (this.getPuzzle()[iRow][iCol] != 0) {
//				iCol++;
//				if (iCol >= iSize) {
//					iRow++;
//					iCol = 0;
//				}
//				continue;
//			}
//			
//			//backwards
//			System.out.println("Poss Values Size: " + this.validValues(iRow, iCol).size());
//			if (this.validValues(iRow, iCol).size() == 0) {
//				System.out.println("SIZE is 0 for: " + iRow + " " + iCol + this.validValues(iRow, iCol));
//				do {
//					iCol--;
//					if (iCol < 0) {
//						iRow--;
//						iCol = iSize - 1;
//					}
//					if (iRow < 0) {
//						iRow = 0;
//						iCol = 0;
//					}
//					this.getPuzzle()[iRow][iCol] = 0;
//				} while (this.validValues(iRow, iCol).size() == 1);
//				System.out.println("backtracked to " + iRow + " " + iCol);
//				this.PrintPuzzle();
//			}
//
//			//forwards
//			System.out.println("Beginning " + iRow + " " + iCol);
//			System.out.println("VV " + this.validValues(iRow, iCol));
//			int randomIndex;
//			if (this.validValues(iRow, iCol).size() == 1) {
//				randomIndex = 0;
//			} else {
//				randomIndex = ThreadLocalRandom.current().nextInt(0, this.validValues(iRow, iCol).size());
//			}
//			System.out.println("RI " + randomIndex);
//			this.getPuzzle()[iRow][iCol] = this.validValues(iRow, iCol).get(randomIndex);
//			//update iRow and iCol
//			System.out.println("Finished with: " + iRow + " " + iCol);
//			System.out.println("Recursive Call Number: " + counter);
//			System.out.println("\n");
//			iCol++;
//			if (iCol >= iSize) {
//				iRow++;
//				iCol = 0;
//			}
//			this.PrintPuzzle();
//			continue;
//			}
//		return true;
//		}
//	}
	private HashSet<Integer> getAllValidCellValues(int iCol, int iRow) {
		return new HashSet<Integer>(cells.get(Objects.hash(iRow, iCol)).getLstValidValues());		
	}
	
	private void SetCells() {
		int iRow = 0;
		int iCol = 0;
		do {
			Cell c = new Cell(iRow, iCol);
			c.setlstValidValues(getAllValidCellValues(iRow, iCol));
			c.ShuffleValidValues();
			cells.put(c.hashCode(), c);
			
			iCol++;
			if (iCol >= iSize) {
				iRow++;
				iCol = 0;
			}

		} while(iRow - 1 != iSize && iCol - 1 != iSize);
	}
	
	private void fillRemaining() {
		
		Cell c = new Cell(0, 0);
		while (c.GetNextCell(c) != null) {
			//
		}
	}
	
	private class Cell extends Sudoku {
		
		private int iRow;
		private int iCol;
		private ArrayList<Integer> lstValidValues = new ArrayList<Integer>();
		
		public Cell(int iRow, int iCol) {
			super();
			this.iRow = iRow;
			this.iCol = iCol;
			setlstValidValues();
		}
		
		public int getiRow() {
			return iRow;
		}
		
		public int getiCol() {
			return iCol;
		}
		
		public int hashCode() {
			return Objects.hash(iRow, iCol);
		}
		
		@Override 
		public boolean equals(Object o) {
			if (o == this)
				return false;
			if (!(o instanceof Cell))
				return false;
			else {
				Cell c = (Cell) o;
				return ((this.iRow == c.getiRow()) && (this.iCol == c.getiCol()));
			}
		}
		
		public ArrayList<Integer> getLstValidValues() {
			ArrayList<Integer> validValues = new ArrayList<Integer>();
			
			for (int i = 1; i <= iSize; i++) {
				if (!doesElementExist(getRow(iRow), i) &&
						!doesElementExist(getColumn(iCol), i) && 
						!doesElementExist(getRegion(iCol, iRow), i)) {
					validValues.add(i);
				}
			}
			return validValues;
		}
		
		public void setlstValidValues(HashSet<Integer> hs) {
			this.lstValidValues = new ArrayList<Integer>(hs);
		}
		
		public void ShuffleValidValues() {
			ArrayList<Integer> templstValidValues = this.lstValidValues;

			Collections.shuffle(templstValidValues, new Random());
			
			this.lstValidValues = templstValidValues;
		}		
		
		public Cell GetNextCell(Cell c) {
			int iRow = getiRow();
			int iCol = getiCol();
			
			if (iRow == (iSize - 1) && iCol == (iSize - 1)) {
				return null;
			}
			
			iCol++;
			if (iCol >= iSize) {
				iRow++;
				iCol = 0;
			}
			
			return cells.get(Objects.hash(iRow, iCol));
		}
	}
}
