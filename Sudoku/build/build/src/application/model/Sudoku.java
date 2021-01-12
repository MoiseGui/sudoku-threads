package application.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Sudoku {

	private int[][] grid;
	private int size, sizec, sizer;
	private String filePath;
	boolean isValid = false;

	public Sudoku(int[][] grid, int size, int sizec, int sizer) {
		grid = this.grid;
		size = this.size;
		sizec = this.sizec;
		sizer = this.sizer;
	}

	public Sudoku() {

	}

	public void count_line() {
		this.size = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {

			@SuppressWarnings("unused")
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				this.size++;

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public void initGridFile() {
		this.grid = new int[this.size][this.size];

		try {

			File file = new File(this.filePath);
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {

				for (int i = 0; i < this.size; i++) {
					for (int j = 0; j < this.size; j++) {
						this.grid[i][j] = sc.nextInt();

					}
				}

			}
			sc.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public void initGrid() {

		this.grid = new int[this.size][this.size];

		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.grid[i][j] = 0;

			}
		}
	}

	public void resetGrid() {
		if (grid != null) {
			for (int i = 0; i < this.size; i++) {
				for (int j = 0; j < this.size; j++) {
					this.grid[i][j] = 0;
				}

			}
		}
	}

	public void diplay_grid() {

		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				System.out.println(this.grid[i][j]);

			}
		}

	}

	
	// cette méthode vérifie que tous les nombres de la grille sont compris entre 0 et la taille du sidoku (9)
	public boolean ValidGrid() {
		for (int i = 0; i < this.grid.length; i++) {
			for (int j = 0; j < this.grid.length; j++) {
				if (this.grid[i][j] > getSize() || this.grid[i][j] < 0) {
					return false;
				}
			}
		}
		return true;
	}

	public void setSizeCell() {
		this.sizec = 3;
		this.sizer = 3;
	}

	
	// cette méthode vérifie si un nombre donné (k) existe déjà sur la colonne spécifiée (j)
	public boolean notInColumn(int k, int j) {

		int i;
		for (i = 0; i < this.size; i++) {
			if (this.grid[i][j] == k)
				return false;
		}
		return true;
	}

	// cette méthode vérifie si un nombre donné (k) existe déjà sur la ligne spécifiée (j)
	public boolean notInRow(int k, int i) {
		int j;
		for (j = 0; j < this.size; j++) {
			if (this.grid[i][j] == k)
				return false;
		}
		return true;
	}

	
	// cette méthode vérifie si un nombre donné (k) existe déjà sur le tableau 3x3 spécifiée (i, j)
	public boolean notInCell(int k, int i, int j) {

		int i2 = i - (i % this.sizec);
		int j2 = j - (j % this.sizer);
		for (i = i2; i < i2 + this.sizec; i++)
			for (j = j2; j < j2 + this.sizer; j++)
				if (this.grid[i][j] == k)
					return false;
		return true;
	}


	
	// Cette classe sera utilisée par les Thread pour vérifier et valider la cellule d'une position donnée
	public class IsValid implements Runnable {
		int position;
		
		IsValid(int position){
			this.position = position;
		}

		@Override
		public void run() {
			
			if (position == size * size) {	
				isValid = true;
				return;
			}

			int i = position / size;  // Exemple: pour position = 8, i = 0, j = 8
			int j = position % size;  // Exemple: pour position = 9, i = 1, j = 0

			// si une valeur non nulle existe déjà, on passe à la cellule suivante.
			if (grid[i][j] != 0) {
//				return isValid(position + 1);
				Thread thread = new Thread(new IsValid(position+1));
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return;
			}

			
			// pour k = 1 - 9, on cherche la valeur de k qui n'existe ni sur sa ligne, ni sur sa colonne, ni dans son 3x3
			int k;
			for (k = 1; k <= size; k++) {

				if (notInColumn(k, j) == true && notInRow(k, i) == true && notInCell(k, i, j) == true) {

					// une fois trouvé, on peut le mettre à cette position
					grid[i][j] = k;
					
					Thread thread = new Thread(new IsValid(position+1));
					thread.start();
					try {
						thread.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (isValid)
						return;
				}
			}
			grid[i][j] = 0;

			return;
			
		}
		
	}
	
	
	// cette méthode vérifie et résoud la grille si possible (position = 0 - 80)
	// Elle utilise des Thread
	public boolean isGridValid(int position) {
		
		Thread thread = new Thread(new IsValid(position));
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isValid;
	}
	
	// Cette méthode est la version sans thread de la précédente
	public boolean isValid(int position) {

		if (position == this.size * this.size)
			return true;

		int i = position / this.size;  // Exemple: pour position = 8, i = 0, j = 8
		int j = position % this.size;  // Exemple: pour position = 9, i = 1, j = 0

		// si une valeur non nulle existe déjà, on passe à la cellule suivante.
		if (this.grid[i][j] != 0) {
			return isValid(position + 1);
		}

		
		// pour k = 1 - 9, on cherche la valeur de k qui n'existe ni sur sa ligne, ni sur sa colonne, ni dans son 3x3
		int k;
		for (k = 1; k <= this.size; k++) {

			if (notInColumn(k, j) == true && notInRow(k, i) == true && notInCell(k, i, j) == true) {

				// une fois trouvé, on peut le mettre à cette position
				this.grid[i][j] = k;

				if (isValid(position + 1))
					return true;
			}
		}
		this.grid[i][j] = 0;

		return false;
	}

	public int[][] getGrid() {
		return grid;
	}

	public boolean isEmpty() {
		int a = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				a = a + this.grid[i][j];
			}
		}
		if (a == 0) {
			return true;
		}
		return false;
	}

	public void setGrid(int[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				this.grid[i][j] = grid[i][j];
			}
		}
	}

	public int getSizec() {
		return sizec;
	}

	public void setSizec(int sizec) {
		this.sizec = sizec;
	}

	public int getSizer() {
		return sizer;
	}

	public void setSizer(int sizer) {
		this.sizer = sizer;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getSize() {
		return size;
	}

}
