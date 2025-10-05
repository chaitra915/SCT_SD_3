import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuSolverGUI extends JFrame {
    private static final int SIZE = 9;
    private JTextField[][] cells = new JTextField[SIZE][SIZE];
    private int[][] board = new int[SIZE][SIZE];

    public SudokuSolverGUI() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(createGridPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createGridPanel() {
        JPanel panel = new JPanel(new GridLayout(SIZE, SIZE));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(tf.getFont().deriveFont(Font.BOLD, 20f));
                // Thicker borders for 3x3 boxes
                int top = (r % 3 == 0) ? 3 : 1;
                int left = (c % 3 == 0) ? 3 : 1;
                int bottom = (r == SIZE-1) ? 3 : 1;
                int right = (c == SIZE-1) ? 3 : 1;
                tf.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
                panel.add(tf);
                cells[r][c] = tf;
            }
        }
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel p = new JPanel();
        JButton loadSample = new JButton("Load Sample");
        JButton solve = new JButton("Solve");
        JButton clear = new JButton("Clear");

        loadSample.addActionListener(e -> loadSamplePuzzle());
        solve.addActionListener(e -> {
            readBoardFromUI();
            if (solveBoard(0, 0)) {
                writeBoardToUI();
                JOptionPane.showMessageDialog(this, "Solved!");
            } else {
                JOptionPane.showMessageDialog(this, "No solution found.");
            }
        });
        clear.addActionListener(e -> clearBoard());

        p.add(loadSample);
        p.add(solve);
        p.add(clear);
        return p;
    }

    private void loadSamplePuzzle() {
        int[][] sample = {
            {5,3,0, 0,7,0, 0,0,0},
            {6,0,0, 1,9,5, 0,0,0},
            {0,9,8, 0,0,0, 0,6,0},

            {8,0,0, 0,6,0, 0,0,3},
            {4,0,0, 8,0,3, 0,0,1},
            {7,0,0, 0,2,0, 0,0,6},

            {0,6,0, 0,0,0, 2,8,0},
            {0,0,0, 4,1,9, 0,0,5},
            {0,0,0, 0,8,0, 0,7,9}
        };
        for (int r=0; r<SIZE; r++)
            for (int c=0; c<SIZE; c++) {
                board[r][c] = sample[r][c];
                cells[r][c].setText(sample[r][c] == 0 ? "" : Integer.toString(sample[r][c]));
                cells[r][c].setForeground(Color.BLUE);
            }
    }

    private void readBoardFromUI() {
        for (int r=0; r<SIZE; r++) {
            for (int c=0; c<SIZE; c++) {
                String txt = cells[r][c].getText().trim();
                try {
                    board[r][c] = txt.isEmpty() ? 0 : Integer.parseInt(txt);
                } catch (NumberFormatException ex) {
                    board[r][c] = 0;
                }
            }
        }
    }

    private void writeBoardToUI() {
        for (int r=0; r<SIZE; r++) {
            for (int c=0; c<SIZE; c++) {
                cells[r][c].setText(Integer.toString(board[r][c]));
                cells[r][c].setForeground(Color.BLACK);
            }
        }
    }

    private void clearBoard() {
        for (int r=0; r<SIZE; r++)
            for (int c=0; c<SIZE; c++) {
                board[r][c] = 0;
                cells[r][c].setText("");
            }
    }

    // Backtracking solver
    private boolean solveBoard(int row, int col) {
        if (row == SIZE) return true;
        int nextRow = (col == SIZE-1) ? row+1 : row;
        int nextCol = (col == SIZE-1) ? 0 : col+1;

        if (board[row][col] != 0) {
            return solveBoard(nextRow, nextCol);
        }

        for (int num=1; num<=9; num++) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                if (solveBoard(nextRow, nextCol)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isValid(int row, int col, int num) {
        // Row & column
        for (int i=0; i<SIZE; i++) {
            if (board[row][i] == num) return false;
            if (board[i][col] == num) return false;
        }
        // 3x3 box
        int br = (row/3)*3;
        int bc = (col/3)*3;
        for (int r=br; r<br+3; r++)
            for (int c=bc; c<bc+3; c++)
                if (board[r][c] == num) return false;

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuSolverGUI());
    }
}