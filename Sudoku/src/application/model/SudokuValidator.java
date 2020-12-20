package application.model;

import javafx.scene.control.TextField;

public class SudokuValidator {
	// constante pour le nombre de threads
	private static final int NUM_THREADS = 27;
	// Sudoku puzzle solution à valider
	public static int[][] sudoku = {
			{6, 2, 4, 5, 3, 9, 1, 8, 7},
			{5, 1, 9, 7, 2, 8, 6, 3, 4},
			{8, 3, 7, 6, 1, 4, 2, 9, 5},
			{1, 4, 3, 8, 6, 5, 7, 2, 9},
			{9, 5, 8, 2, 4, 7, 3, 6, 1},
			{7, 6, 2, 3, 9, 1, 4, 5, 8},
			{3, 7, 1, 9, 5, 6, 8, 4, 2},
			{4, 9, 6, 1, 8, 2, 5, 7, 3},
			{2, 8, 5, 4, 7, 3, 9, 1, 6}
	};
	
	public static TextField[][] champs = null; 
	
	// Array que les thread vont mettre à jour
	private static boolean[] valid;
	
	// Classe parent qui sera étendu par les classes Thread, contient uniquement
	// la ligne et la colonne concernant le hread
	public static class RowColumnObject {
		int row;
		int col;
		RowColumnObject(int row, int column) {
			this.row = row;
			this.col = column;
		}
	}
	
	// Runnable object that determines if numbers 1-9 only appear once in a row
	public static class IsRowValid extends RowColumnObject implements Runnable {		
		IsRowValid(int row, int column) {
			super(row, column); 
		}

		@Override
		public void run() {
			if (col != 0 || row > 8) {
				System.out.println("Invalid row or column for row subsection!");				
				return;
			}
			
			// ce tableau vérifie l'unicité des nombres 1-9 sur la ligne
			// lorsqu'on trouve 1 => validityArray[0] = true;
			boolean[] validityArray = new boolean[9];
			int i;
			for (i = 0; i < 9; i++) {
				// If the corresponding index for the number is set to 1, and the number is encountered again,
				// the valid array will not be updated and the thread will exit.
				int num = sudoku[row][i];
				if (num < 1 || num > 9 || validityArray[num - 1]) {
					champs[row][i].getStyleClass().add("error");
					return;
				} else if (!validityArray[num - 1]) {
					validityArray[num - 1] = true;
				}
			}
			
			// lorsqu'on atteint cette ligne , c'est que tout est correct.
			valid[9 + row] = true;
			
		}

	}
	
	// Runnable object that determines if numbers 1-9 only appear once in a column
	public static class IsColumnValid extends RowColumnObject implements Runnable {
		IsColumnValid(int row, int column) {
			super(row, column); 
		}

		@Override
		public void run() {
			if (row != 0 || col > 8) {
				System.out.println("Invalid row or column for col subsection!");				
				return;
			}
			
			// Check if numbers 1-9 only appear once in the column
			boolean[] validityArray = new boolean[9];
			int i;
			for (i = 0; i < 9; i++) {
				// If the corresponding index for the number is set to 1, and the number is encountered again,
				// the valid array will not be updated and the thread will exit.
				int num = sudoku[i][col];
				if (num < 1 || num > 9 || validityArray[num - 1]) {
					champs[i][col].getStyleClass().add("error");
					return;
				} else if (!validityArray[num - 1]) {
					validityArray[num - 1] = true;
				}
			}
			// Arrivé ici, la colonne est valide.
			valid[18 + col] = true;			
		}		
	}
	
	// Runnable object that determines if numbers 1-9 only appear once in a 3x3 subsection
	public static class Is3x3Valid extends RowColumnObject implements Runnable {
		Is3x3Valid(int row, int column) {
			super(row, column); 
		}

		@Override
		public void run() {
			// Confirm valid parameters
			if (row > 6 || row % 3 != 0 || col > 6 || col % 3 != 0) {
				System.out.println("Invalid row or column for subsection!");
				return;
			}
			
			// Check if numbers 1-9 only appear once in 3x3 subsection
			boolean[] validityArray = new boolean[9];			
			for (int i = row; i < row + 3; i++) {
				for (int j = col; j < col + 3; j++) {
					int num = sudoku[i][j];
					if (num < 1 || num > 9 || validityArray[num - 1]) {
						champs[i][j].getStyleClass().add("error");
						return;
					} else {
						validityArray[num - 1] = true;		
					}
				}
			}
			// Arrivé ici, la section 3x3 est correcte
			valid[row + col/3] = true; // Maper le sous section à un index de 0 à 8 dans le array valid			
		}
		
	}
	
	public boolean verifier() {
		valid = new boolean[NUM_THREADS];		
		Thread[] threads = new Thread[NUM_THREADS];
		int threadIndex = 0;
		// Créer 9 threads pour 9 3x3 sous sections, 9 threads pour 9 colonnes et 9 threads pour 9 lignes.
		// This will end up with a total of 27 threads.
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {						
				if (i%3 == 0 && j%3 == 0) {
					threads[threadIndex++] = new Thread(new Is3x3Valid(i, j));				
				}
				if (i == 0) {					
					threads[threadIndex++] = new Thread(new IsColumnValid(i, j));
				}
				if (j == 0) {
					threads[threadIndex++] = new Thread(new IsRowValid(i, j));					
				}
			}
		}
		
		// Start all threads
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		
		// Wait for all threads to finish
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Si un seul élément du tableau valid est resté false, alors le sudoku n'est pas valide
		for (int i = 0; i < valid.length; i++) {
			if (!valid[i]) {
//				System.out.println("Sudoku solution is invalid!");
				return false;
			}
		}
//		System.out.println("Sudoku solution is valid!");
		return true;
	}
}
